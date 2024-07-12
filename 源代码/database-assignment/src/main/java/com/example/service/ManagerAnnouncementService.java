package com.example.service;

import com.example.entity.ManagerAnnouncement;
import com.example.entity.User;
import com.example.mapper.NoticeMapper;
import com.example.mapper.ManagerAnnouncementMapper;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerAnnouncementService {
    @Autowired
    private ManagerAnnouncementMapper managerAnnouncementMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    public void sendManagerAnnouncement(Integer userId, String message, List<Integer> receivers) {
        // 只发给普通用户
        int normalUserNum = userMapper.getAllNormalUser().size();
        if (receivers.size() == normalUserNum) {
            managerAnnouncementMapper.addManagerAnnouncement(userId, message, receivers.size(), "所有人");
        } else {
            managerAnnouncementMapper.addManagerAnnouncement(userId, message, receivers.size(), "部分人");
        }
        int curNid = managerAnnouncementMapper.getCurManagerAnnouncementId();
        for (Integer receiverId : receivers) {
            noticeMapper.insertNewAnnouncement("system", receiverId, userId, null, null, message, null, curNid);
        }
    }

    public List<ManagerAnnouncement> getManagerAnnouncement(Integer userId) {
        return managerAnnouncementMapper.getManagerAnnouncement(userId);
    }

    public Pair<Object, Integer> page(Integer pageNum, Integer pageSize, Integer userId) {
        int limitStart = pageSize * (pageNum - 1);
        int total = managerAnnouncementMapper.getManagerAnnouncement(userId).size();
        Object list = managerAnnouncementMapper.findPage(limitStart, pageSize, userId);
        return Pair.of(list, total);
    }

    public void deleteManagerAnnouncement(Integer id) {
        managerAnnouncementMapper.delByMid(id); // 撤回通告
        noticeMapper.deleteByMid(id); // 撤回所有发送出去的通告
    }

    public List<ManagerAnnouncement> getAllManagerAnnouncement(Integer uid) {
        return managerAnnouncementMapper.getAllManagerAnnouncement(uid);
    }
}
