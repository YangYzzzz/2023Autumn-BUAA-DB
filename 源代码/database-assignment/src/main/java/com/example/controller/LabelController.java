package com.example.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.entity.Label;
import com.example.entity.Post;
import com.example.entity.RestBean;
import com.example.entity.User;
import com.example.service.LabelService;
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

import javax.imageio.stream.IIOByteBuffer;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelService labelService;
    @GetMapping
    public RestBean getLabel() {
        return labelService.getLabel();
    }

    @GetMapping("/getLabel")
    public RestBean getAllLabel(@RequestParam(required = false) String name) {
        if (name.isEmpty()) {
            return RestBean.success(labelService.getAllLabel());
        } else {
            return RestBean.success(201, labelService.getLabelWithName(name));
        }
    }

    @PostMapping("/updateLabel")
    public RestBean updateLabel(@RequestBody Label label) {
        labelService.updateLabel(label);
        return RestBean.success();
    }

    @DeleteMapping("/del")
    public RestBean delLabel(@RequestParam Integer id) {
        labelService.delLabel(id);
        return RestBean.success();
    }
    @PostMapping("/del/batch")
    public RestBean delBatchLabel(@RequestBody List<Integer> ids) {
        labelService.delBatchLabel(ids);
        return RestBean.success();
    }
    @PostMapping("/addNewLabel")
    public RestBean addNewLabel(@RequestBody Label label) {
        labelService.addNewLabel(label);
        return RestBean.success();
    }

    @GetMapping("/export") //导出接口
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查询出所有数据
        List<Label> list = labelService.export();
        // 写入磁盘
        // ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx")
        // 在内存操作 写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //默认格式 强制输出标题
        writer.write(list, true);

        //设置浏览器相应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("标签信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    @PostMapping("/import")
    public RestBean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Label> list = reader.readAll(Label.class); //以User类格式解析
        System.out.println(list);
        for (Label label : list) {
            labelService.addNewLabel(label);
        }
        return RestBean.success();
    }

    @GetMapping("/getUserLabelList")
    public RestBean getUserLabelList(@RequestParam Integer uid) {
        return RestBean.success(labelService.getUserLabelList(uid));
    }

    @GetMapping("/getUserLabelNameList")
    public RestBean getUserLabelNameList(@RequestParam Integer uid) {
        return RestBean.success(labelService.getUserLabelNameList(uid));
    }
}
