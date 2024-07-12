package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 帖子信息
public class Post {
    private Integer id;
    private Integer userId; // 发帖人Id
    private String time; // 发帖时间
    private String content; // 内容 图片如何处理?
    private Boolean type; // true个人，false所有人
    private Integer likeNum; // 点赞数
    private Integer clickNum; // 点击量
    private Integer commentNum; // 评论数
    private Boolean top; // 个人空间中是否置顶


    // 不在数据库中
    private String nickname;
    private String avatarUrl;
    private List<String> imageUrls;
}
