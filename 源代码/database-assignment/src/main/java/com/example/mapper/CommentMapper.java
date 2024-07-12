package com.example.mapper;

import com.example.entity.Comment;
import com.example.entity.Like2Comment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.relational.core.sql.In;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper {
    @Select("select * from comment where replyPostId = #{pid}")
    List<Comment> getCommentByPid(Integer pid);

    @Insert("insert into comment(userId, replyPostId, content, time) " +
            "VALUES (#{uid}, #{pid}, #{content}, NOW())")
    void insertFirstComment(String content, Integer uid, Integer pid);

    @Insert("insert into comment(userId, replyPostId, content, time, replyCommentId, showId) " +
            "VALUES (#{uid}, #{pid}, #{content}, NOW(), #{cid}, #{showId})")
    void insertSecondComment(String content, Integer uid, Integer pid, Integer cid, Integer showId);

    @Select("select MAX(id) from comment")
    Integer getCurrentId();

    @Select("select replyPostId from comment where id = #{replyCommentId}")
    Integer getPostIdByReplyCommentId(Integer replyCommentId);

    @Select("select l2c.commentId from like2comment l2c where l2c.userId = #{uid} and l2c.commentId in (select id from comment where replyPostId = #{pid})")
    List<Integer> getLikeList(Integer uid, Integer pid);

    @Select("select * from like2comment where userId = #{uid} and commentId = #{cid}")
    List<Like2Comment> getCommentLiked(Integer uid, Integer cid);

    @Delete("delete from like2comment where userId = #{uid} and commentId = #{cid}")
    void delCommentLiked(Integer uid, Integer cid);

    @Insert("insert into like2comment(commentId, userId) values(#{cid}, #{uid})")
    void insertCommentLiked(Integer uid, Integer cid);

    @Select("select userId from comment where id = #{replyCommentId}")
    Integer getUserId(Integer replyCommentId);

    @Select("select * from comment where id = #{replyCommentId}")
    List<Comment> getCommentByCid(Integer replyCommentId);

    List<Integer> getReplyLikes(@Param("commentIds") List<Integer> commentIds, @Param("uid") Integer uid);

    @Select("select CONCAT('/post&pid', c.replyPostId, '#anchor', c.id) route, 3 * likeNum + 5 * commentNum heat from comment c order by 3 * likeNum + 5 * commentNum desc limit 0, 20")
    List<Map<String, Object>> getCommentHeat();

    // 从一个临时表中select 避免了不能同时查询和更新同一张表的限制
    @Update("update comment set commentNum = (select sub.commentNum from (select commentNum from comment where id = #{replyCommentId}) as sub) + 1 where id = #{replyCommentId}")
    void updateReplyComment(Integer replyCommentId);
}
