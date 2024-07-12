package com.example.mapper;

import com.example.entity.Label;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface LabelMapper {
    @Select("select * from label")
    ArrayList<Label> getLabels();

    @Select("select name from label where id = #{id}")
    String getLabelById(int id);

    @Select("select COUNT(*) from label2user where labelId = #{id}")
    Integer getUsedTimesByLid(Integer id);

    @Select("select * from label where name like concat('%', #{name}, '%')")
    List<Label> getLabelWithName(String name);

    @Update("update label set type = #{type}, name = #{name}, weight = #{weight} where id = #{id}")
    void updateLabel(Integer id, String type, String name, float weight);

    @Delete("delete from label where id = #{id}")
    void delLabel(Integer id);

    @Insert("insert into label(type, name, weight) values(#{type}, #{name}, #{weight})")
    void addNewLabel(String type, String name, float weight);

    void delBatch(List<Integer> ids);

    @Select("select labelId from label2user where userId = #{uid}")
    List<Integer> getUserLabelList(Integer uid);

    @Select("select name from label where id in (select labelId from label2user where userId = #{uid})")
    List<String> getUserLabelNameList(Integer uid);
}
