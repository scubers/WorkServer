package com.jrwong.modules.iOSPackage.controller;

import com.jrwong.modules.common.controller.BaseController;
import com.jrwong.modules.common.util.ReflectUtil;
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
import java.util.*;

/**
 * Created by J on 16/8/25.
 */
@Controller
@RequestMapping("ios")
public class IOSPackageController extends BaseController {

    public static String CER_FILE_NAME = "cer.p12";
    public static String PROVISION_FILE_NAME = "pro.mobileprovision";
    public static String CONFIG_FILE_NAME = "config";
    public static String SRC_DIR_NAME = "src";


    @Autowired
    private HttpServletRequest request;

    @RequestMapping("config")
    public String config(String projectName) throws Exception {
        File file = new File(getProjectPath(projectName) + IOSPackageController.CONFIG_FILE_NAME);
        if (file.exists()) {
            request.setAttribute("config", ReflectUtil.beanToMap(loadConfig(projectName)));
        }
        return "iOSPackageConfiguration";
    }

    @RequestMapping("saveConfig")
    @ResponseBody
    public String saveConfig(IOSConfig config) throws Exception {

        createProjectDir(config.getProjectName());

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(getProjectPath(config.getProjectName()) + IOSPackageController.CONFIG_FILE_NAME);
            ReflectUtil.beanToProperties(config).store(os, "update");
        } catch (Exception e) {
            return "{success:false}";
        } finally {
            if (os != null) {
                os.close();
            }
        }
        return "{success:true}";
    }

    @RequestMapping("uploadCer")
    public String cerUpload(@RequestParam("file") MultipartFile file, String projectName) throws Exception {
        if (!checkNameExists(projectName)) {
            throw new Exception();
        }
        // 判断文件是否为空
        if (!file.isEmpty()) {
            // 文件保存路径
            String filePath = getProjectPath(projectName) + IOSPackageController.CER_FILE_NAME;
            // 转存文件
            file.transferTo(new File(filePath));
        }
        return "redirect:config.do?projectName=" + projectName;
    }

    @RequestMapping("uploadProvision")
    public String provisionUpload(@RequestParam("file") MultipartFile file, String projectName) throws Exception {

        if (!checkNameExists(projectName)) {
            throw new Exception();
        }

        // 判断文件是否为空
        if (!file.isEmpty()) {
            // 文件保存路径
            String filePath = getProjectPath(projectName) + IOSPackageController.PROVISION_FILE_NAME;
            // 转存文件
            file.transferTo(new File(filePath));
        }
        return "redirect:config.do?projectName=" + projectName;
    }

    @RequestMapping("package")
    @ResponseBody
    public String startpackage(String projectName) throws Exception {
        IOSConfig config = loadConfig(projectName);
        File file = new File(getProjectPath(projectName) + IOSPackageController.SRC_DIR_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (config != null) {
            runshell(config);
            return "";
        }
        return "";
    }

    private boolean runshell(IOSConfig config) {
        Process process;
        List<String> processList = new ArrayList<String>();
        BufferedReader input = null;
        try {
            process = Runtime.getRuntime().exec("chmod +x /Users/mac/Desktop/autopackage.sh");
            String abc = "/Users/mac/Desktop/autopackage.sh " +
                    " " + config.getSvnpath() + " " + // svn
                    " " + config.getUsername() + " " + // svn
                    " " + config.getPwd() + " " + // svn
                    " " + getProjectPath(config.getProjectName()) + IOSPackageController.SRC_DIR_NAME + " " + // src 目录
                    " " + getProjectPath(config.getProjectName()) + IOSPackageController.PROVISION_FILE_NAME + " " + // provision path
                    " " + getProjectPath(config.getProjectName()) + IOSPackageController.CER_FILE_NAME + " " + // p12 path
                    " " + (config.getP12pwd().length() == 0 ? "isEmptyPwd" : config.getP12pwd()) + " " + // p12 pwd
                    " " + config.getConfiguration() + " " + // configuration
                    " " + config.getTarget() + " " + // target
                    " " + loadBaseConfig().getProperty("keychainpwd") + // keychain pwd
                    " " + request.getServletContext().getRealPath("/") +loadBaseConfig().getProperty("ipaExportPath") +  // ipa export path
                    " " + request.getServletContext().getRealPath("/") +loadBaseConfig().getProperty("ipaPlistPath") + // plist export path
                    " " + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/work/builds/ipas " + // down load url
                    " " + config.getBundleId() + "/work/ios/download.do " // down load url
            ;
            System.out.println(abc);
            process = Runtime.getRuntime().exec(abc);
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                processList.add(line);
                System.out.println(line);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @RequestMapping("downloads")
    public String downloads() {
        String path = request.getServletContext().getRealPath("/builds/plists");
        File file = new File(path);
        File[] files = file.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File o1, File o2) {
                return (int)(o2.lastModified() - o1.lastModified());
            }
        });
        List<String> filenames = new ArrayList<String>(files.length);
        for (File f: files) {
            filenames.add(f.getName());
        }
        request.setAttribute("filenames", filenames);
        request.setAttribute("downloadBaseUrl", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/work/builds/plists/");
        return "downloads";
    }

    private IOSConfig loadConfig(String projectName) throws Exception {
        Properties prop = new Properties();

        FileInputStream is = null;
        try {
            is = new FileInputStream(getProjectPath(projectName) + IOSPackageController.CONFIG_FILE_NAME);
            prop.load(is);
            IOSConfig config = ReflectUtil.propertiesToBean(prop, IOSConfig.class);
            return config;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String getIOSConfigBasePath() {
        return loadBaseConfig().getProperty("outputpath");
    }

    private Properties loadBaseConfig() {
        Properties prop = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(request.getServletContext().getRealPath("/") + "WEB-INF/classes/iosconfig/baseConfig.properties");
            prop.load(in);

        } catch (Exception e) {

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }

    private boolean checkNameExists(String projectName) {
        File file = new File(getIOSConfigBasePath() + projectName);
        return file.exists();
    }

    private void createProjectDir(String projectName) {
        File file = new File(getIOSConfigBasePath() + projectName);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private String getProjectPath(String projectName) {
        return getIOSConfigBasePath() + projectName + "/";
    }

    public static void main(String[] args) throws Exception {

    }


}
