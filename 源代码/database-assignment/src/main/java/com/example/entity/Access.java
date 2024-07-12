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
/*
    访问统计
 */
public class Access {
    private Integer id;
    private Integer userId;
    private Date time;
    private String pageName;
}
