package com.example.service;
import com.example.entity.RestBean;
import com.example.entity.User;
import com.example.mapper.Label2UserMapper;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Label2UserMapper label2UserMapper;
    public static final String[] CONSTELLATION_ARR = { "..", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座" };
    public static final int[] CONSTELLATION_EDGE_DAY = { 0, 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };

    public RestBean login(User user) {
        List<User> users = userMapper.login(user.getUsername(), user.getPassword());
        if (users.size() == 1) {
            users.get(0).setPassword(null);
            return RestBean.success(users.get(0));
        } else {
            return RestBean.failure(100);
        }
    }

    public static String getConstellation(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        if (day <= CONSTELLATION_EDGE_DAY[month]) {
            month = month - 1;
        }
        if (month > 0) {
            return CONSTELLATION_ARR[month];
        }
        return CONSTELLATION_ARR[12];
    }
    public RestBean register(User user) {
        // 若学号相同 则注册失败
        List<User> sameIdUsers = userMapper.getSameIdUser(user.getStudentId());
        // 若用户名密码重复，则注册失败
        List<User> sameUsernameAndPasswordUsers = userMapper.getSameUsernameAndPasswordUsers(user.getUsername(), user.getPassword());
        if (sameIdUsers.isEmpty() && sameUsernameAndPasswordUsers.isEmpty()) {
            String birthday = user.getBirthday();
            // 返回时可以使用
            String studentId = user.getStudentId();
            String username = user.getUsername();
            String password = user.getPassword();
            String sex = user.getSex();
            String nickname = user.getNickname();
            String addr = user.getAddress();
            String major = user.getMajor();
            String grade = user.getGrade();
            String avatarUrl;
            if (sex.equals("男")) {
                avatarUrl = "../image/boy_default_avatar.jpeg";
            } else {
                avatarUrl = "../image/girl_default_avatar.jpeg";
            }
            userMapper.insertUser(studentId, username, password, sex, nickname, addr, major, grade, avatarUrl, birthday);
            return RestBean.success();
        } else if (!sameIdUsers.isEmpty()){
            return RestBean.failure(100, "学号重复，已注册过");
        } else {
            return RestBean.failure(101, "用户名与密码重复，有同名的哥们与你选择了一样的密码，换个密码吧~");
        }
    }

    public Pair<Object, Integer> findPage(Integer pageNum, Integer pageSize, String username, String major, String sex, String studentId) {
        // 返回 Arr<User> 和 total
        int limitStart = pageSize * (pageNum - 1);
        int total = userMapper.getTotalUserNum();
        List<User> users = userMapper.findPage(limitStart, pageSize, username, major, sex, studentId);
        System.out.println(users);
        System.out.println(Pair.of(users, total));
        return Pair.of(users, total);
    }

    public RestBean delBatch(List<Integer> ids) {
        userMapper.delBatch(ids);
        return RestBean.success();
    }

    public RestBean del(Integer id) {
        userMapper.del(id);
        return RestBean.success();
    }

    public RestBean updateOrInsert(User user) {
        if (user.getId() != null) {
            userMapper.updateUser(user);
        } else {
            userMapper.insertUser(user.getStudentId(), user.getUsername(), user.getPassword(), user.getSex(),
                    user.getNickname(), user.getAddress(), user.getMajor(), user.getGrade(), user.getAvatarUrl(), user.getBirthday());
        }
        return RestBean.success();
    }

    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }

    public void saveAllUser(List<User> list) {
        for (User user : list) {
            userMapper.insertUser(user.getStudentId(), user.getUsername(), user.getPassword(), user.getSex(),
                    user.getNickname(), user.getAddress(), user.getMajor(), user.getGrade(), user.getAvatarUrl(), user.getBirthday());
        }
    }

    public User getUserById(Integer id) {
        List<User> users = userMapper.getUserById(id);
        if (users.size() == 1) {
            return users.get(0);
        } else {
            return null;
        }
    }

    public void chooseLabel(List<Integer> labelList) {
        Integer studentId = labelList.get(0);
        Integer uid = userMapper.getUidByStudentId(studentId);
        // 先删除全部
        label2UserMapper.delLabel(uid);
        // 再添加
        for (int i = 1; i < labelList.size(); i++) {
            label2UserMapper.setLabel(uid, labelList.get(i));
        }
    }

    public User changeInfo(User user) {
        userMapper.updateUser(user);

        Integer uid = user.getId();
        List<Integer> labelList = user.getCheckTags();
        label2UserMapper.delLabel(uid);
        for (int i = 1; i < labelList.size(); i++) {
            label2UserMapper.setLabel(uid, labelList.get(i));
        }

        User returnUser = userMapper.getUserById(user.getId()).get(0);
        returnUser.setPassword(null);
        return returnUser;
    }

    public List<User> getNormalUser(String username) {
        return userMapper.getNormalUser(username);
    }

    public RestBean changePassword(Integer uid, String newPw, String oldPw) {
        String password = userMapper.getPassword(uid);
        System.out.println(password);
        if (password.equals(oldPw)) {
            userMapper.changePassword(uid, newPw);
            return RestBean.success();
        } else {
            return RestBean.failure(100, "修改密码失败");
        }
    }
}
