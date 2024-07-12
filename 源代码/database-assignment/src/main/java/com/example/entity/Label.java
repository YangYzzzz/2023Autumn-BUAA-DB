package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Array;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 标签墙
public class Label {
    private Integer id;
    private String type;
    private String name;
    private Float weight;

    // 数据库不存在
    private List<Label> children;
    private Integer usedTime;
}
