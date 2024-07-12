package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.example.entity.RestBean;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/python")
public class PythonController {
    @PostMapping("/test")
    public RestBean test() {
        try {
            String basePath = "D:\\Users\\2024冯如杯物理对抗样本攻击\\pytorch-CycleGAN-and-pix2pix-master\\pytorch-CycleGAN-and-pix2pix-master";
            String secondPath = "\\my_model_test";
            String targetPath = "D:\\cpp java\\database-assignment\\vue3\\public\\pix2piximage";
            // 指定 Python 解释器的路径和 Python 脚本的路径
            ProcessBuilder pb = new ProcessBuilder("python", basePath + secondPath + "\\model_test.py", basePath + secondPath + "\\cmp_b0351.jpg", "-c", "white", "-o", targetPath + "\\result.jpg");
            Process p = pb.start();

            // 读取脚本的标准输出
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            // 等待脚本执行完成
            p.waitFor();
            System.out.println("脚本执行完成");
        } catch (Exception e) {
            System.out.println("出现问题");
            e.printStackTrace();
        }

        return RestBean.success();
    }

    @PostMapping("/pix2pix")
    public RestBean pix2pix(@RequestParam MultipartFile file) {
        try {
            // 步骤一：将文件转存到basic下
            String fileUploadPath = "vue3\\public\\pix2piximage\\basic\\";
            String originalFileName = file.getOriginalFilename();
            String type = FileUtil.extName(originalFileName);
            String uuid = IdUtil.fastSimpleUUID();
            String tmpuuid = uuid;
            String fileUUID = uuid + "." + type;
            File uploadFile = new File(fileUploadPath + fileUUID);
            FileUtils.copyInputStreamToFile(file.getInputStream(), uploadFile);
            String basicUrl = "/public/pix2piximage/basic/" + fileUUID;

            // 步骤二：将文件转化为指定格式
            String basePath = "D:\\Users\\2024冯如杯物理对抗样本攻击\\pytorch-CycleGAN-and-pix2pix-master\\pytorch-CycleGAN-and-pix2pix-master";
            String secondPath = "\\my_model_test";
            String sourcePath = "vue3\\public\\pix2piximage\\basic\\" + fileUUID;
            String targetPath = "vue3\\public\\pix2piximage\\test\\";
            ProcessBuilder pb = new ProcessBuilder("python", basePath + secondPath + "\\model_test.py", sourcePath , "-c", "white", "-o", targetPath + fileUUID);
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            p.waitFor();

            // 步骤三：模型生成
            // python test.py --dataroot test_data\AB --name facades_pix2pix --model pix2pix --direction BtoA
            String modelPath = "D:\\Users\\2024冯如杯物理对抗样本攻击\\pytorch-CycleGAN-and-pix2pix-master\\pytorch-CycleGAN-and-pix2pix-master";
            String imagePath = "D:\\cpp java\\database-assignment\\vue3\\public\\pix2piximage";
            // 指定 Python 解释器的路径和 Python 脚本的路径
            pb = new ProcessBuilder("python",
                    modelPath + "\\test.py",
                    "--dataroot", imagePath,
                    "--checkpoints_dir", modelPath + "\\checkpoints",
                    "--name", "facades_pix2pix",
                    "--model", "pix2pix",
                    "--direction", "BtoA",
                    "--results_dir", imagePath + "\\results\\");
            p = pb.start();

            // 读取脚本的标准输出
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            // 等待脚本执行完成
            p.waitFor();

            // 复制
            fileUploadPath = "vue3\\public\\pix2piximage\\target\\";
            uuid = IdUtil.fastSimpleUUID();
            fileUUID = uuid + ".png";
            uploadFile = new File(fileUploadPath + fileUUID); // 上传到的位置
            InputStream sourceStream = new FileInputStream("vue3\\public\\pix2piximage\\results\\facades_pix2pix\\test_latest\\images\\" + tmpuuid + "_fake_B.png");
            FileUtils.copyInputStreamToFile(sourceStream, uploadFile);
            String targetUrl = "/public/pix2piximage/target/" + fileUUID;
            // 返回给前端 on-success 方法中
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("basicUrl", basicUrl);
            resultMap.put("targetUrl", targetUrl);

            // 删除test下文件
            File directory = new File("vue3\\public\\pix2piximage\\test\\");
            if (directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file1 : files) {
                        if (!file1.delete()) {
                            System.out.println("删除失败");
                        }
                    }
                }
            }

            // 删除result下文件
            File resultDirectory = new File("vue3\\public\\pix2piximage\\results\\");
            if (resultDirectory.exists() && resultDirectory.isDirectory()) {
                deleteDirectory(resultDirectory);
            }
            return RestBean.success(resultMap);

        } catch (Exception e) {
            System.out.println("出现问题");
            e.printStackTrace();
        }

        return RestBean.failure(100);
    }

    public static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) { // some JVMs return null for empty directories
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        if (!directory.delete()) {
            // Handle the case where the directory may not be successfully deleted
            System.err.println("Failed to delete " + directory);
        }
    }
}
