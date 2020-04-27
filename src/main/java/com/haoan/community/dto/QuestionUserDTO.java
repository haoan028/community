package com.haoan.community.dto;

import com.haoan.community.bean.Question;
import com.haoan.community.bean.User;
import lombok.Data;

@Data
public class QuestionUserDTO {
    private Question question;
    private User user;
}
