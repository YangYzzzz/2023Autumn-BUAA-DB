package com.example.service;

import com.example.entity.Chat;
import com.example.entity.User;
import com.example.entity.front.Chat2Front;
import com.example.mapper.ChatMapper;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendService friendService;
    public List<Map<String, Object>> getFriendsInfo(Integer uid) {
        return chatMapper.getFriendsInfo(uid);
    }

    public Chat2Front getChatRecord(Integer uid, Integer friendId) {
        Chat2Front chat2Front = new Chat2Front();
        chat2Front.chats = chatMapper.getChatRecord(uid, friendId);
        chat2Front.selfAvatarUrl = userMapper.getUserById(uid).get(0).getAvatarUrl();
        chat2Front.friendAvatarUrl = userMapper.getUserById(friendId).get(0).getAvatarUrl();
        // 双方向的信息全部收集
        return chat2Front;
    }

    public void sendChatRecord(Integer senderId, Integer receiverId, String content) {
        chatMapper.insertChatRecord(senderId, receiverId, content);
        friendService.addIntimacy(senderId, receiverId, "chat");
    }

    public void readAll(Integer senderId, Integer receiverId) {
        // 应当两极反转
        chatMapper.readAll(senderId, receiverId);
    }
}
