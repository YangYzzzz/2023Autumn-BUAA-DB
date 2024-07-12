package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 照片墙
public class ImageBoard {
    private Integer id;
    private Integer userId;
    private String imgUrl;
    private String time; // 上传日期
}
