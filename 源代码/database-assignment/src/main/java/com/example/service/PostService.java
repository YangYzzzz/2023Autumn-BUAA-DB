package com.example.service;

import com.example.entity.Like2Post;
import com.example.mapper.NoticeMapper;
import com.example.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import com.example.entity.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private FriendService friendService;
    public List<Post> getPostByUid(Integer uid) {
        List<Post> posts = postMapper.getPostByUid(uid);
        for (Post post : posts) {
            post.setImageUrls(postMapper.getImageUrls(post.getId()));
        }
        return posts;
    }

    public Boolean getLiked(Integer uid, Integer pid) {
        List<Like2Post> like2Posts = postMapper.getLikedByUidPid(uid, pid);
        return like2Posts.size() == 1;
    }

    public String changeLiked(Integer uid, Integer pid) {
        Integer receiverUid = postMapper.getUidByPid(pid);
        System.out.println(uid);
        if (getLiked(uid, pid)) {
            // 已经点赞 需要取消 需要写个触发器
            postMapper.delLiked(uid, pid);
            noticeMapper.deleteLikeByPid(uid, pid); // 撤销信息
            friendService.subIntimacy(uid, receiverUid, "like2post");
            return "取消点赞成功";
        } else {
            postMapper.insertLiked(uid, pid);
            // 添加信息
            noticeMapper.insertNewAnnouncement("like", receiverUid, uid, pid, null, "您的帖子收到了一条点赞，请快看看吧!", null, null);
            friendService.addIntimacy(uid, receiverUid, "like2post");
            return "点赞成功";
        }
    }

    public Post getPostByPid(Integer pid) {
        postMapper.addClickNum(pid);
        Post post = postMapper.getPostByPid(pid).get(0);
        List<String> imageUrls = postMapper.getImageUrls(pid);
        post.setImageUrls(imageUrls);
        return post;
    }

    // 返回已经点赞的帖子列表
    public List<Integer> getLikeList(Integer uid, Integer osId) {
        return postMapper.getLikeList(uid, osId);
    }

    public Pair<Object, Integer> findPage(Integer pageNum, Integer pageSize, String username, String title, String content) {
        int limitStart = pageSize * (pageNum - 1);
        int total = postMapper.getTotalPostNum();
        List<Map<String, Object>> posts = postMapper.findPage(limitStart, pageSize, username, content);
        return Pair.of(posts, total);
    }

    public void addNewPost(Integer uid, Boolean type, String content, List<String> imageLists) {
        postMapper.addNewPost(uid, type, content);
        Integer curPid = postMapper.getCurPostId();
        for (String imageUrl : imageLists) {
            postMapper.addImage2Post(imageUrl, curPid);
        }
    }

    public List<Post> getAllPost() {
        return postMapper.getAllPost();
    }

    public void delPost(Integer pid) {
        postMapper.delPostByPid(pid);
    }

    public void delBatchPost(ArrayList<Integer> pid) {
        postMapper.delBatchPost(pid);
    }

    public List<Post> getAllPublicPost() {
        List<Post> posts = postMapper.getAllPublicPost();
        for (Post post : posts) {
            post.setImageUrls(postMapper.getImageUrls(post.getId()));
        }
        return posts;
    }

    public List<Integer> getAllLikeList(Integer uid) {
        return postMapper.getAllLikeList(uid);
    }
}
