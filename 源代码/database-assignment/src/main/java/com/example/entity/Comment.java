package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 二级评论
public class Comment {
    private Integer id;
    private Integer userId; // 评论人Id
    private Date time; // 评论时间
    private Integer replyCommentId; // 回复的评论id 回复帖子默认-1
    private Integer replyPostId; // 回复的帖子id
    private String content; //回复内容
    private Integer showId; // 展示在一级评论下
    private Integer likeNum; // 点赞数
    private Integer commentNum;
}
