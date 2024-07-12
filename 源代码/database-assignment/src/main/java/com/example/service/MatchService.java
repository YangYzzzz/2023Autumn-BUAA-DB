package com.example.service;

import com.example.entity.Label;
import com.example.entity.RestBean;
import com.example.entity.SoulMatch;
import com.example.entity.User;
import com.example.mapper.FriendMapper;
import com.example.mapper.ImageBoardMapper;
import com.example.mapper.LabelMapper;
import com.example.mapper.MatchMapper;
import com.example.mapper.NoticeMapper;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class MatchService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private ImageBoardMapper imageBoardMapper;
    @Autowired
    private MatchMapper matchMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    /*
        返回匹配结果, 归根到底即一串uid，根据标签匹配
     */
    public Pair<List<User>, Integer> match(Integer uid, List<Integer> labelIds, Integer num) {
        User user = userMapper.getUserById(uid).get(0);
        String targetSex = null;
        switch (user.getSex()) {
            case "男": {
                targetSex = "女";
                break;
            }
            case "女": {
                targetSex = "男";
                break;
            }
            default:{
                System.out.println("兄弟你什么性别");
                targetSex = "男";
                break;
            }
        }
        // 得到候选者
        List<Integer> friends = friendMapper.getUserAllFriendId(uid);
        List<User> targetUsers = userMapper.getUsersBySex(friends, uid, targetSex);
        List<Label> labels = labelMapper.getLabels(); // 得到所有标签
        HashMap<Integer, Float> label2weight = new HashMap<>();
        for (Label label : labels) {
            label2weight.put(label.getId(), label.getWeight());
        }
        Set<Integer> set1 = new HashSet<>(labelIds);

        // 为候选者打分
        for (User targetUser : targetUsers) {
            List<Integer> targetLabelIds = labelMapper.getUserLabelList(targetUser.getId());
            // 度量两个集合的相似度
            float divide = 0, divided = 0;
            // 受杰卡德相似系数的启发(交集 / 并集)，我们提出了自己的相似度计算方法，我们(管理员)为每个标签赋予权重(0~1)，在此基础上计算(并集 * 各项权重 / 交集 * 各项权重)
            Set<Integer> set2 = new HashSet<>(targetLabelIds);
            Set<Integer> combine = new HashSet<>(); // 并集
            combine.addAll(set2);
            combine.addAll(set1);
            Set<Integer> intersection = new HashSet<>(); // 交集
            for (Integer integer : set1) {
                if (set2.contains(integer)) {
                    intersection.add(integer);
                }
            }

            for (Integer i : intersection) {
                divided += label2weight.get(i);
            }

            for (Integer i : combine) {
                divide += label2weight.get(i);
            }
            if (divide != 0) {
                targetUser.setScore(divided / divide);
            } else {
                targetUser.setScore(0);
            }
        }

        // 降序排序
        Collections.sort(targetUsers);

        num = targetUsers.size() > num ? num : targetUsers.size();
        targetUsers = targetUsers.subList(0, num);

        // 获得照片集
        for (int i = 0; i < num; i++) {
            targetUsers.get(i).setImageBoardUrls(imageBoardMapper.getImageBoardUrlsByUid(targetUsers.get(i).getId()));
        }
        matchMapper.addSoulMatch(uid, num);
        Integer matchId = matchMapper.getCurMatchId();
        return Pair.of(targetUsers, matchId);
    }

    public void sendFriendApplication(Integer myId, Integer userId, String message, Integer soulMatchId) {
        noticeMapper.addSoulMatchNotice(myId, userId, message, soulMatchId);
        Integer sendApplicationNum = matchMapper.getFriendApplicationNum(soulMatchId);
        matchMapper.updateFriendApplicationNum(sendApplicationNum, soulMatchId);
    }

    public void addFriend(Integer myId, Integer userId, Integer soulMatchId) {
        friendMapper.addFriendThroughSoulMatch(myId, userId, soulMatchId);
        noticeMapper.addFriendSuccessNotice(myId, userId, "对方同意了您的好友申请");
        Integer successNum = matchMapper.getSuccessNum(soulMatchId);
        matchMapper.updateSuccessNum(successNum, soulMatchId);
    }

    public Pair<Object, Integer> findPage(Integer pageNum, Integer pageSize, String username) {
        Integer limitStart = (pageNum - 1) * pageSize;
        Integer total = matchMapper.getTotal(username);
        List<Map<String, Object>> results = matchMapper.findPage(limitStart, pageSize, username);
        return Pair.of(results, total);
    }

    public List<Object> getStatisticInfo() {
        Integer soulMatchTotal = matchMapper.getTotal(null);
        Float avaSuccessRate = matchMapper.getAvaSuccessRate();
        Map<String, Object> maxUse = matchMapper.getMaxUse().get(0);
        Map<String, Object> flowerHeart = matchMapper.getFlowerHeartMost().get(0);
        List<Object> result = new ArrayList<>();
        result.add(soulMatchTotal);
        result.add(avaSuccessRate);
        result.add(maxUse);
        result.add(flowerHeart);
        return result;
    }
}
