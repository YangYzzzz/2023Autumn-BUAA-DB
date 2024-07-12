package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.entity.RestBean;
import com.example.service.ImageBoardService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/imageBoard")
public class ImageBoardController {
    @Autowired
    private ImageBoardService imageBoardService;

    @GetMapping("/getByUid")
    public RestBean getImageBoardByUid(@RequestParam Integer uid) {
        return RestBean.success(imageBoardService.getImageBoardByUid(uid));
    }

    @PostMapping("/newPhotos")
    public RestBean putNewPhotos(@RequestBody HashMap<String, Object> imageInfos) {
        System.out.println(imageInfos);
        Integer userId = (Integer) imageInfos.get("userId");
        List<String> imageUrls = (List<String>) imageInfos.get("imageList");
        imageBoardService.putNewPhotos(userId, imageUrls);
        return RestBean.success();
    }

    @PostMapping("/deletePhotos")
    public RestBean deletePhotos(@RequestBody HashMap<String, Object> imageInfos) {
        Integer userId = (Integer) imageInfos.get("userId");
        List<String> imageUrls = (List<String>) imageInfos.get("deleteList");
        imageBoardService.deletePhotos(userId, imageUrls);
        return RestBean.success();
    }

    // "/imageBoard/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&username=" + this.username
    @GetMapping("/page")
    public RestBean findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                             @RequestParam(required = false) String username) {
        return RestBean.success(imageBoardService.findPage(pageNum, pageSize, username));
    }

    @DeleteMapping("/del")
    public RestBean deleteImage(@RequestParam Integer id) {
        imageBoardService.deleteImage(id);
        return RestBean.success();
    }

    @GetMapping("/getImgList")
    public RestBean getImgList(@RequestParam Integer userId) {
        return RestBean.success(imageBoardService.getImgList(userId));
    }
}
