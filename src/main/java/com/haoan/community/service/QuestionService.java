package com.haoan.community.service;

import com.haoan.community.bean.Question;
import com.haoan.community.bean.QuestionExample;
import com.haoan.community.bean.User;
import com.haoan.community.bean.UserExample;
import com.haoan.community.dto.PageInfoDTO;
import com.haoan.community.dto.QuestionUserDTO;
import com.haoan.community.exception.CustomizeErrorCode;
import com.haoan.community.exception.CustomizeException;
import com.haoan.community.mapper.QuestionExMapper;
import com.haoan.community.mapper.QuestionMapper;
import com.haoan.community.mapper.UserMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionExMapper questionExMapper;

    @Autowired
    UserMapper userMapper;

    /**
     * 首页问题列表集分页
     * @param page
     * @param size
     * @return
     */
    public PageInfoDTO findAll(Integer page,Integer size){
        //对分页信息进行封装
        PageInfoDTO<QuestionUserDTO> pageInfoDTO = new PageInfoDTO<>();
        Integer count =(int) questionMapper.countByExample(new QuestionExample());
        pageInfoDTO.setPageInfo(count,page,size);

        //保证page不越界
        if(page<1){
            page=1;
        }
        if(page>pageInfoDTO.getTotalPage()){
            page=pageInfoDTO.getTotalPage();
        }

        //数据库查询当前页问题
        Integer offset = size * (page-1);
        QuestionExample questionExample=new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds(offset,size));

        //对问题内容封装到questionUserDTOs
        List<QuestionUserDTO> questionUserDTOs = new ArrayList<>();
        for(Question question:questions){

            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andIdEqualTo(question.getCreator());
            User user = userMapper.selectByExample(userExample).get(0);

            QuestionUserDTO questionUserDTO= new QuestionUserDTO();
            questionUserDTO.setQuestion(question);
            questionUserDTO.setUser(user);
            questionUserDTOs.add(questionUserDTO);
        }
        //将questionUserDTOs封装到pageInfoDTO  与分页信息一起返回
        pageInfoDTO.setData(questionUserDTOs);

        return pageInfoDTO;
    }

    /**
     * 问题详情页
     * @param id
     */
    public QuestionUserDTO findById(Long id) {

        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdEqualTo(question.getCreator());
        User user = userMapper.selectByExample(userExample).get(0);

        QuestionUserDTO questionUserDTO = new QuestionUserDTO();
        questionUserDTO.setQuestion(question);
        questionUserDTO.setUser(user);
        return questionUserDTO;
    }




    public Long saveOrUpdate(Question question) {
        if(question.getId()==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
            return  questionExMapper.lastInsertId();
        }else {
            question.setGmtModified(System.currentTimeMillis());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(question.getId());
            int i = questionMapper.updateByExampleSelective(question, questionExample);
            if(i!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            return question.getId();
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExMapper.incView(question);
        //n个用户同时访问  同时取到游览数  15   然后同时加1   阅读数为 16  而不是 15+n
//        Question question = questionMapper.selectByPrimaryKey(id);
//        question.setViewCount(question.getViewCount()+1);
//        QuestionExample questionExample = new QuestionExample();
//        questionExample.createCriteria()
//                .andIdEqualTo(question.getId());
//        questionMapper.updateByExampleSelective(question,questionExample);
    }

    public List<Question> selectRelated(Question question) {
        String tag = question.getTag();
        String replace = StringUtils.replace(tag, ",", "|");
        List<Question> questions = questionExMapper.selectRelated(replace, question.getId());
        return questions;
    }
}
