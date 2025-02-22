package com.example.acm.common;

/**
 * @author Anxdada
 * @version 1.0
 * @date 2020-02-10 10:38
 */

public enum ResultCode {
    SUCCESS(0, "成功"),
    HTTP_PARA_ERROR(1, "系统繁忙"),
    NO_THIS_USER(2, "尚未注册"),
    PWD_ERROR(3, "密码错误"),
    NO_OAUTH(4, "尚未授权或者授权失败，请重新授权"),
    READ_MYSQL_FAILED(5, "获取信息失败"),
    SYSTEM_FAILED(6, "系统异常"),
    IS_THE_END(7, "已经是最后一组了"),
    PARAM_ERROR(8, "参数错误"),
    SESSION_OUT(13, "用户登出"),
    MSG_NOT_EXIST(14, "信息不存在"),
    ACCOUNT_TERMINATION(15, "用户被封停"),
    BUCKET_EXISTED(16, "当前bucket已存在"),
    FILE_EXISTED(17, "当前文件夹已存在"),
    OTHER_FAIL(100, "其他错误"),
    HAS_FINISHED(11, "已经完成"),
    ACCOUNT_NOTAUTH(16, "用户未认证"),
    ACCOUNT_NOTBIND(17, "用户未绑定"),
    SOURCE_EXIST(18, "素材已预处理"),
    SOURCE_INVALID(19, "素材不符合要求"),
    TASK_IS_CONDUCTING(20, "关联任务进行中"),
    USER_NOT_ADMIN(21, "用户不是管理员"),
    USER_EXEIST(22, "该用户已存在"),
    SQL_NULL_RECODE(23, "数据中没有满足条件的记录"),
    REGISTER_FAIL(24, "请勿重复报名");

    private final int status;  // code
    private String msg;

    private ResultCode(int code, String msg) {
        this.status = code;
        this.msg = msg;
    }

    public Integer getStatus() {
        return Integer.valueOf(this.status);
    }

    public String getMsg() {
        return this.msg;
    }

    public String toMessage() {
        return this.toString().toLowerCase().replaceAll("_", " ");
    }
}

/*
枚举类的注意点:
1、需要在枚举实例后面加上分号，然后再写构造函数等
2、枚举实例必须在前面
3、定义枚举的构造器方法带参,只能为private
 */
