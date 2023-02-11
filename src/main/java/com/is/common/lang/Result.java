package com.is.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private String code; //200 正常
    private String msg;
    private Object data;

    public static Result succ(Object data) {
        Result r = new Result();
        r.setCode("200");
        r.setData(data);
        r.setMsg("Success");
        return r;
    }

    public static Result succ(String msg, Object data) {
        Result r = new Result();
        r.setCode("200");
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

    public static Result fail(String msg) {
        Result r = new Result();
        r.setCode("-1");
        r.setData(null);
        r.setMsg(msg);
        return r;
    }

    public static Result fail(String msg, Object data) {
        Result r = new Result();
        r.setCode("-1");
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

    public static Result fail(String code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

    public static Result fail(String code, String msg) {
        Result r = new Result();
        r.setCode(code);
        r.setData(null);
        r.setMsg(msg);
        return r;
    }
}