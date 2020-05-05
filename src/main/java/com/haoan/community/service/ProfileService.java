package com.haoan.community.service;

import com.haoan.community.bean.Question;
import com.haoan.community.bean.QuestionExample;
import com.haoan.community.bean.User;
import com.haoan.community.bean.UserExample;
import com.haoan.community.dto.PageInfoDTO;
import com.haoan.community.dto.QuestionUserDTO;
import com.haoan.community.mapper.QuestionMapper;
import com.haoan.community.mapper.UserMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    public PageInfoDTO findAll(Long id, Integer page, Integer size) {
        //对分页信息进行封装
        PageInfoDTO<QuestionUserDTO> pageInfoDTO = new PageInfoDTO<>();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(id);
        Integer count = (int)questionMapper.countByExample(questionExample);
        pageInfoDTO.setPageInfo(count, page, size);

        //保证page不越界
        if (page < 1) {
            page = 1;
        }
        if (page > pageInfoDTO.getTotalPage()) {
            page = pageInfoDTO.getTotalPage();
        }

        //数据库查询当前页问题
        Integer offset = size * (page - 1);
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds(offset,size));

        //对问题内容封装到questionUserDTOs
        List<QuestionUserDTO> questionUserDTOs = new ArrayList<>();
        for (Question question : questions) {
            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andIdEqualTo(question.getCreator());
            User user = userMapper.selectByExample(userExample).get(0);
            QuestionUserDTO questionUserDTO = new QuestionUserDTO();
            questionUserDTO.setQuestion(question);
            questionUserDTO.setUser(user);
            questionUserDTOs.add(questionUserDTO);
        }
        //将questionUserDTOs封装到pageInfoDTO  与分页信息一起返回
        pageInfoDTO.setData(questionUserDTOs);

        return pageInfoDTO;
    }
}
