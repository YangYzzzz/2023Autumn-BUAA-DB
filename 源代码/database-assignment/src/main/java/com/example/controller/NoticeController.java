package com.example.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.entity.Notice;
import com.example.entity.RestBean;
import com.example.entity.User;
import com.example.service.NoticeService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/getNewNum")
    public RestBean getNewAnnouncementNum(@RequestParam Integer uid) {
        return RestBean.success(noticeService.getNewAnnouncementNum(uid));
    }

    @GetMapping("/reply/getReplyList")
    public RestBean getReplyList(@RequestParam Integer uid) {
        return noticeService.getReplyList(uid);
    }

    @DeleteMapping("/reply/deleteReply")
    public RestBean deleteReply(@RequestParam Integer rid) {
        noticeService.deleteByAid(rid);
        return RestBean.success();
    }

    @DeleteMapping("/reply/deleteAll")
    public RestBean deleteReplyAll(@RequestParam Integer uid) {
        noticeService.deleteReplyByUid(uid);
        return RestBean.success();
    }

    @GetMapping("/sys/getSysList")
    public RestBean getSysList(@RequestParam Integer uid) {
        return RestBean.success(noticeService.getSysList(uid));
    }

    @DeleteMapping("/sys/deleteSys")
    public RestBean deleteSys(@RequestParam Integer sid) {
        noticeService.deleteByAid(sid);
        return RestBean.success();
    }

    @DeleteMapping("/sys/deleteAll")
    public RestBean deleteSysAll(@RequestParam Integer uid) {
        noticeService.deleteSysByUid(uid);
        return RestBean.success();
    }

    @GetMapping("/like/getLikeList")
    public RestBean getLikeList(@RequestParam Integer uid) {
        return noticeService.getLikeList(uid);
    }

    @DeleteMapping("/like/deleteLike")
    public RestBean deleteLike(@RequestParam Integer lid) {
        noticeService.deleteByAid(lid);
        return RestBean.success();
    }

    @DeleteMapping("/like/deleteAll")
    public RestBean deleteLikeAll(@RequestParam Integer uid) {
        noticeService.deleteLikeByUid(uid);
        return RestBean.success();
    }

    @GetMapping("/reply/getReplyLikes")
    public RestBean getReplyLikes(@RequestParam Integer uid) {
        // 返回的是senderComment的Id
        return RestBean.success(noticeService.getReplyLikes(uid));
    }

    @GetMapping("/friend/getFriendList")
    public RestBean getFriendList(@RequestParam Integer uid) {
        return RestBean.success(noticeService.getFriendList(uid));
    }

    @DeleteMapping("/friend/deleteAll")
    public RestBean deleteFriendAll(@RequestParam Integer uid) {
        noticeService.deleteFriendByUid(uid);
        return RestBean.success();
    }

    @DeleteMapping("/friend/delete")
    public RestBean deleteFriend(@RequestParam Integer nid) {
        noticeService.deleteByAid(nid);
        return RestBean.success();
    }

    @PostMapping("/friend/addFriend")
    public RestBean addFriendApplication(@RequestBody Map<String, Object> paramMap) {
        Integer myId = (Integer) paramMap.get("myId");
        Integer userId = (Integer) paramMap.get("userId");
        String message = (String) paramMap.get("message");
        return noticeService.addFriendApplication(myId, userId, message);
    }

    @GetMapping("/page")
    public RestBean findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(required = false) String senderUsername, @RequestParam(required = false) String receiverUsername, @RequestParam(required = false) String type) {
        return RestBean.success(noticeService.findPage(pageNum, pageSize, senderUsername, receiverUsername, type));
    }

    @GetMapping("/export") //导出接口 不能传requestbody
    public void export(HttpServletResponse response) throws Exception {
        List<Notice> notices = (List<Notice>) noticeService.findPage(1, 100000, null, null, null).getFirst();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //默认格式 强制输出标题
        writer.write(notices, true);

        //设置浏览器相应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户轨迹信息追踪", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    @GetMapping("/friendSuccess/getFriendSuccessList")
    public RestBean getFriendSuccessList(@RequestParam Integer uid) {
        return RestBean.success(noticeService.getFriendSuccessList(uid));
    }

    @DeleteMapping("/friendSuccess/deleteAll")
    public RestBean deleteFriendSuccessAll(@RequestParam Integer uid) {
        noticeService.deleteFriendSuccessByUid(uid);
        return RestBean.success();
    }

    @DeleteMapping("/friendSuccess/delete")
    public RestBean deleteFriendSuccess(@RequestParam Integer nid) {
        noticeService.deleteByAid(nid);
        return RestBean.success();
    }
}
