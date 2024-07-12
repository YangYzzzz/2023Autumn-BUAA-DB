package com.example.mapper;

import com.example.entity.ManagerAnnouncement;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ManagerAnnouncementMapper {
    @Insert("insert into managerannouncement(TIME, USERID, MESSAGE, RECEIVERNUM, TYPE) " +
            "values(NOW(), #{userId}, #{message}, #{receiverNum}, #{type})")
    void addManagerAnnouncement(Integer userId, String message, int receiverNum, String type);

    @Select("select * from managerannouncement where userId = #{userId};")
    List<ManagerAnnouncement> getManagerAnnouncement(Integer userId);

    @Select("select * from managerannouncement where userId = #{userId} limit #{limitStart}, #{pageSize}")
    List<ManagerAnnouncement> findPage(int limitStart, Integer pageSize, Integer userId);

    @Select("select MAX(id) from managerannouncement")
    Integer getCurManagerAnnouncementId();

    @Delete("delete from managerannouncement where id = #{id}")
    void delByMid(Integer id);

    @Select("select * from managerannouncement where userId = #{uid}")
    List<ManagerAnnouncement> getAllManagerAnnouncement(Integer uid);
}
