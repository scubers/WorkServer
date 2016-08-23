package com.jrwong.modules.test.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.File;

/**
 * Created by J on 16/8/23.
 */
@Controller
@RequestMapping("test")
public class TestController {

    @Autowired
    private HttpServletRequest request;


    @RequestMapping("upload")
    public String test() {
        request.getServletContext().setAttribute("name", "myname");
        return "test";
    }

    /***
     * 上传文件 用@RequestParam注解来指定表单上的file为MultipartFile
     *
     * @param file
     * @return
     */
    @RequestMapping("uploadFile")
    public String fileUpload(@RequestParam("file") MultipartFile file) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                // 文件保存路径
                String filePath = getUploadPath() + "/testupload";
                // 转存文件
                file.transferTo(new File(filePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 重定向
        return "redirect:/list.html";
    }

    private String getUploadPath() {
        String path =  request.getServletContext().getRealPath("/") + "upload";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }
}
