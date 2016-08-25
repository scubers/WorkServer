package com.jrwong.modules.iOSPackage.controller;

import com.jrwong.modules.common.controller.BaseController;
import com.jrwong.modules.common.util.ToolsKit;
import com.jrwong.modules.iOSPackage.bean.IOSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Created by J on 16/8/25.
 */
@Controller
@RequestMapping("ios")
public class IOSPackageController extends BaseController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping("config")
    public String config() throws Exception {
        request.setAttribute("config", loadConfig().toMap());
        return "iOSPackageConfiguration";
    }

    @RequestMapping("saveConfig")
    @ResponseBody
    public String saveConfig(IOSConfig config) throws Exception {
        System.out.println(config);

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(getConfigPath());
            config.toProperties().store(os, "update");
        } catch (Exception e) {
            return "{success:false}";
        }
        finally {
            if (os != null) {
                os.close();
            }
        }
        return "{success:true}";
    }

    @RequestMapping("uploadCer")
    public String fileUpload(@RequestParam("file") MultipartFile file, String type) throws Exception {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            // 文件保存路径
            String filePath = getFilePathWithType(Integer.parseInt(type)) + "/" + file.getOriginalFilename();
            // 转存文件
            file.transferTo(new File(filePath));
        }
        return "redirect:config.do";
    }

    @RequestMapping("package")
    @ResponseBody
    public String startpackage() throws Exception {
        IOSConfig config = loadConfig();
        String oripath = "/Users/mac/Desktop/bash.command";
        String string = ToolsKit.FileUtil.readFile(oripath);
        string = string.replace("${replace_svn_path}", config.getSvnpath());
        string = string.replace("${replace_target_path}", "/Users/mac/Desktop");
        string = string.replace("${replace_target_name}", config.getTarget());
        string = string.replace("${replace_code_sign}", config.getCerFriendlyName());
        string = string.replace("${replace_configuration}", config.getConfiguration());
        ToolsKit.FileUtil.writeFile("/Users/mac/Desktop/test1.command",string, false);
        runshell("/Users/mac/Desktop/test1.command");
        return "";
    }

    private boolean runshell(String filepath) {
        Process process;
        List<String> processList = new ArrayList<String>();
        try {
            process = Runtime.getRuntime().exec("chmod +x " + filepath);
            process = Runtime.getRuntime().exec(filepath);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                processList.add(line);
                System.out.println(line);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String getFilePathWithType(int type) {
        String path = getUploadPath() + (type == 1? "/certificate" : "/provisioning");
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    private String getUploadPath() {
        String path =  request.getServletContext().getRealPath("/") + "upload";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    private IOSConfig loadConfig() throws Exception {
        Properties prop = new Properties();

        FileInputStream is = null;
        try {
            is = new FileInputStream(getConfigPath());
            prop.load(is);
            IOSConfig config = new IOSConfig(prop);
            return config;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String getConfigPath() {
        return request.getServletContext().getRealPath("/") + "builds/iosconfig.properties";
    }

    public static void main(String[] args) throws Exception {

    }


}
