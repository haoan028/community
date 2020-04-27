package com.haoan.community.bean;

import lombok.Data;

import javax.swing.*;

@Data
public class Question {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmt_create;
    private Long gmt_modified;
    private String creator;
    private Integer comment_count;
    private Integer view_count;
    private Integer like_count;

}
