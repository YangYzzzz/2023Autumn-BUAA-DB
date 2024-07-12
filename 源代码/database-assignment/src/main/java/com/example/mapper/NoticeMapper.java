package com.example.mapper;

import com.example.entity.Notice;
import com.example.entity.front.SystemNotice2Front;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {

    @Insert("insert into notice(type, time, receiverId, senderId, replyPostId, replyCommentId, message, senderCommentId, managerAnnouncementId) values " +
            "(#{type}, NOW(), #{receiverId}, #{senderId}, #{replyPostId}, #{replyCommentId}, #{message}, #{senderCommentId}, #{managerAnnouncementId})")
    void insertNewAnnouncement(String type, Integer receiverId, Integer senderId, Integer replyPostId, Integer replyCommentId, String message, Integer senderCommentId, Integer managerAnnouncementId);

    @Select("select type, COUNT(*) from notice where receiverId = #{uid} and `read` = 0 and isDelete = 0 group by type")
    List<Map<String, Object>> getNewAnnouncementNum(Integer uid);

    @Update("update notice set `read` = 1 where receiverId = #{uid} and type = #{type}")
    void setRead(Integer uid, String type);

    @Select("select * from notice where receiverId = #{uid} and type = 'reply' and isDelete = 0")
    List<Notice> getReplyByUid(Integer uid);

    @Select("select a.id, a.time, a.message, u.id uid, u.nickname, u.avatarUrl from notice a left join user u on a.senderId = u.id where receiverId = #{uid} and type = 'system' and isDelete = 0")
    List<SystemNotice2Front> getSystemByUid(Integer uid);

    @Update("update notice set isDelete = 1 where id = #{id}")
    void deleteByNid(Integer id);

    @Update("update notice set isDelete = 1 where receiverId = #{uid} and type = #{type}")
    void deleteByUid(Integer uid, String type);

    @Select("select * from notice where receiverId = #{uid} and type = 'like' and isDelete = 0")
    List<Notice> getLikeByUid(Integer uid);

    // 当发送者并非发送了一条评论时，代表着发送者发送的是like
    @Update("update notice set isDelete = 1 where senderId = #{uid} and replyCommentId = #{cid} and type = 'like'")
    void deleteLikeByCid(Integer uid, Integer cid);

    @Update("update notice set isDelete = 1 where senderId = #{uid} and replyPostId = #{pid} and type = 'like'")
    void deleteLikeByPid(Integer uid, Integer pid);

    @Select("select a.id, a.senderId uid, a.message, u.avatarUrl, u.nickname, a.time, COALESCE(a.soulMatchId, '') soulMatchId from notice a left join user u on u.id = a.senderId " +
            "where a.type = 'friend' and isDelete = 0 and a.receiverId = #{uid}")
    List<Map<String, Object>> getFriendList(Integer uid);

    @Select("select * from notice where senderId = #{myId} and receiverId = #{userId} and type = 'friend'")
    List<Notice> hasSameFriendNotice(Integer myId, Integer userId);

    @Update("update notice set isDelete = 1 where senderId = #{myId} and receiverId = #{userId} and type = 'friend'")
    void deleteFriendNotice(Integer myId, Integer userId, String message);

    @Insert("insert into notice(type, time, receiverId, senderId, message) values('friend', NOW(), #{userId}, #{myId}, #{message})")
    void addFriendNotice(Integer myId, Integer userId, String message);

    @Update("update notice set isDelete = 1 where type = 'system' and managerAnnouncementId = #{mid}")
    void deleteByMid(Integer mid);

    List<Notice> findPage(Integer limitStart, Integer pageSize, String senderUsername, String receiverUsername, String type);

    Integer getTotalNotice(String senderUsername, String receiverUsername, String type);

    @Insert("insert into notice(type, time, receiverId, senderId, message, soulMatchId) values('friend', NOW(), #{userId}, #{myId}, #{message}, #{soulMatchId})")
    void addSoulMatchNotice(Integer myId, Integer userId, String message, Integer soulMatchId);


    @Insert("insert into notice(type, time, receiverId, senderId, message) values('success', NOW(), #{userId}, #{myId}, #{message})")
    void addFriendSuccessNotice(Integer myId, Integer userId, String message);


    @Select("select n.id, n.senderId uid, n.message, u.avatarUrl, u.nickname, n.time from notice n left join user u on u.id = n.senderId where n.type = 'success' and isDelete = 0")
    List<Map<String, Object>> getFriendSuccessListByUid(Integer uid);
}
