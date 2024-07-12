package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 好友
public class Friend {
    private Integer id;
    private Integer userId1;
    private Integer userId2;
    private Integer intimacy; // 双方亲密度
    private Integer soulMatchId; // 记录是否由灵魂匹配相加的好友
    private String time; // 加上好友的时间
}
