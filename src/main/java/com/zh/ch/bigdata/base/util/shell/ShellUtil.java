package com.zh.ch.bigdata.base.util.shell;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xzc
 * @description
 * @date 2021/01/15
 */
public class ShellUtil {
    public static ShellResult executeCmd(String cmd) {
        ShellResult result = new ShellResult();

        int status = 0;
        String s;
        try {
            Process process = Runtime.getRuntime().exec(cmd);

            BufferedReader stdInput =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError =
                    new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder inputBuilder = new StringBuilder();
            StringBuilder errorBuilder = new StringBuilder();
            while ((s = stdInput.readLine()) != null) {
                inputBuilder.append(s).append("\n");
            }
            while ((s = stdError.readLine()) != null) {
                errorBuilder.append(s).append("\n");
            }
            status = process.waitFor();
            String errmsg = errorBuilder.toString();
            if (status == 0 && errmsg.isEmpty()) {
                result.setSuccess(true);
            }
            result.setStdout(inputBuilder.toString());
            result.setStderr(errmsg);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            result.setStderr(e.getMessage());
        }
        return result;
    }

    /**
     * 执行shell脚本
     *
     * @param shellPath 脚本路径
     * @param params 参数
     * @return ShellResult
     */
    public static ShellResult executeShell(String shellPath, String... params) {
        ShellResult result = new ShellResult();
        ProcessBuilder pb = new ProcessBuilder(shellPath);
        List<String> command = new ArrayList<>();
        command.add(shellPath);
        for (String param : params) {
            command.add(param);
        }
        pb.command(command);
        int status = 0;
        String s;
        try {
            Process p = pb.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuilder inputBuilder = new StringBuilder();
            StringBuilder errorBuilder = new StringBuilder();
            while ((s = stdInput.readLine()) != null) {
                inputBuilder.append(s).append("\n");
            }
            while ((s = stdError.readLine()) != null) {
                // System.out.println(new String(s.getBytes("gbk"), "utf-8"));
                errorBuilder.append(s).append("\n");
            }
            status = p.waitFor();
            String errmsg = errorBuilder.toString();
            if (status == 0 && errmsg.isEmpty()) {
                result.setSuccess(true);
            }
            result.setStdout(inputBuilder.toString());
            result.setStderr(errmsg);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            result.setStderr(e.getMessage());
        }
        return result;
    }

    /**
     * ssh上传文件
     *
     * @param localFilePath 本地文件
     * @param remoteTargetDirectory 远程文件夹
     * @param clientHost 主机信息
     * @return ShellResult
     */
    public static ShellResult sshUploadFile(
            String localFilePath, String remoteTargetDirectory, ClientHost clientHost) {
        ShellResult result = new ShellResult();
        int sshPort = clientHost.getPort();
        String mode = "0777";
        Connection conn = null;
        StringBuilder errorBuilder = new StringBuilder();
        try {
            conn = new Connection(clientHost.getHost(), sshPort);
            conn.connect();
            // 用户登录验证
            boolean isAuthenticated =
                    conn.authenticateWithPassword(
                            clientHost.getUsername(), clientHost.getPassword());
            if (!isAuthenticated) {
                errorBuilder.append(
                        String.format(
                                "Login failed: host:%s, username:%s, password:%s\n",
                                clientHost.getHost(),
                                clientHost.getUsername(),
                                clientHost.getPassword()));
            } else {
                // 检查本地文件是否存在
                File file = new File(localFilePath);
                if (file.exists()) {
                    SCPClient scpClient = conn.createSCPClient();
                    scpClient.put(localFilePath, remoteTargetDirectory, mode);
                    result.setSuccess(true);
                } else {
                    errorBuilder.append(
                            String.format("The local file does not exist: %s\n", localFilePath));
                }
            }
        } catch (Exception e) {
            errorBuilder.append(e.getMessage()).append("\n");
        } finally {
            if (null != conn) {
                conn.close();
            }
        }
        result.setStderr(errorBuilder.toString());
        return result;
    }

    /**
     * ssh执行命令
     *
     * @param cmds 命令数组
     * @param clientHost 主机信息
     * @return ShellResult
     */
    public static ShellResult sshExecuteCmd(String[] cmds, ClientHost clientHost) {
        ShellResult result = new ShellResult();
        int sshPort = clientHost.getPort();
        Connection conn = null;
        StringBuilder outBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();
        try {
            conn = new Connection(clientHost.getHost(), sshPort);
            // 打开连接
            conn.connect();
            // 用户登录验证
            boolean isAuthenticated =
                    conn.authenticateWithPassword(
                            clientHost.getUsername(), clientHost.getPassword());
            if (!isAuthenticated) {
                errorBuilder.append(
                        String.format(
                                "Login failed: host:%s, username:%s, password:%s\n",
                                clientHost.getHost(),
                                clientHost.getUsername(),
                                clientHost.getPassword()));
            } else {
                for (String cmd : cmds) {
                    Session session = null;
                    String s;
                    try {
                        session = conn.openSession();
                        session.execCommand(cmd);
                        BufferedReader stdOut =
                                new BufferedReader(
                                        new InputStreamReader(
                                                session.getStdout(), StandardCharsets.UTF_8));
                        BufferedReader stdError =
                                new BufferedReader(
                                        new InputStreamReader(
                                                session.getStderr(), StandardCharsets.UTF_8));
                        while ((s = stdOut.readLine()) != null) {
                            outBuilder.append(s).append("\n");
                        }
                        while ((s = stdError.readLine()) != null) {
                            errorBuilder.append(s).append("\n");
                        }
                        String errmsg = errorBuilder.toString();
                        if (errmsg.isEmpty()) {
                            result.setSuccess(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (session != null) {
                            session.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            errorBuilder.append(e.getMessage()).append("\n");
        } finally {
            if (null != conn) {
                conn.close();
            }
        }
        result.setStdout(outBuilder.toString());
        result.setStderr(errorBuilder.toString());
        return result;
    }
}
