package com.zh.ch.bigdata.base.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * @author xzc
 * @description 生成固定数据量的文件
 * @date 2021/01/04
 */
public class GenFileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenFileUtil.class);

    /**
     * 生成固定数据量的文件
     *
     * @param filePath 文件保存路径
     * @param size 文件大小
     * @throws IOException
     */
    public static void doGenerate(String filePath, Long size) throws IOException {

        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
        BufferedWriter out = new BufferedWriter(osw);
        Random r = new Random();

        long i = 0L;
        while (i < size) {
            i++;
            for (int j = 0; j < 100; j++) {
                out.write(Integer.toString(r.nextInt(10)));
            }
            out.newLine();
            if (i % 100000 == 0) {
                out.flush();
            }
        }
        out.close();

        LOGGER.info("数据生成到 {},数据大小为 {}", file, size);
    }

    public static void main(String[] args) throws Exception {
        doGenerate(args[0], Long.valueOf(args[1]));
    }
}
