package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.entity.Post;
import com.example.entity.RestBean;
import com.example.entity.User;
import com.example.service.PostService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.FieldNameConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;


    @GetMapping("/getByUid")
    public RestBean getPostByUid(@RequestParam Integer uid) {
        return RestBean.success(postService.getPostByUid(uid));
    }

    @GetMapping("/getLiked")
    public RestBean getLiked(@RequestParam Integer uid, @RequestParam Integer pid) {
        return RestBean.success(postService.getLiked(uid, pid));
    }

    @GetMapping("/changeLiked")
    public RestBean changeLiked(@RequestParam Integer uid, @RequestParam Integer pid){
        String mes = postService.changeLiked(uid, pid);
        return RestBean.success(mes);
    }

    @GetMapping("/getByPid")
    public RestBean getPostByPid(@RequestParam Integer pid) {
        return RestBean.success(postService.getPostByPid(pid));
    }

    @GetMapping("/getLikeList")
    public RestBean getLikeList(@RequestParam Integer uid, @RequestParam Integer osId) {
        return RestBean.success(postService.getLikeList(uid, osId));
    }

    @PostMapping("/upload")
    public RestBean upload(@RequestParam MultipartFile file) throws IOException {
        // 设置保存路径
        String fileUploadPath = "vue3\\public\\image\\";
        // MultipartFile  多媒体文件
        String originalFileName = file.getOriginalFilename();
        String type = FileUtil.extName(originalFileName);
        // 为文件重命名
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + "." + type;
        File uploadFile = new File(fileUploadPath + fileUUID);
        //先上传文件到磁盘
        FileUtils.copyInputStreamToFile(file.getInputStream(), uploadFile);
        String url = "/public/image/" + fileUUID;
        // 返回给前端 on-success 方法中
        return RestBean.success(url);
    }

    // 考虑排序问题
    @GetMapping("/page")
    public RestBean findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(required = false) String username, @RequestParam(required = false) String title,
                             @RequestParam(required = false) String content) {
        return RestBean.success(postService.findPage(pageNum, pageSize, username, title, content));
    }

    @PostMapping("/newPost")
    public RestBean addNewPost(@RequestBody HashMap<String, Object> post) {
        Integer uid = (Integer) post.get("userId");
        Boolean type = (Boolean) post.get("type");
        String content = (String) post.get("content");
        List<String> imageLists = (List<String>) post.get("imageList");
        postService.addNewPost(uid, type, content, imageLists);
        return RestBean.success();
    }

    @GetMapping("/export") //导出接口
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查询出所有数据
        List<Post> list = postService.getAllPost();
        // 写入磁盘
        // ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx")
        // 在内存操作 写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //默认格式 强制输出标题
        writer.write(list, true);

        //设置浏览器相应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("帖子信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    @DeleteMapping("/del")
    public RestBean delPost(@RequestParam Integer pid) {
        postService.delPost(pid);
        return RestBean.success();
    }

    @PostMapping("/del/batch")
    public RestBean delBatchPost(@RequestBody ArrayList<Integer> ids) {
        postService.delBatchPost(ids);
        return RestBean.success();
    }

    @GetMapping("/square/getAllPost")
    public RestBean getAllPublicPost() {
        return RestBean.success(postService.getAllPublicPost());
    }

    @GetMapping("/square/getAllLikeList")
    public RestBean getAllLikeList(@RequestParam Integer uid) {
        return RestBean.success(postService.getAllLikeList(uid));
    }
}
