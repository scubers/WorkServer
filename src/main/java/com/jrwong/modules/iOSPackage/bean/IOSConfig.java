package com.jrwong.modules.iOSPackage.bean;


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

    private String projectName;

    private String svnpath;
    private String username;
    private String pwd;
    private String scheme;
    private String target;
    private String configuration;
    private String cerFriendlyName;
    private String p12pwd;
    private String bundleId;

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }
}
