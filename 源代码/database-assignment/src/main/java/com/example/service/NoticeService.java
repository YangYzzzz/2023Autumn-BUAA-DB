package com.example.service;

import com.example.entity.Notice;
import com.example.entity.Comment;
import com.example.entity.Post;
import com.example.entity.RestBean;
import com.example.entity.User;
import com.example.entity.front.LikeNotice2Front;
import com.example.entity.front.ReplyNotice2Front;
import com.example.entity.front.SystemNotice2Front;
import com.example.mapper.NoticeMapper;
import com.example.mapper.CommentMapper;
import com.example.mapper.FriendMapper;
import com.example.mapper.PostMapper;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private FriendMapper friendMapper;
    public HashMap<String, Integer> getNewAnnouncementNum(Integer uid) {
        HashMap<String, Integer> reMap = new HashMap<>();
        List<Map<String, Object>> params = noticeMapper.getNewAnnouncementNum(uid);
        int total = 0;
        for (Map<String, Object> map : params) {
            int num = 0;
            for (Map.Entry<String, Object> entry :map.entrySet()) {
                if (entry.getValue() instanceof Long) {
                    total += ((Long) entry.getValue()).intValue();
                    num = ((Long) entry.getValue()).intValue();
                }
                if (entry.getValue() instanceof String) {
                    reMap.put((String) entry.getValue(), num);
                }
            }
        }
        reMap.put("total", total);
        return reMap;
    }

    public RestBean getReplyList(Integer uid) {
        List<ReplyNotice2Front> replyNotice2Fronts = new ArrayList<>();
        noticeMapper.setRead(uid, "reply");
        List<Notice> notices = noticeMapper.getReplyByUid(uid);
        for (Notice notice : notices) {
            ReplyNotice2Front replyNotice2Front = new ReplyNotice2Front();
            List<User> users = userMapper.getUserById(notice.getSenderId());
            if (users.size() != 1) {
                return RestBean.failure(100, "用户不存在");
            } else {
                User senderUser = users.get(0);
                replyNotice2Front.avatarUrl = senderUser.getAvatarUrl();
                replyNotice2Front.uid = senderUser.getId();
                replyNotice2Front.nickname = senderUser.getNickname();
                replyNotice2Front.id = notice.getId();
                replyNotice2Front.time = notice.getTime();
                if (notice.getReplyCommentId() == null) {
                    // 说明是一级评论，回复的是帖子
                    replyNotice2Front.originLink = "/post&pid" + notice.getReplyPostId();
                    List<Post> posts = postMapper.getPostByPid(notice.getReplyPostId());
                    if (posts.size() != 1) {
                        replyNotice2Front.originContent = "原始帖子不存在";
                    } else {
                        replyNotice2Front.originContent = posts.get(0).getContent(); // 帖子可能被刪除，注意錯誤處理
                    }
                } else {
                    // 说明是二级评论，回复的是评论
                    replyNotice2Front.originLink = "/post&pid" + notice.getReplyPostId() + "#anchor" + notice.getReplyCommentId();
                    List<Comment> comments = commentMapper.getCommentByCid(notice.getReplyCommentId());
                    if (comments.size() != 1) {
                        replyNotice2Front.originContent = "原始评论不存在";
                    } else {
                        replyNotice2Front.originContent = comments.get(0).getContent();
                    }
                }

                List<Comment> comments = commentMapper.getCommentByCid(notice.getSenderCommentId());
                if (comments.size() != 1) {
                    replyNotice2Front.replyContent = "回复评论不存在";
                } else {
                    replyNotice2Front.replyContent = comments.get(0).getContent();
                    replyNotice2Front.replyCommentId = comments.get(0).getId();
                    replyNotice2Front.showId = comments.get(0).getShowId();
                }
            }
            replyNotice2Fronts.add(replyNotice2Front);
        }

        return RestBean.success(replyNotice2Fronts);
    }

    public List<SystemNotice2Front> getSysList(Integer uid) {
        noticeMapper.setRead(uid, "system");
        return noticeMapper.getSystemByUid(uid);
    }

    public RestBean getLikeList(Integer uid) {
        noticeMapper.setRead(uid, "like");
        List<LikeNotice2Front> likeNotice2Fronts = new ArrayList<>();
        List<Notice> notices = noticeMapper.getLikeByUid(uid);
        for (Notice notice : notices) {
            LikeNotice2Front likeNotice2Front = new LikeNotice2Front();
            List<User> users = userMapper.getUserById(notice.getSenderId());
            if (users.size() != 1) {
                return RestBean.failure(100, "用户不存在");
            } else {
                User senderUser = users.get(0);
                likeNotice2Front.avatarUrl = senderUser.getAvatarUrl();
                likeNotice2Front.uid = senderUser.getId();
                likeNotice2Front.nickname = senderUser.getNickname();
                likeNotice2Front.id = notice.getId();
                likeNotice2Front.time = notice.getTime();
                if (notice.getReplyCommentId() == null) {
                    // 说明是一级评论，点赞的是帖子
                    likeNotice2Front.originLink = "/post&pid" + notice.getReplyPostId();
                    List<Post> posts = postMapper.getPostByPid(notice.getReplyPostId());
                    if (posts.size() != 1) {
                        likeNotice2Front.originContent = "原始帖子不存在";
                    } else {
                        likeNotice2Front.originContent = posts.get(0).getContent(); // 帖子可能被刪除，注意錯誤處理
                    }
                } else {
                    // 说明是二级评论，回复的是评论
                    likeNotice2Front.originLink = "/post&pid" + notice.getReplyPostId() + "#anchor" + notice.getReplyCommentId();
                    List<Comment> comments = commentMapper.getCommentByCid(notice.getReplyCommentId());
                    if (comments.size() != 1) {
                        likeNotice2Front.originContent = "原始评论不存在";
                    } else {
                        likeNotice2Front.originContent = comments.get(0).getContent();
                    }
                }
            }
            likeNotice2Fronts.add(likeNotice2Front);
        }
        return RestBean.success(likeNotice2Fronts);
    }

    public void deleteSysByUid(Integer uid) {
        noticeMapper.deleteByUid(uid, "system");
    }

    public void deleteByAid(Integer aid) {
        noticeMapper.deleteByNid(aid);
    }

    public void deleteLikeByUid(Integer uid) {
        noticeMapper.deleteByUid(uid, "like");
    }

    public void deleteReplyByUid(Integer uid) {
        noticeMapper.deleteByUid(uid, "reply");
    }

    public List<Integer> getReplyLikes(Integer uid) {
        List<Notice> notices = noticeMapper.getReplyByUid(uid); // 得到该用户收到的所有reply
        List<Integer> commentIds = new ArrayList<>();
        for (Notice notice : notices) {
            commentIds.add(notice.getSenderCommentId());
        }
        if (commentIds.isEmpty()) {
            return null;
        } else {
            System.out.println(commentIds);
            return commentMapper.getReplyLikes(commentIds, uid);
        }
        // 对于reply而言 一个消息id对应一条评论id
    }

    public List<Map<String, Object>> getFriendList(Integer uid) {
        noticeMapper.setRead(uid, "friend");
        List<Map<String, Object>> resultMap = noticeMapper.getFriendList(uid);
        for (Map<String, Object> result : resultMap) {
            int friendId = (int) result.get("uid");
            Boolean isFriend = friendMapper.isFriend(uid, friendId).size() == 1;
            result.put("isFriend", isFriend);
        }
        return resultMap;
    }

    public void deleteFriendByUid(Integer uid) {
        noticeMapper.deleteByUid(uid, "friend");
    }

    public RestBean addFriendApplication(Integer myId, Integer userId, String message) {
        boolean isFriend = friendMapper.isFriend(myId, userId).size() == 1;
        if (isFriend) {
            return RestBean.failure(100, "已经是好友了");
        } else {
            noticeMapper.deleteFriendNotice(myId, userId, message);
            System.out.println("删除成功");
            noticeMapper.addFriendNotice(myId, userId, message);
            return RestBean.success("发送好友申请成功");
        }
    }

    public Pair<Object, Integer> findPage(Integer pageNum, Integer pageSize, String senderUsername, String receiverUsername, String type) {
        int limitStart = (pageNum - 1) * pageSize;
        int total = noticeMapper.getTotalNotice(senderUsername, receiverUsername, type);
        List<Notice> notices = noticeMapper.findPage(limitStart, pageSize, senderUsername, receiverUsername, type); // 不管用户删不删，点赞撤销不撤销，只要做出行为便记录
        System.out.println(notices);
        for (Notice notice : notices) {
            switch (notice.getType()) {
                case "system": {
                    notice.setDescription("管理员向其发送通知，通知内容为: " + notice.getMessage());
                    break;
                }
                case "reply": {
                    if (notice.getReplyCommentId() == null) {
                        // 评论的帖子
                        Post post = postMapper.getPostByPid(notice.getReplyPostId()).get(0);
                        String content = post.getContent();
                        if (content.length() > 10) {
                            content = content.substring(0, 10) + "...";
                        }
                        Comment senderComment = commentMapper.getCommentByCid(notice.getSenderCommentId()).get(0);
                        String commentContent = senderComment.getContent();
                        if (commentContent.length() > 10) {
                            commentContent = commentContent.substring(0, 10) + "...";
                        }
                        notice.setDescription("为其发表的帖子(" + content + ")下发表了一条评论(" + commentContent + ")");
                    } else {
                        // 评论的评论
                        Post post = postMapper.getPostByPid(notice.getReplyPostId()).get(0);
                        String content = post.getContent();
                        if (content.length() > 10) {
                            content = content.substring(0, 10) + "...";
                        }
                        String postOwner = userMapper.getUserById(post.getUserId()).get(0).getUsername();

                        Comment comment = commentMapper.getCommentByCid(notice.getReplyCommentId()).get(0);
                        String commentContent = comment.getContent();
                        if (commentContent.length() > 10) {
                            commentContent = commentContent.substring(0, 10) + "...";
                        }

                        Comment senderComment = commentMapper.getCommentByCid(notice.getSenderCommentId()).get(0);
                        String senderCommentContent = senderComment.getContent();
                        if (senderCommentContent.length() > 10) {
                            senderCommentContent = senderCommentContent.substring(0, 10) + "...";
                        }
                        notice.setDescription("为" + postOwner + "发表的帖子(" + content + ")内其发表的评论(" + commentContent + ")回复了一条评论(" + senderCommentContent + ")");
                    }
                    break;
                }
                case "like": {
                    if (notice.getReplyCommentId() == null) {
                        // 点赞的帖子
                        Post post = postMapper.getPostByPid(notice.getReplyPostId()).get(0);
                        String content = post.getContent();
                        if (content.length() > 10) {
                            content = content.substring(0, 10) + "...";
                        }
                        notice.setDescription("为其发表的帖子(" + content + ")点了一个赞");
                    } else {
                        // 点赞的评论
                        Post post = postMapper.getPostByPid(notice.getReplyPostId()).get(0);
                        String content = post.getContent();
                        if (content.length() > 10) {
                            content = content.substring(0, 10) + "...";
                        }
                        String postOwner = userMapper.getUserById(post.getUserId()).get(0).getUsername();

                        Comment comment = commentMapper.getCommentByCid(notice.getReplyCommentId()).get(0);
                        String commentContent = comment.getContent();
                        if (commentContent.length() > 10) {
                            commentContent = commentContent.substring(0, 10) + "...";
                        }
                        notice.setDescription("为在" + postOwner + "发表的帖子(" + content + ")下其发表的评论(" + commentContent + ")点了一个赞");
                    }
                    break;
                }
                case "friend": {
                    notice.setDescription("向其发送了一条好友申请，申请内容为: " + notice.getMessage());
                    break;
                }
                case "success": {
                    notice.setDescription("同意了其发出的好友申请，快来并肩开启一段奇妙冒险吧");
                    break;
                }
            }
        }
        return Pair.of(notices, total);
    }

    public void deleteFriendSuccessByUid(Integer uid) {
        noticeMapper.deleteByUid(uid, "success");
    }

    public List<Map<String, Object>> getFriendSuccessList(Integer uid) {
        noticeMapper.setRead(uid, "success");
        return noticeMapper.getFriendSuccessListByUid(uid);
    }
}
