package com.example.service;

import com.example.mapper.AccessMapper;
import com.example.mapper.CommentMapper;
import com.example.mapper.FriendMapper;
import com.example.mapper.PostMapper;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.ObjectName;
import java.lang.annotation.Target;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChartService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private AccessMapper accessMapper;
    /*
        统计用户每个月的注册情况，返回
        [
            {
                "month": 12,
                "num": 20
            },
            {
                ....
            }
        ]
     */
    public List<Map<String, Object>> getUserDataByMonth() {
        return userMapper.getUserNumByMonth();
    }

    public List<Map<String, Object>> getBoyGirl() {
        // 一次查询得到两个信息
        return userMapper.getBoyGirl();
    }

    public List<Map<String, Object>> getConstellation() {
        List<String> birthdays = userMapper.getBirthdays();
        Map<String, Integer> resultMap = new HashMap<>();
        for (String birthday : birthdays) {
            String constellation = UserService.getConstellation(birthday.substring(0, 10));
            if (resultMap.containsKey(constellation)) {
                resultMap.put(constellation, resultMap.get(constellation) + 1);
            } else {
                resultMap.put(constellation, 1);
            }
        }
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", entry.getValue());
            map.put("name", entry.getKey());
            resultList.add(map);
        }
        return resultList;
    }

    // 同理需要name和value
    public List<Map<String, Object>> getAddress() {
        return userMapper.getAddress();
    }


    /*
        {
            "xAxis": ["/post&pid3"], // 直接id就可以
            "data": [1, 2, 3]
         }
     */
    public Map<String, List<Object>> getPostHeat() {
        List<Map<String, Object>> result = postMapper.getPostHeat();
        Map<String, List<Object>> resultMap = new HashMap<>();
        resultMap.put("xAxis", new ArrayList<>());
        resultMap.put("data", new ArrayList<>());
        for (Map<String, Object> map : result) {
            resultMap.get("xAxis").add(map.get("route"));
            resultMap.get("data").add(map.get("heat"));
        }
        return resultMap;
    }

    public Map<String, List<Object>> getCommentHeat() {
        List<Map<String, Object>> result = commentMapper.getCommentHeat();
        Map<String, List<Object>> resultMap = new HashMap<>();
        resultMap.put("xAxis", new ArrayList<>());
        resultMap.put("data", new ArrayList<>());
        for (Map<String, Object> map : result) {
            resultMap.get("xAxis").add(map.get("route"));
            resultMap.get("data").add(map.get("heat"));
        }
        return resultMap;
    }

    /*
        统计每个人的好友个数data，max，ava
     */
    public Map<String, Object> getSocialFriend(Integer min) {
        List<Integer> friendsCnt = userMapper.getTotalFriendNum(min);
        int max = 0, total = 0;
        for (Integer cnt : friendsCnt) {
            total += cnt;
            if (max < cnt) {
                max = cnt;
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", friendsCnt);
        resultMap.put("max", max);
        resultMap.put("ave", (float)total / (float) friendsCnt.size());
        return resultMap;
    }

    public Map<String, Object> getSocialIntimacy(Integer min) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Integer> intimacy = friendMapper.getIntimacy(min);
        Map<String, Object> maxInfo = friendMapper.getMaxIntimacy().get(0); // 只取一个最大值
        resultMap.put("data", intimacy);
        resultMap.put("max", maxInfo.get("intimacy"));
        resultMap.put("username1", userMapper.getUserById((Integer) maxInfo.get("userId1")).get(0).getUsername());
        resultMap.put("username2", userMapper.getUserById((Integer) maxInfo.get("userId2")).get(0).getUsername());
        return resultMap;
    }

    public List<HashMap<String, Object>> getActiveUserByDay(Integer day) {
        // 统计每天的活跃用户流量
        return accessMapper.getActiveUserByDay(day);
    }

    public List<HashMap<String, Object>> getAccessByDay(Integer day) {
        return accessMapper.getAccessByDay(day);
    }

    public List<HashMap<String, Object>> getWebPageAccess(Integer day) {
        return accessMapper.getWebPageAccess(day);
    }

    public Map<String, List<String>> getSocialGraph() {
        // 社交图的节点和边的关系
        // 需要一个NameList “节点1”，“节点2”,两个边关系2sourceList和targetList 一共三个List
        Map<String, List<String>> resultMap = new HashMap<>();
        List<String> nameList = userMapper.getAllHaveFriendUser();
        List<Map<String, Object>> tmp = friendMapper.getSource();
        List<String> source = new ArrayList<>();
        List<String> target = new ArrayList<>();
        for (Map<String, Object> st : tmp) {
            source.add((String) st.get("source"));
            target.add((String) st.get("target"));
        }
        resultMap.put("name", nameList);
        resultMap.put("source", source);
        resultMap.put("target", target);
        return resultMap;
    }
}
