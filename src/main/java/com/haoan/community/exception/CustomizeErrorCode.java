package com.haoan.community.exception;

public enum  CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"你找到问题不在了，要不要换个试试？"),
    TAEGET_PARAM_NOT_FOUND(2002,"未选中任何问题和评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后重试"),
    SYSTEM_ERROR(2004,"机房着火了，请择时再来！！"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你要回复的评论不存在了"),
    QUESTION_COMMENT_OT_FOUND(2007,"你要回复的问题不存在了"),
    CONTENT_IS_EMPTY(2008,"回复的内容不能为空"),
    READ_NOTIFICATION_FAIL(2009,"不要尝试访问其他的信息哦");

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private String message;
    private  Integer code;
    CustomizeErrorCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }
}
