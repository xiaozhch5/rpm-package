package com.zh.ch.bigdata.rpm.domain;

/**
 * @author xzc
 * @description
 * @date 2021/03/31
 */
public class DirMapping {

    private String from;

    private String to;

    private String execute;

    private String filemode;

    private String userName;

    private String groupName;

    private String directoryIncluded;

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setExecute(String execute) {
        this.execute = execute;
    }

    public void setFilemode(String filemode) {
        this.filemode = filemode;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setDirectoryIncluded(String directoryIncluded) {
        this.directoryIncluded = directoryIncluded;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getExecute() {
        return execute;
    }

    public String getUserName() {
        return userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDirectoryIncluded() {
        return directoryIncluded;
    }

    public String getFilemode() {
        return filemode;
    }
}
