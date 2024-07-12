package com.example.mapper;

import com.example.entity.Friend;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface FriendMapper {

    @Select("select id from friend where userId1 = #{uid} and userId2 = #{friendId} or userId1 = #{friendId} and userId2 = #{uid}")
    List<Integer> isFriend(Integer uid, int friendId);

    @Insert("insert into friend(userId1, userId2, time) values(#{myId}, #{uid}, NOW())")
    void addFriend(Integer myId, Integer uid);

    @Select("select userId1 from friend where userId2 = #{uid} union select userId2 from friend where userId1 = #{uid}")
    List<Integer> getUserAllFriendId(Integer uid);
    @Insert("insert into friend(userId1, userId2, time, soulMatchId) values(#{myId}, #{userId}, NOW(), #{soulMatchId})")
    void addFriendThroughSoulMatch(Integer myId, Integer userId, Integer soulMatchId);

    @Select("select id, intimacy from friend where userId1 = #{uid} and userId2 = #{myId} or userId1 = #{myId} and userId2 = #{uid}")
    Friend getFriend(Integer myId, Integer uid);

    @Update("update friend set intimacy = #{intimacy} where id = #{id}")
    void updateIntimacy(Integer id, Integer intimacy);

    @Select("select intimacy from friend where intimacy >= #{min}")
    List<Integer> getIntimacy(Integer min);

    @Select("select * from friend where intimacy = (select MAX(intimacy) from friend)")
    List<Map<String, Object>> getMaxIntimacy();

    @Select("select u1.username source, u2.username target from friend left join user u1 on u1.id = friend.userId1 left join user u2 on u2.id = friend.userId2")
    List<Map<String, Object>> getSource();

}
