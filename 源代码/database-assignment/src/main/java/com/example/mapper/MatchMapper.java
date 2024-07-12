package com.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface MatchMapper {

    @Insert("insert into soulmatch(USERID, MATCHNUM, TIME) values(#{uid}, #{num}, NOW())")
    void addSoulMatch(Integer uid, Integer num);

    @Select("select MAX(id) from soulmatch")
    Integer getCurMatchId();

    @Select("select sendApplicationNum from soulmatch where id = #{soulMatchId}")
    Integer getFriendApplicationNum(Integer soulMatchId);

    // 手动触发器
    @Update("update soulmatch set sendApplicationNum = #{sendApplicationNum} + 1 where id = #{soulMatchId}")
    void updateFriendApplicationNum(Integer sendApplicationNum, Integer soulMatchId);

    @Select("select successNum from soulmatch where id = #{soulMatchId}")
    Integer getSuccessNum(Integer soulMatchId);

    @Update("update soulmatch set successNum = #{successNum} + 1 where id = #{soulMatchId}")
    void updateSuccessNum(Integer successNum, Integer soulMatchId);

    Integer getTotal(String username);

    List<Map<String, Object>> findPage(Integer limitStart, Integer pageSize, String username);

    @Select("select AVG(successNum / sendApplicationNum) from soulmatch where sendApplicationNum != 0")
    Float getAvaSuccessRate();

    @Select("select u.username, useTime from (select userId, COUNT(*) useTime from soulmatch group by userId) as sub left join user u on u.id = sub.userId where useTime = (select MAX(useTime) from (select userId, COUNT(*) useTime from soulmatch group by userId) as sub2)")
    List<Map<String, Object>> getMaxUse();

    @Select("select u.username, sendApplicationNum + matchNum score, sendApplicationNum from soulmatch left join user u on u.id = userId where sendApplicationNum + matchNum = (select MAX(sendApplicationNum + matchNum) from soulmatch)")
    List<Map<String, Object>> getFlowerHeartMost();
}
