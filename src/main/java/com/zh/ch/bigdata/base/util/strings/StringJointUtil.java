package com.zh.ch.bigdata.base.util.strings;

/**
 * 字符串拼接工具
 *
 * @author hadoop
 */
public class StringJointUtil {

    /**
     * 返回拼接后的字符串
     *
     * @param strings 待拼接的字符串
     * @return 返回拼接后的字符串
     */
    public static String getString(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }
}
