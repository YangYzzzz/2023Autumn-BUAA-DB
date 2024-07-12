package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 点赞和帖子映射
public class Like2Post {
    private Integer id;
    private Integer postId; // 帖子Id
    private Integer userId; // 点赞的人id
}
