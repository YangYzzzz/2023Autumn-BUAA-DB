package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 聊天信息
public class Chat {
    private Integer id;
    private String time; // 消息日期
    private Integer senderId; // 发送方Id
    private Integer receiverId; // 接受方Id
    private String content; // 内容
    private Boolean read; //是否已读
}
