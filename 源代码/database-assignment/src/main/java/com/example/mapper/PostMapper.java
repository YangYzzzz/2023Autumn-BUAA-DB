package com.example.mapper;

import com.example.entity.Like2Post;
import com.example.entity.Post;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper {

    @Select("select * from post where userId = #{uid}")
    List<Post> getPostByUid(Integer uid);

    @Select("select * from like2post where userId = #{uid} and postId = #{pid}")
    List<Like2Post> getLikedByUidPid(Integer uid, Integer pid);

    @Delete("delete from like2post where userId = #{uid} and postId = #{pid};")
    void delLiked(Integer uid, Integer pid);

    @Insert("insert into like2post(postId, userId) VALUES(#{pid}, #{uid})")
    void insertLiked(Integer uid, Integer pid);

    @Select("select * from post where id = #{pid}")
    List<Post> getPostByPid(Integer pid);

    @Select("select imgUrl from image2post where postId = #{pid}")
    List<String> getImageUrls(Integer pid);

    @Select("select id from post where userId = #{osId} and id in (select postId from like2post where like2post.userId = #{uid})")
    List<Integer> getLikeList(Integer uid, Integer osId);

    @Select("select userId from post where id = #{pid}")
    Integer getUidByPid(Integer pid);

    @Select("select COUNT(*) from post")
    int getTotalPostNum();

    List<Map<String, Object>> findPage(int limitStart, Integer pageSize, String username, String content);

    @Insert("insert into post(time, content, type, userId) " +
            "values(NOW(), #{content}, #{type}, #{uid})")
    void addNewPost(Integer uid, Boolean type, String content);

    @Select("select MAX(id) from post")
    Integer getCurPostId();

    @Insert("insert into image2post(postId, imgUrl) values(#{pid}, #{imageUrl})")
    void addImage2Post(String imageUrl, Integer pid);

    @Select("select * from post")
    List<Post> getAllPost();

    @Delete("delete from post where id = #{pid}")
    void delPostByPid(Integer pid);

    @Delete("delete from post where id in(#{pid})")
    void delBatchPost(ArrayList<Integer> pid);

    // 所有公开的帖子
    @Select("select *, u.avatarUrl, u.nickname from post left join user u on u.id = post.userId where post.type = 0 order by post.time desc ")
    List<Post> getAllPublicPost();

    @Select("select id from post where type = 0 and id in (select postId from like2post where userId = #{uid})")
    List<Integer> getAllLikeList(Integer uid);

    @Select("select CONCAT('/post&pid', id) route, clickNum + 5 * likeNum + 10 * commentNum heat from post order by heat desc limit 0, 20")
    List<Map<String, Object>> getPostHeat();

    @Update("update post set clickNum = post.clickNum + 1 where id = #{pid}")
    void addClickNum(Integer pid);
}
