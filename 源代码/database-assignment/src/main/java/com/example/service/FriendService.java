package com.example.service;

import com.example.controller.FriendController;
import com.example.entity.Friend;
import com.example.entity.RestBean;
import com.example.mapper.FriendMapper;
import com.example.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    public Boolean isFriend(Integer myId, Integer uid) {
        return friendMapper.isFriend(myId, uid).size() == 1;
    }

    public RestBean addFriend(Integer myId, Integer uid) {
        boolean isFriend = friendMapper.isFriend(myId, uid).size() == 1;
        if (isFriend) {
            return RestBean.failure(100, "已经存在好友关系，添加好友失败");
        } else {
            friendMapper.addFriend(myId, uid);
            noticeMapper.addFriendSuccessNotice(myId, uid, "对方同意了您的好友申请");
            return RestBean.success("添加好友成功!");
        }
    }

    public void addIntimacy(Integer myId, Integer uid, String type) {
        if (isFriend(myId, uid)) {
            Friend friend = friendMapper.getFriend(myId, uid);
            Integer intimacy = friend.getIntimacy();
            Integer id = friend.getId();
            /*
                私聊每条+2，点赞对方的帖子 +5，点赞对方的评论 +3，评论对方的帖子 +10，评论对方的评论 +7
             */
            switch (type) {
                case "chat": {
                    intimacy += 2;
                    break;
                }
                case "like2post": {
                    intimacy += 5;
                    break;
                }
                case "like2comment": {
                    intimacy += 3;
                    break;
                }
                case "comment2post": {
                    intimacy += 10;
                    break;
                }
                case "comment2comment": {
                    intimacy += 7;
                    break;
                }
            }
            friendMapper.updateIntimacy(id, intimacy);
        }
    }

    public void subIntimacy(Integer myId, Integer uid, String type) {
        if (isFriend(myId, uid)) {
            Friend friend = friendMapper.getFriend(myId, uid);
            Integer intimacy = friend.getIntimacy();
            Integer id = friend.getId();
            /*
                禁止刷亲密度行为
             */
            switch (type) {
                case "chat": {
                    intimacy -= 2;
                    break;
                }
                case "like2post": {
                    intimacy -= 5;
                    break;
                }
                case "like2comment": {
                    intimacy -= 3;
                    break;
                }
                case "comment2post": {
                    intimacy -= 10;
                    break;
                }
                case "comment2comment": {
                    intimacy -= 7;
                    break;
                }
            }
            friendMapper.updateIntimacy(id, intimacy);
        }
    }
}
