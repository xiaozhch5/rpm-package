package com.zh.ch.bigdata.base.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author xzc
 * @description 文件写工具
 * @date 2021/02/08
 */
public class FileWriteUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileWriteUtil.class);

    /**
     * 写文件工具类， 将字符串写入到我呢见中
     *
     * @param filePath 文件名称，全路径
     * @param s 待写入数据
     * @throws IOException 异常
     */
    public static void writeFile(String filePath, String s) throws IOException {
        try {
            File file = new File(filePath);
            boolean flag = false;
            if (file.exists()) {
                flag = file.delete();
            }
            if (flag && file.createNewFile()) {
                Writer out = new FileWriter(file);
                out.write(s);
                out.close();
            }
        } catch (IOException e) {
            LOGGER.error("数据{}写入到{}文件中写入失败", s, filePath, e);
            throw e;
        }
    }
}
