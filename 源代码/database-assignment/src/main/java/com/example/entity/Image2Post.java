package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 帖子和图片的映射
public class Image2Post {
    private Integer id;
    private Integer postId; // 帖子id
    private String imgUrl; // 照片url
}
