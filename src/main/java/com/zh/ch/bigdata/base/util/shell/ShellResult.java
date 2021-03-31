package com.zh.ch.bigdata.base.util.shell;

/**
 * @author xzc
 * @description
 * @date 2021/01/15
 */
public class ShellResult {

    private boolean success;

    private String stdout;

    private String stderr;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    @Override
    public String toString() {
        return "ShellResult{"
                + "success="
                + success
                + ", stdout='"
                + stdout
                + '\''
                + ", stderr='"
                + stderr
                + '\''
                + '}';
    }
}
