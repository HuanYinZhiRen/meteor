package com.nilin.meteor.entity;


import java.io.Serializable;

public class Result implements Serializable {

    /** 错误返回值*/
    public final static int ERROR_CODE = 0;

    /** 正确返回值*/
    public final static int RIGHT_CODE = 200;

    /** Token错误返回值*/
    public final static int TOKEN_CODE = 401;

    /** 数据正确与否标识 */
    private int code = 200;

    /** 错误信息 */
    private String msg;

    /** 返回数据 */
    private Object data;


    public void setErrorMsg(String msg) {
        this.code = this.ERROR_CODE;
        this.msg = msg;
    }
    public void setUnauthorizedMsg(String msg) {
        this.code = this.TOKEN_CODE;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
