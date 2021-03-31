package com.zh.ch.bigdata.base.util.exception;

/**
 * Json解析错误处理，针对找不到key值的异常
 * @author hadoop
 */
public class JsonKeyNotFoundException extends ProjectException {

    String exceptionMessage;

    public JsonKeyNotFoundException() {
        super();
    }

    public JsonKeyNotFoundException(String message) {
        super(message);
        exceptionMessage = message;
    }

    public JsonKeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonKeyNotFoundException(Throwable cause) {
        super(cause);
    }

    public JsonKeyNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String exceptionMessage) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("该JSON字符串中不含有该Key: ");
        stringBuilder.append(exceptionMessage);
        return stringBuilder.toString();
    }
}
