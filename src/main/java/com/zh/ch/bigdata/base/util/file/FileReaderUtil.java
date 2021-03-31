package com.zh.ch.bigdata.base.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xzc
 * @description 文件读取类
 * @date 2020/10/20
 */
public class FileReaderUtil {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(FileReaderUtil.class);

    /**
     * 打印
     */
    static PrintStream out = System.out;

    /**
     * 按行读取文件，判断文件行是否以xxx开头
     * @param filePath 文件路径
     * @param code xxx编码
     */
    public static void print(String filePath, String code) throws IOException {
        try {
            File file = new File(filePath);
            InputStreamReader input = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(input);
            LOG.info("开始按行读取文件");
            String str;
            int count = 0;
            while ((str = bf.readLine()) != null) {
                if (str.startsWith(code)) {
                    out.println(str);
                    count = count + 1;
                    out.println(count);
                }
            }
            bf.close();
            input.close();
        } catch (IOException e) {
            LOG.error("文件读取失败，当前文件路径为 {}", filePath);
            throw e;
        }
    }

    /**
     * 按行读取文件，返回字符串列表
     * @param filePath 文件路径
     * @return 字符串列表
     */
    public static List<String> getLines(String filePath) {

        List<String> lines = new ArrayList<>();

        try {
            File file = new File(filePath);
            InputStreamReader input = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(input);
            LOG.info("开始按行读取文件");
            String str;
            while ((str = bf.readLine()) != null) {
                lines.add(str);
            }
            bf.close();
            input.close();
        } catch (IOException e) {
            LOG.error("文件读取失败，当前文件路径为 {}", filePath);
        }
        return lines;
    }



    /**
     * 逐个字符读取文件，转换为字符串
     * @param filePath 文件路径
     * @return 文件的字符串内容
     */
    public static String getString(String filePath) throws IOException {
        try {
            File jsonFile = new File(filePath);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = fileReader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            LOG.info("{} 文件读取成功", filePath);
            return sb.toString();
        }
        catch (IOException ioException) {
            LOG.error("文件读取失败，文件路径为 {}", filePath);
            throw ioException;
        }
    }
}
