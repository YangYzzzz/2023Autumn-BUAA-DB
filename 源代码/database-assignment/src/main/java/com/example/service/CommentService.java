package com.example.service;

import com.example.entity.Comment;
import com.example.entity.User;
import com.example.entity.front.Comment2Front;
import com.example.mapper.NoticeMapper;
import com.example.mapper.CommentMapper;
import com.example.mapper.PostMapper;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private FriendService friendService;
    public Comment2Front insertComment2FrontInfo(Comment2Front comment2Front, Comment comment) {
        comment2Front.id = comment.getId();
        comment2Front.address = "来自北京";
        comment2Front.createTime = comment.getTime();
        comment2Front.content = comment.getContent();
        comment2Front.likes = comment.getLikeNum();
        comment2Front.uid = comment.getUserId();
        comment2Front.parentId = comment.getReplyCommentId();
        User user = userMapper.getUserById(comment.getUserId()).get(0);
        comment2Front.user.username = user.getNickname();
        comment2Front.user.avatar = user.getAvatarUrl();
        comment2Front.user.homeLink = "/personal&uid" + user.getId();
        return comment2Front;
    }
    public List<Comment2Front> getCommentByPid(Integer pid) {
        List<Comment2Front> comment2FrontArrayList = new ArrayList<>();
        List<Comment> comments = commentMapper.getCommentByPid(pid);
        for (Comment comment : comments) {
            if (comment.getReplyCommentId() == null) {
                // 一级评论
                Comment2Front comment2Front = new Comment2Front(1);
                comment2FrontArrayList.add(insertComment2FrontInfo(comment2Front, comment));
            }
        }
        for (Comment comment : comments) {
            if (comment.getReplyCommentId() != null) {
                // 二级评论
                Comment2Front comment2Front = new Comment2Front();
                comment2Front = insertComment2FrontInfo(comment2Front, comment);
                for (Comment2Front comment2FrontFirst : comment2FrontArrayList) {
                    if (comment2FrontFirst.id.equals(comment.getShowId())) {
                        comment2FrontFirst.reply.total++;
                        comment2FrontFirst.reply.list.add(comment2Front);
                        break;
                    }
                }
            }
        }
        return comment2FrontArrayList;
    }

    public Integer insertFirstComment(String content, Integer uid, Integer pid) {
        commentMapper.insertFirstComment(content, uid, pid);
        Integer curCommentId = commentMapper.getCurrentId();
        Integer receiverId = postMapper.getUidByPid(pid);
        noticeMapper.insertNewAnnouncement("reply", receiverId, uid, pid, null, "您的xxx帖子收到了一条新的评论，快来看看吧!", curCommentId, null);
        friendService.addIntimacy(uid, receiverId, "comment2post");
        return curCommentId;
    }

    public Integer insertSecondComment(String content, Integer userId, Integer replyCommentId, Integer showId) {
        Integer receiverId = commentMapper.getUserId(replyCommentId); // 通过回复的评论id得到接收信息的userid
        Integer replyPostId = commentMapper.getPostIdByReplyCommentId(replyCommentId);
        commentMapper.insertSecondComment(content, userId, replyPostId, replyCommentId, showId);
        commentMapper.updateReplyComment(replyCommentId);
        int curCommentId = commentMapper.getCurrentId();
        noticeMapper.insertNewAnnouncement("reply", receiverId, userId, replyPostId, replyCommentId,  "您在xxx帖子下的评论收到了一条新的评论，点击查看", curCommentId, null);
        friendService.addIntimacy(userId, receiverId, "comment2comment");
        return curCommentId;
    }

    public List<Integer> getLikeList(Integer uid, Integer pid) {
        return commentMapper.getLikeList(uid, pid);
    }

    public String changeLiked(Integer uid, Integer cid) {
        Integer receiverUid = commentMapper.getUserId(cid);
        Integer postId = commentMapper.getPostIdByReplyCommentId(cid);
        if (commentMapper.getCommentLiked(uid, cid).size() == 1) {
            commentMapper.delCommentLiked(uid, cid);
            noticeMapper.deleteLikeByCid(uid, cid);
            friendService.subIntimacy(uid, receiverUid, "like2comment");
            return "评论取消点赞成功";
        } else {
            commentMapper.insertCommentLiked(uid, cid);
            noticeMapper.insertNewAnnouncement("like", receiverUid, uid, postId, cid, "您收到了一条新的点赞，请看看吧!", null, null);
            friendService.addIntimacy(uid, receiverUid, "like2comment");
            return "评论点赞成功";
        }
    }
}
