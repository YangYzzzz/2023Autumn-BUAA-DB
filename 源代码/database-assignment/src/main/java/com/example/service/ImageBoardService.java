package com.example.service;

import com.example.entity.ImageBoard;
import com.example.entity.RestBean;
import com.example.mapper.ImageBoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImageBoardService {
    @Autowired
    private ImageBoardMapper imageBoardMapper;


    public List<ImageBoard> getImageBoardByUid(Integer uid) {
        return imageBoardMapper.getImageBoardByUid(uid);
    }

    public void putNewPhotos(Integer userId, List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            imageBoardMapper.putNewPhoto(userId, imageUrl);
        }
    }

    public void deletePhotos(Integer userId, List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            String fileBasePath = "vue3\\public\\image\\";
            String[] strings = imageUrl.split("/");
            String filename = strings[strings.length - 1];
            File file = new File(fileBasePath + filename);
            file.delete();
            imageBoardMapper.deletePhoto(userId, imageUrl);
        }
    }

    public Pair<List<Map<String, Object>>, Integer> findPage(Integer pageNum, Integer pageSize, String username) {
        int limitStart = (pageNum - 1) * pageSize;
        int total = imageBoardMapper.getImageNum(username);
        return Pair.of(imageBoardMapper.findPage(limitStart, pageSize, username), total);
    }

    public void deleteImage(Integer id) {
        imageBoardMapper.deleteImageById(id);
    }

    public List<Map<String, Object>> getImgList(Integer userId) {
        List<ImageBoard> imageBoards = imageBoardMapper.getImageBoardByUid(userId);
        List<Map<String, Object>> resultLists = new ArrayList<>();
        for (ImageBoard imageBoard : imageBoards) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("name", "照片名起啥好");
            resultMap.put("url", imageBoard.getImgUrl());
            resultLists.add(resultMap);
        }
        return resultLists;
    }
}
