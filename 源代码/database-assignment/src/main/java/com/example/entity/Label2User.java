package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.sql.In;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 用户和标签映射
public class Label2User {
    private Integer id;
    private Integer userId;
    private Integer labelId;
}
