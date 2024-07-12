package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chart")
public class ChartController {
    @Autowired
    private ChartService chartService;

    @GetMapping("/getUserDataByMonth")
    public RestBean getUserDataByMonth() {
        return RestBean.success(chartService.getUserDataByMonth());
    }

    @GetMapping("/getBoyGirl")
    public RestBean getBoyGirl() {
        return RestBean.success(chartService.getBoyGirl());
    }

    @GetMapping("/getConstellation")
    public RestBean getConstellation() {
        return RestBean.success(chartService.getConstellation());
    }

    @GetMapping("/getAddress")
    public RestBean getAddress() {
        return RestBean.success(chartService.getAddress());
    }

    @GetMapping("/getPostHeat")
    public RestBean getPostHeat() {
        return RestBean.success(chartService.getPostHeat());
    }

    @GetMapping("/getCommentHeat")
    public RestBean getCommentHeat() {
        return RestBean.success(chartService.getCommentHeat());
    }

    @GetMapping("/getSocialFriend")
    public RestBean getSocialFriend(@RequestParam Integer min) {
        return RestBean.success(chartService.getSocialFriend(min));
    }

    @GetMapping("/getSocialIntimacy")
    public RestBean getSocialIntimacy(@RequestParam Integer min) {
        return RestBean.success(chartService.getSocialIntimacy(min));
    }

    @GetMapping("/getActiveUserDataByDay")
    public RestBean getActiveUserByDay(@RequestParam Integer day) {
        return RestBean.success(chartService.getActiveUserByDay(day));
    }

    @GetMapping("/getWebAccessByDay")
    public RestBean getAccessByDay(@RequestParam Integer day) {
        return RestBean.success(chartService.getAccessByDay(day));
    }
    @GetMapping("/getWebPageAccess")
    public RestBean getWebPageAccess(@RequestParam Integer day) {
        return RestBean.success(chartService.getWebPageAccess(day));
    }

    @GetMapping("/getSocialGraph")
    public RestBean getSocialGraph() {
        return RestBean.success(chartService.getSocialGraph());
    }
}
