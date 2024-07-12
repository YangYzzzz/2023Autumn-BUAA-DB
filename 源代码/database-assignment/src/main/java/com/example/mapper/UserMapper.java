package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface UserMapper {
    @Select("select * from user u where u.username = #{username} and u.password = #{password}")
    List<User> login(@Param("username") String username, @Param("password") String password);

    @Select("select * from user u where u.studentId = #{studentId};")
    List<User> getSameIdUser(@Param("studentId") String studentId);

    @Insert("insert into user(username, password, major, studentId, nickname, avatarUrl, sex, address, grade, birthday, role, time) " +
            "values (#{username}, #{password}, #{major}, #{studentId}, #{nickname}, #{avatarUrl}, #{sex}, #{addr}, #{grade}, #{birthday}, '普通用户', NOW())")
    void insertUser(String studentId, String username, String password, String sex, String nickname, String addr, String major, String grade, String avatarUrl, String birthday);

    @Select("select count(*) from user")
    Integer getTotalUserNum();
    List<User> findPage(@Param("limitStart") int limitStart, @Param("pageSize") int pageSize,
                        @Param("username") String username, @Param("major") String major,
                        @Param("sex") String sex, @Param("studentId") String studentId);

    void delBatch(List<Integer> ids);
    @Delete("delete from user where id = #{id}")
    void del(Integer id);

    void updateUser(User user);

    @Select("select * from user")
    List<User> getAllUser();

    @Select("select *, -password from user where id = #{id}")
    List<User> getUserById(Integer id);

    @Select("select id from user where studentId = #{studentId}")
    Integer getUidByStudentId(Integer studentId);

    @Select("select id, avatarUrl, username from user where role = '普通用户' and #{username} is null or username like concat('%', #{username}, '%')")
    List<User> getNormalUser(String username);

    List<User> getUsersBySex(List<Integer> friends, Integer uid, String targetSex);

    @Select("SELECT DATE_FORMAT(time, '%Y-%m') AS month, COUNT(*) num \n" +
            "FROM user \n" +
            "GROUP BY month;")
    List<Map<String, Object>> getUserNumByMonth();

    @Select("select sex, COUNT(*) num from user group by sex")
    List<Map<String, Object>> getBoyGirl();

    @Select("select birthday from user where birthday is not null")
    List<String> getBirthdays();

    @Select("select address name, COUNT(*) value from user group by address order by COUNT(*) desc")
    List<Map<String, Object>> getAddress();

    @Select("select total_friend_count from user2friendnum where total_friend_count > #{min}")
    List<Integer> getTotalFriendNum(Integer min);

    @Update("update user set password = #{pw} where id = #{uid}")
    void changePassword(Integer uid, String pw);

    @Select("select password from user where id = #{uid}")
    String getPassword(Integer uid);

    @Select("select * from user where username = #{username} and password = #{password}")
    List<User> getSameUsernameAndPasswordUsers(String username, String password);

    @Select("select username from user2friendnum where total_friend_count > 0")
    List<String> getAllHaveFriendUser();

    @Select("select id, avatarUrl, username from user where role = '普通用户'")
    List<User> getAllNormalUser();
}
