package com.zh.ch.bigdata.base.util.java;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xzc
 * @description 获取接口或者抽象类所有的实现
 * @date 2021/02/04
 */
public class SubClassUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SubClassUtil.class);

    /**
     * 在某个目录下扫描某个接口或者抽象类的所有实现类
     *
     * @param clazz 全路径类名
     * @param scannerDir 扫描路径
     * @return 实现类集合
     * @throws ClassNotFoundException 异常
     */
    public static List getFullImplClassNames(String clazz, String scannerDir)
            throws ClassNotFoundException {
        try {
            Class className = Class.forName(clazz);
            Reflections reflections = new Reflections(scannerDir);
            return new ArrayList<>(reflections.getSubTypesOf(className));
        } catch (ClassNotFoundException e) {
            LOG.error("在{}目录下获取{}类的所有子类失败", scannerDir, clazz, e);
            throw e;
        }
    }

    /**
     * 在全目录下扫描某个接口或者抽象类的所有实现类
     *
     * @param clazz 全路径类名
     * @return 实现类集合
     * @throws ClassNotFoundException 异常
     */
    public static List getFullImplClassNames(String clazz) throws ClassNotFoundException {
        try {
            Class className = Class.forName(clazz);
            Reflections reflections = new Reflections();
            return new ArrayList<>(reflections.getSubTypesOf(className));
        } catch (ClassNotFoundException e) {
            LOG.error("在全目录下获取{}类的所有子类失败", clazz, e);
            throw e;
        }
    }
}
