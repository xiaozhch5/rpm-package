package com.zh.ch.bigdata.base.util.properties;

import com.zh.ch.bigdata.base.util.exception.ProjectException;
import com.zh.ch.bigdata.base.util.exception.PropertiesFileKeyNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author xzc
 * @description properties文件解析工具
 * @date 2020/11/20
 */
public class PropertiesAnalyzeUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesAnalyzeUtil.class);

    /**
     * 获取Properties中的key值
     *
     * @param propertiesFilePath properties的文件路径
     * @param key 待获取的key
     * @return keyValue
     * @throws IOException 异常
     * @throws ProjectException 异常
     */
    public static String getProperty(String propertiesFilePath, String key)
            throws IOException, ProjectException {
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(propertiesFilePath);
            properties.load(inputStream);
            String keyValue = properties.getProperty(key);
            if (keyValue == null) {
                throw new PropertiesFileKeyNotFoundException(key);
            } else {
                return keyValue;
            }
        } catch (FileNotFoundException e) {
            LOG.error("找不到{}配置文件,请检查该文件路径", propertiesFilePath, e);
            throw e;
        }
    }

    public static Properties loadProperties(String propertiesFilePath, Class configClass)
            throws IOException, ProjectException {
        String propName = null;
        Properties properties = new Properties();
        try {
            Field[] fields = configClass.getFields();
            for (Field field : fields) {
                propName = field.getName();
                properties.setProperty(propName, getProperty(propertiesFilePath, propName));
            }
        } catch (ProjectException | IOException e) {
            throw e;
        }
        return properties;
    }

    /**
     * 功能与getProperty类似，只是不处理获取不到key的情况
     *
     * @param propertiesFilePath properties配置文件路径
     * @param key 待获取的key值
     * @return 返回值
     * @throws IOException 异常
     */
    public static String getPropertyWithoutProcessingException(
            String propertiesFilePath, String key) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream(propertiesFilePath);
        properties.load(inputStream);
        return properties.getProperty(key);
    }
}
