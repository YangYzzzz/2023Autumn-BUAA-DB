package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/access")
public class AccessController {
    @Autowired
    private AccessService accessService;

    @GetMapping ("/addAccess")
    public RestBean addAccess(@RequestParam Integer userId, @RequestParam String pageName) {
        accessService.addAccess(userId, pageName);
        return RestBean.success();
    }
}
