package com.example.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface Label2UserMapper {

    @Insert("insert into label2user(userId, labelId) values (#{uid}, #{labelId})")
    void setLabel(int uid, Integer labelId);

    @Select("select labelId from label2user where userId = #{id}")
    ArrayList<Integer> getLabel(int id);

    @Delete("delete from label2user where userId = #{uid}")
    void delLabel(Integer uid);
}
