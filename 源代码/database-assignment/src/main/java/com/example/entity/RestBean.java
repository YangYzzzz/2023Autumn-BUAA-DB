package com.example.entity;

import lombok.Data;

@Data
public class RestBean {
    private int status;
    private boolean success;
    private Object data;

    private RestBean(int status, boolean success, Object data) {
        this.status = status;
        this.success = success;
        this.data = data;
    }

    // 成功分为 有返回成功 和无需返回成功
    public static RestBean success() {
        return new RestBean(200, true, null);
    }

    public static RestBean success(Object data) {
        return new RestBean(200, true, data);
    }

    // 特殊情况用到
    public static RestBean success(int status, Object data) {
        return new RestBean(status, true, data);
    }
    // 失败需要错误码来标识 再分为需不需要返回失败信息
    public static RestBean failure(int status) {
        return new RestBean(status, false, null);
    }

    public static RestBean failure(int status, Object data) {
        return new RestBean(status, false, data);
    }
}
