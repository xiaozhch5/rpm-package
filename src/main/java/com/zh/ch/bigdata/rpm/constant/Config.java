package com.zh.ch.bigdata.rpm.constant;

import com.zh.ch.bigdata.rpm.domain.DirMapping;
import com.zh.ch.bigdata.rpm.domain.ExecutableScriptLet;
import com.zh.ch.bigdata.rpm.domain.FileMapping;

/**
 * @author hadoop
 */
public class Config {

    private String  originalCompressFilePath;

    private String targetRpmFilePath;

    private DirMapping[] dirMappings;

    private FileMapping[] fileMappings;

    private ExecutableScriptLet[] executableScriptLets;

}
