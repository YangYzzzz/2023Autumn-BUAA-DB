package com.example.controller;

import com.example.entity.Friend;
import com.example.entity.RestBean;
import com.example.service.FriendService;
import org.apache.ibatis.plugin.Intercepts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @GetMapping("/isFriend")
    public RestBean isFriend(@RequestParam Integer myId, @RequestParam Integer uid) {
        return RestBean.success(friendService.isFriend(myId, uid));
    }

    @PostMapping("/addFriend")
    public RestBean addFriend(@RequestParam Integer myId, @RequestParam Integer uid) {
        return friendService.addFriend(myId, uid);
    }
}
