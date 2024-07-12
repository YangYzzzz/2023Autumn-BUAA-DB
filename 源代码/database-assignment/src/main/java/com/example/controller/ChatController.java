package com.example.controller;

import com.example.entity.Chat;
import com.example.entity.RestBean;
import com.example.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.channels.InterruptedByTimeoutException;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/friend")
    public RestBean getFriendsInfo(@RequestParam Integer uid) {
        return RestBean.success(chatService.getFriendsInfo(uid));
    }

    @GetMapping("/getChatRecord")
    public RestBean getChatRecord(@RequestParam Integer uid, @RequestParam Integer friendId) {
//        System.out.println(uid + "  " + friendId);
        return RestBean.success(chatService.getChatRecord(uid, friendId));
    }

    @PostMapping("/sendChatRecord")
    public RestBean sendChatRecord(@RequestBody Chat chat) {
        chatService.sendChatRecord(chat.getSenderId(), chat.getReceiverId(), chat.getContent());
        return RestBean.success("发送消息成功");
    }

    @PostMapping("/readAll")
    public RestBean readAll(@RequestParam Integer senderId, @RequestParam Integer receiverId) {
        chatService.readAll(senderId, receiverId);
        return RestBean.success();
    }
}
