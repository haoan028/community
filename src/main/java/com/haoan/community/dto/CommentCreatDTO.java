package com.haoan.community.dto;

import lombok.Data;

@Data
public class CommentCreatDTO {
    private String content;
    private Integer type;
    private Long parentId;
}
