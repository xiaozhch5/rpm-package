package com.zh.ch.bigdata.base.util.shell;

/**
 * @author xzc
 * @description
 * @date 2021/01/15
 */
public class ClientHost {

    private String host;

    private int port;

    private String username;

    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ClientHost{" + "host='" + host + '\'' + ", port=" + port + ", username='" + username + '\''
                + ", password='" + password + '\'' + '}';
    }

}
