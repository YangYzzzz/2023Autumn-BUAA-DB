package com.example.entity.front;

import java.util.Date;

public class SystemNotice2Front {
    public Integer id;
    public Date time; // 回复时间
    public Integer uid; // 回复的用户id
    public String nickname;
    public String avatarUrl;
    public String message;

    public SystemNotice2Front() {
    }
}
