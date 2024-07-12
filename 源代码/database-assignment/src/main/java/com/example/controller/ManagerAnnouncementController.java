package com.example.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.entity.ManagerAnnouncement;
import com.example.entity.RestBean;
import com.example.entity.User;
import com.example.service.ManagerAnnouncementService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
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

@RestController
@RequestMapping("/manager")
public class ManagerAnnouncementController {
    @Autowired
    private ManagerAnnouncementService managerAnnouncementService;

    @PostMapping("/sendManagerAnnouncement")
    public RestBean sendManagerAnnouncement(@RequestBody HashMap<String, Object> managerAnnouncementInfo) {
        Integer userId = (Integer) managerAnnouncementInfo.get("userId");
        String message = (String) managerAnnouncementInfo.get("message");
        List<Integer> receivers = (List<Integer>) managerAnnouncementInfo.get("receivers");
        System.out.println(receivers);
        managerAnnouncementService.sendManagerAnnouncement(userId, message, receivers);
        return RestBean.success();
    }

    @GetMapping("/getManagerAnnouncement")
    public RestBean getManagerAnnouncement(@RequestParam Integer userId) {
        return RestBean.success(managerAnnouncementService.getManagerAnnouncement(userId));
    }

    @GetMapping("/page")
    public RestBean page(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam Integer userId) {
        return RestBean.success(managerAnnouncementService.page(pageNum, pageSize, userId));
    }


    // TODO:
    @DeleteMapping("/del")
    public RestBean deleteManagerAnnouncement(@RequestParam Integer id) {
        managerAnnouncementService.deleteManagerAnnouncement(id);
        return RestBean.success();
    }

    @PostMapping("/del/batch")
    public RestBean deleteBatchManagerAnnouncement(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            managerAnnouncementService.deleteManagerAnnouncement(id);
        }
        return RestBean.success();
    }

    @GetMapping("/export") //导出接口
    public void export(HttpServletResponse response, @RequestParam Integer uid) throws Exception {
        System.out.println("通告导出" + uid);
        //从数据库查询出所有数据
        List<ManagerAnnouncement> list = managerAnnouncementService.getAllManagerAnnouncement(uid);
        System.out.println(list);
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
        String fileName = URLEncoder.encode("管理员通告信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }
}
