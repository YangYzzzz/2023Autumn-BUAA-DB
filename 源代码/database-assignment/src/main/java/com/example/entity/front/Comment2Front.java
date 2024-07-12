package com.example.entity.front;

import java.util.Date;

public class Comment2Front {
    public Integer id;
    public Integer parentId;
    public Integer uid;
    public String address;
    public String content;
    public Integer likes;
    public Date createTime;
    public User2Front user;
    public Reply reply;

    public Comment2Front(Integer firstCommentFlag) {
        user = new User2Front();
        reply = new Reply();
    }

    public Comment2Front() {
        user = new User2Front();
    }
}
