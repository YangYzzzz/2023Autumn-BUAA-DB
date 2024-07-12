package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    private MatchService matchService;

    // 同意好友申请也算是一条通知
    @PostMapping("/getMatchResult")
    public RestBean getMatchResult(@RequestBody HashMap<String, Object> matchData) {
        Integer uid = (Integer) matchData.get("uid");
        Integer num = (Integer) matchData.get("num");
        List<Integer> labelIds = (List<Integer>) matchData.get("tagList");
        return RestBean.success(matchService.match(uid, labelIds, num));
    }

    @PostMapping("/sendFriendApplication")
    public RestBean sendFriendApplication(@RequestBody Map<String, Object> paramMap) {
        Integer myId = (Integer) paramMap.get("myId");
        Integer userId = (Integer) paramMap.get("userId");
        String message = (String) paramMap.get("message");
        Integer soulMatchId = (Integer) paramMap.get("soulMatchId");
        matchService.sendFriendApplication(myId, userId, message, soulMatchId); // 添加灵魂匹配的好友信息
        return RestBean.success();
    }

    @PostMapping("/addFriend")
    public RestBean addFriend(@RequestBody Map<String, Object> paramMap) {
        Integer myId = (Integer) paramMap.get("myId");
        Integer userId = (Integer) paramMap.get("userId");
        Integer soulMatchId = (Integer) paramMap.get("soulMatchId");
        matchService.addFriend(myId, userId, soulMatchId);
        return RestBean.success();
    }

    @GetMapping("/page")
    public RestBean findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(required = false) String username) {
        return RestBean.success(matchService.findPage(pageNum, pageSize, username));
    }

    @GetMapping("/getStatisticInfo")
    public RestBean getStatisticInfo() {
        return RestBean.success(matchService.getStatisticInfo());
    }
}
