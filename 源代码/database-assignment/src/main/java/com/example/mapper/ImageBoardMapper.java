package com.example.mapper;

import com.example.entity.ImageBoard;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
@Mapper
public interface ImageBoardMapper {

    @Select("select * from imageboard where userId = #{uid}")
    List<ImageBoard> getImageBoardByUid(Integer uid);


    @Insert("insert into imageboard(userId, imgUrl, time) values(#{userId}, #{imageUrl}, NOW())")
    void putNewPhoto(Integer userId, String imageUrl);

    @Delete("delete from imageboard where userId = #{userId} and imgUrl = #{imageUrl}")
    void deletePhoto(Integer userId, String imageUrl);

    @MapKey("id")
    List<Map<String, Object>> findPage(int limitStart, Integer pageSize, String username);

    int getImageNum(String username);

    @Delete("delete from imageboard where id = #{id}")
    void deleteImageById(Integer id);

    @Select("select imgUrl from imageboard where userId = #{uid} limit 0, 9")
    List<String> getImageBoardUrlsByUid(Integer id);
}
