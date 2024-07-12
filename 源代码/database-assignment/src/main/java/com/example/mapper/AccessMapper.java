package com.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface AccessMapper {
    @Insert("insert into access(userId, time, pageName) values(#{userId}, NOW(), #{pageName})")
    void addAccess(Integer userId, String pageName);

    @Select("SELECT DATE(time) as date, COUNT(DISTINCT userid) as count FROM access WHERE time >= CURDATE() - INTERVAL #{day} DAY GROUP BY date ORDER BY date")
    List<HashMap<String, Object>> getActiveUserByDay(Integer day);

    @Select("select DATE(time) as date, count(*) as count from access where time >= CURDATE() - interval #{day} day group by date order by date")
    List<HashMap<String, Object>> getAccessByDay(Integer day);

    @Select("select pageName as name, COUNT(*) as count from access where time > CURDATE() - interval #{day} DAY group by name")
    List<HashMap<String, Object>> getWebPageAccess(Integer day);
}
