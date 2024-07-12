package com.example.controller;

import ch.qos.logback.core.joran.sanity.Pair;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.entity.RestBean;
import com.example.entity.User;
import com.example.service.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Delete;
import org.apache.poi.poifs.crypt.dsig.facets.OOXMLSignatureFacet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public RestBean login(@RequestBody User user) {
        System.out.println("111111111111111111");
        return userService.login(user);
    }

    @PostMapping("/register")
    public RestBean register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/page")
    public RestBean findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(required = false) String username, @RequestParam(required = false) String major,
                            @RequestParam(required = false) String sex, @RequestParam(required = false) String studentId) {
        return RestBean.success(userService.findPage(pageNum, pageSize, username, major, sex, studentId));
    }

    @PostMapping("/del/batch")
    public RestBean delBatch(@RequestBody List<Integer> ids) {
        return userService.delBatch(ids);
    }

    @DeleteMapping("/del")
    public RestBean del(@RequestParam Integer id) {
        return userService.del(id);
    }

    // 后续把后台接口统一加上/backend
    @PostMapping("/updateOrInsert")
    public RestBean updateOrSave(@RequestBody User user) {
        return userService.updateOrInsert(user);
    }

    @GetMapping("/export") //导出接口
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查询出所有数据
        List<User> list = userService.getAllUser();
        // 写入磁盘
        // ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx")
        // 在内存操作 写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
//        writer.addHeaderAlias("username", "用户名");
//        writer.addHeaderAlias("password", "密码");
//        writer.addHeaderAlias("nickname", "昵称");
//        writer.addHeaderAlias("email", "邮箱");
//        writer.addHeaderAlias("phone", "电话");
//        writer.addHeaderAlias("address", "地址");
//        writer.addHeaderAlias("createTime", "创建时间");
        //默认格式 强制输出标题
        writer.write(list, true);

        //设置浏览器相应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    //导入信息
    @PostMapping("/import")
    public RestBean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User> list = reader.readAll(User.class); //以User类格式解析
        //List<User> list = reader.read(0, 1, User.class);
        System.out.println(list);
        userService.saveAllUser(list);
        return RestBean.success();
    }

    @GetMapping("/getInfo")
    public RestBean getUserById(@RequestParam Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return RestBean.success(user);
        } else {
            return RestBean.failure(400);
        }
    }

    @PostMapping("/chooseLabel")
    public RestBean chooseLabel(@RequestBody List<Integer> labelList) {
        userService.chooseLabel(labelList);
        return RestBean.success();
    }

    @PostMapping("/changeInfo")
    public RestBean changeInfo(@RequestBody User user) {
        return RestBean.success(userService.changeInfo(user));
    }

    @GetMapping("/getNormalUser")
    public RestBean getNormalUser(@RequestParam String username) {
        return RestBean.success(userService.getNormalUser(username));
    }

    @PostMapping("/changePassword")
    public RestBean changePW(@RequestBody Map<String, Object> info) {
        Integer uid = (Integer) info.get("uid");
        String newPw = (String) info.get("pw");
        String oldPw = (String) info.get("old");
        return userService.changePassword(uid, newPw, oldPw);
    }
}
