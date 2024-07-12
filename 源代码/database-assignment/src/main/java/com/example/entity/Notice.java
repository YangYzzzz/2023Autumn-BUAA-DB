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
// 通告
public class Notice {
    private Integer id;
    private String type; // 通知类型
    private Date time; // 通知时间
    private Integer receiverId; // 接收者id
    private Integer senderId; // 发送者Id
    private String message; // 通知信息
    private Boolean read; // 是否已读
    private Boolean isDelete; // 假删除，用来追踪用户轨迹
    // 点赞和评论消息必要
    private Integer replyPostId; // 回复或点赞的帖子Id
    // 点赞和评论消息需要
    private Integer replyCommentId; // 回复或点赞的评论id 若直接回复的帖子则该条null

    // 仅评论消息需要
    private Integer senderCommentId; // 发送者的自己的评论id, 可以通过此得到senderid，不满足3nf

    // 管理员通知需要
    private Integer managerAnnouncementId; // 记录管理员通知的id

    // 灵魂匹配需要
    private Integer soulMatchId; // 记录对应的灵魂匹配事件id

    // 不在数据库内
    private String senderUsername;
    private String receiverUsername;
    private String description;
}
