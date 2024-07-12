package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 情侣空间
public class CoupleSpace {
    private Integer id;
    private Integer boyId; // 男方id
    private Integer girlId; // 女方id
    private String boyDescription; // 男方爱情宣言
    private String girlDescription; // 女方爱情宣言
    private String backgroundUrl; // 背景url
    private String time; // 相恋时间
}
