package com.example.entity.front;

import java.util.Date;
/*
    一级评论为回复帖子，二级评论及以后为回复人
    回复皆为评论
 */
public class ReplyNotice2Front {
    public Integer id;
    public Date time; // 回复时间
    public Integer uid; // 回复的用户id
    public String nickname;
    public String avatarUrl;
    public String originContent; // 被回复的内容
    public String originLink; // 被回复的链接
    public String replyContent;
    public Integer replyCommentId;
    public Integer showId;
    public ReplyNotice2Front() {
    }
}
