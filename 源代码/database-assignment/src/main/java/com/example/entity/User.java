package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 用户信息
public class User  implements Comparable<User>{
    private Integer id; // 唯一Id
    private String studentId; // 学号 未必全是数字
    private String username; // 真实姓名
    private String password; // 密码
    private Date time; // 注册时间
    private String nickname; // 昵称
    private String avatarUrl; // 头像url
    private String backgroundUrl; // 背景图片url
    private String sex; // 性别
    private String signature; // 个性签名
    private String address; // 家乡 精确到省即可
    private String major; // 专业
    private String grade; // 年级
    private String birthday; // 生日
    private String role; // 身份 普通用户 or 管理员

    // 无需存储到数据库
    private String age; // 年龄
    private String constellation; // 星座 不用自己填 填生日转化即可
    private float score; // 匹配结果打分
    private List<String> imageBoardUrls;
    private List<Integer> checkTags;
    public int compareTo(User user) {
        return Float.compare(user.score, this.score);
    }
}

