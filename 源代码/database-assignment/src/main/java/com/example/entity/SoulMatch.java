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
public class SoulMatch {
    private Integer id;
    private Integer userId;
    private Integer matchNum;
    private Integer sendApplicationNum;
    private Integer successNum;
    private Date time;
}
