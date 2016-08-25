package com.jrwong.modules.iOSPackage.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by J on 16/8/25.
 */
public class IOSConfig {

    public IOSConfig() {
        super();
        svnpath = "";
        username = "";
        pwd = "";
        target = "";
        scheme = "";
        configuration = "";
        cerFriendlyName = "";
        p12pwd = "";
    }

    public IOSConfig(Properties prop) {
        this.svnpath = prop.getProperty("svnpath");
        this.username= prop.getProperty("username");
        this.pwd = prop.getProperty("pwd");
        this.scheme = prop.getProperty("scheme");
        this.target = prop.getProperty("target");
        this.configuration = prop.getProperty("configuration");
        this.p12pwd = prop.getProperty("p12pwd");
        this.cerFriendlyName = prop.getProperty("cerFriendlyName");
    }



    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("svnpath", svnpath);
        map.put("username", username);
        map.put("pwd", pwd);
        map.put("scheme", scheme);
        map.put("target", target);
        map.put("configuration", configuration);
        map.put("cerFriendlyName", cerFriendlyName);
        map.put("p12pwd", p12pwd);
        return map;
    }

    public Properties toProperties() {
        Properties prop = new Properties();
        prop.setProperty("svnpath", svnpath);
        prop.setProperty("username", username);
        prop.setProperty("pwd", pwd);
        prop.setProperty("scheme", scheme);
        prop.setProperty("target", target);
        prop.setProperty("configuration", configuration);
        prop.setProperty("cerFriendlyName", cerFriendlyName);
        prop.setProperty("p12pwd", p12pwd);
        return prop;
    }

    private String svnpath;
    private String username;
    private String pwd;
    private String scheme;
    private String target;
    private String configuration;
    private String cerFriendlyName;
    private String p12pwd;

    public String getP12pwd() {
        return p12pwd;
    }

    public void setP12pwd(String p12pwd) {
        this.p12pwd = p12pwd;
    }

    public String getCerFriendlyName() {
        return cerFriendlyName;
    }

    public void setCerFriendlyName(String cerFriendlyName) {
        this.cerFriendlyName = cerFriendlyName;
    }



    public String getSvnpath() {
        return svnpath;
    }

    public void setSvnpath(String svnpath) {
        this.svnpath = svnpath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return "IOSConfig{" +
                "svnpath='" + svnpath + '\'' +
                ", username='" + username + '\'' +
                ", pwd='" + pwd + '\'' +
                ", scheme='" + scheme + '\'' +
                ", target='" + target + '\'' +
                ", configuration='" + configuration + '\'' +
                '}';
    }
}
