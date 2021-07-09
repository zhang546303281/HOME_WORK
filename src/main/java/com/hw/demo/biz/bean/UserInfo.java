package com.hw.demo.biz.bean;

import java.io.Serializable;

/**
 * 登錄用戶信息
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1542901679404102578L;
    private Integer userKey;//用户主键
    private String userName;//用户登录名
    private String userTrueName;//用户真实姓名
    private Integer orgKey;//机构主键
    private String orgName;//机构名称
    private String orgCode;//机构编码（9位），与国家保持一致
    private String districtCode;//10位地区编码
    private String districtName;//地区名称
    private String loginIp;//本次用戶登錄IP
    private String loginBrowser;//本次用戶登錄瀏覽器
    private String loginBrowserVersion;//本次用戶登錄瀏覽器版本號

    public UserInfo(){

    }

    public UserInfo(String userName, String userTrueName) {
        this.userName = userName;
        this.userTrueName = userTrueName;
    }

    public Integer getUserKey() {
        return userKey;
    }

    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTrueName() {
        return userTrueName;
    }

    public void setUserTrueName(String userTrueName) {
        this.userTrueName = userTrueName;
    }

    public Integer getOrgKey() {
        return orgKey;
    }

    public void setOrgKey(Integer orgKey) {
        this.orgKey = orgKey;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginBrowser() {
        return loginBrowser;
    }

    public void setLoginBrowser(String loginBrowser) {
        this.loginBrowser = loginBrowser;
    }

    public String getLoginBrowserVersion() {
        return loginBrowserVersion;
    }

    public void setLoginBrowserVersion(String loginBrowserVersion) {
        this.loginBrowserVersion = loginBrowserVersion;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserInfo{");
        sb.append("userKey=").append(userKey);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", userTrueName='").append(userTrueName).append('\'');
        sb.append(", orgKey=").append(orgKey);
        sb.append(", orgName='").append(orgName).append('\'');
        sb.append(", orgCode='").append(orgCode).append('\'');
        sb.append(", districtCode='").append(districtCode).append('\'');
        sb.append(", districtName='").append(districtName).append('\'');
        sb.append(", loginIp='").append(loginIp).append('\'');
        sb.append(", loginBrowser='").append(loginBrowser).append('\'');
        sb.append(", loginBrowserVersion='").append(loginBrowserVersion).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
