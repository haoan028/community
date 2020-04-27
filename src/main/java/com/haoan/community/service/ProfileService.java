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
public class ProfileService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    public PageInfoDTO findAll(String id, Integer page, Integer size) {
        //对分页信息进行封装
        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        Integer count = questionMapper.userCount(id);
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
        List<Question> questions = questionMapper.findByUser(id, offset, size);

        //对问题内容封装到questionUserDTOs
        List<QuestionUserDTO> questionUserDTOs = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionUserDTO questionUserDTO = new QuestionUserDTO();
            questionUserDTO.setQuestion(question);
            questionUserDTO.setUser(user);
            questionUserDTOs.add(questionUserDTO);
        }
        //将questionUserDTOs封装到pageInfoDTO  与分页信息一起返回
        pageInfoDTO.setQuestionUserDTO(questionUserDTOs);

        return pageInfoDTO;
    }
}
