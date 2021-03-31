/*
 * SSH客户端工具
 */
package com.zh.ch.bigdata.base.util.shell;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

/** @author hadoop */
public class SshClientUtil {

    /** 私有变量 */
    private Session session;

    /** 默认构造方法 */
    public SshClientUtil() {}

    /**
     * 初始化SSH连接
     *
     * @param hostName hostName
     * @param userName 用户名
     * @param passWd 密码
     * @return 是否建立连接
     * @throws JSchException 异常
     */
    public boolean initConnection(String hostName, String userName, String passWd)
            throws JSchException {
        try {
            JSch jSch = new JSch();
            session = jSch.getSession(userName, hostName);
            session.setPassword(passWd);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            return true;
        } catch (JSchException jSchException) {
            throw new JSchException(jSchException.getMessage());
        }
    }

    /**
     * 执行命令并返回将结果返回为InputStream
     *
     * @param command 待执行的命令
     * @return InputStream
     * @throws JSchException 异常
     * @throws IOException 异常
     */
    public InputStream exec(String command) throws JSchException, IOException {
        try {
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            InputStream inputStream = channelExec.getInputStream();
            channelExec.setCommand(command);
            channelExec.setErrStream(System.err);
            channelExec.connect();
            return inputStream;
        } catch (JSchException jSchException) {
            throw new JSchException(jSchException.getMessage());
        } catch (IOException ioException) {
            throw new IOException(ioException.getMessage());
        }
    }
}
