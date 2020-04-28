package com.haoan.community.service;

import com.haoan.community.bean.Question;
import com.haoan.community.bean.User;
import com.haoan.community.dto.PageInfoDTO;
import com.haoan.community.dto.QuestionUserDTO;
import com.haoan.community.mapper.QuestionMapper;
import com.haoan.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

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
        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        Integer count = questionMapper.count();
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
        List<Question> questions = questionMapper.findAll(offset,size);

        //对问题内容封装到questionUserDTOs
        List<QuestionUserDTO> questionUserDTOs = new ArrayList<>();
        for(Question question:questions){
            User user = userMapper.findById(question.getCreator());
            QuestionUserDTO questionUserDTO= new QuestionUserDTO();
            questionUserDTO.setQuestion(question);
            questionUserDTO.setUser(user);
            questionUserDTOs.add(questionUserDTO);
        }
        //将questionUserDTOs封装到pageInfoDTO  与分页信息一起返回
        pageInfoDTO.setQuestionUserDTO(questionUserDTOs);

        return pageInfoDTO;
    }

    /**
     * 问题详情页
     * @param id
     */
    public QuestionUserDTO findById(Integer id) {

        Question question = questionMapper.findById(id);
        User user = userMapper.findById(question.getCreator());
        QuestionUserDTO questionUserDTO = new QuestionUserDTO();
        questionUserDTO.setQuestion(question);
        questionUserDTO.setUser(user);
        return questionUserDTO;
    }




    public Integer saveOoUpdate(Question question) {
        if(question.getId()==null){
            question.setGmt_create(System.currentTimeMillis());
            question.setGmt_modified(question.getGmt_create());
            questionMapper.saveQuestion(question);
            return questionMapper.lastInsertId();
        }else {
            question.setGmt_modified(System.currentTimeMillis());
            questionMapper.update(question);
            return question.getId();
        }
    }
}
