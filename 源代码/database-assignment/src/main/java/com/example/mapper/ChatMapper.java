package com.example.mapper;

import com.example.entity.Chat;
import com.example.entity.User;
import com.example.entity.front.Chat2Front;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChatMapper {
    @Select("select u.id, u.nickname, u.avatarUrl from user u where u.id in (select f.userId2 from friend f " +
            "where f.userId1 = #{uid}) or u.id in (select f.userId1 from friend f where f.userId2 = #{uid})")
    List<Map<String, Object>> getFriendsInfo1(Integer uid);

    @Select("select u.id, u.nickname, u.avatarUrl, f.intimacy from user u inner join friend f on f.userId1 = u.id and f.userId2 = #{uid} or f.userId2 = u.id and f.userId1 = #{uid}")
    List<Map<String, Object>> getFriendsInfo(Integer uid);

    @Select("select * from chat where senderId = #{uid} and receiverId = #{friendId} or senderId = #{friendId} and receiverId = #{uid} order by time")
    List<Chat> getChatRecord(Integer uid, Integer friendId);

    @Insert("insert into chat(time, senderId, receiverId, content, `read`) values(NOW(), #{senderId}, #{receiverId}, #{content}, 0)")
    void insertChatRecord(Integer senderId, Integer receiverId, String content);
    @Update("update chat set `read` = 1 where senderId = #{senderId} and receiverId = #{receiverId}")
    void readAll(Integer receiverId, Integer senderId);
}
