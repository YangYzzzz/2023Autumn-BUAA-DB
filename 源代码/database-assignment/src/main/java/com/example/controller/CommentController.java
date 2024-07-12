package com.example.controller;

import com.example.entity.Comment;
import com.example.entity.RestBean;
import com.example.service.CommentService;
import org.apache.ibatis.plugin.Intercepts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/getByPid")
    public RestBean getCommentByPid(@RequestParam Integer pid) {
        return RestBean.success(commentService.getCommentByPid(pid));
    }

    @PostMapping("/newFirstComment")
    public RestBean insertFirstComment(@RequestBody Comment comment) {
        return RestBean.success(commentService.insertFirstComment(comment.getContent(), comment.getUserId(), comment.getReplyPostId()));
    }

    @PostMapping("/newReply")
    public RestBean insertSecondComment(@RequestBody Comment comment) {
        return RestBean.success(commentService.insertSecondComment(comment.getContent(), comment.getUserId(), comment.getReplyCommentId(), comment.getShowId()));
    }

    @GetMapping("/likeList")
    public RestBean getLikeList(@RequestParam Integer uid, @RequestParam Integer pid) {
        return RestBean.success(commentService.getLikeList(uid, pid));
    }

    @GetMapping("/changeLiked")
    public RestBean changeLiked(@RequestParam Integer uid, @RequestParam Integer cid) {
        return RestBean.success(commentService.changeLiked(uid, cid));
    }
}
