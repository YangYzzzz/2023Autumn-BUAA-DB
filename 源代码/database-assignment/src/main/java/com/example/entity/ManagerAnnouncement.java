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
public class ManagerAnnouncement {
    private Integer id;
    private Date time;
    private Integer userId; // 管理员Id
    private String message;
    private Integer receiverNum;
    private String type;
}
