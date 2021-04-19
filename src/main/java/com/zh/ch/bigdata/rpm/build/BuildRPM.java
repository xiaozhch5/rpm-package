package com.zh.ch.bigdata.rpm.build;

import com.zh.ch.bigdata.base.util.json.JsonAnalysisUtil;
import com.zh.ch.bigdata.rpm.common.RPMPOMModel;
import com.zh.ch.bigdata.rpm.constant.RPMPluginParameters;
import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

/**
 * @author xzc
 * @description 解析json描述文件并打出形成maven打包项目模板
 * @date 2021/03/31
 */
public class BuildRPM {

    public static void main(String[] args) throws Exception {

        List<String> argsList = Arrays.asList(args);
        String mappingsFilePath;

        if (argsList.contains("--mappingsFilePath")) {
            mappingsFilePath = argsList.get(argsList.indexOf("--mappingsFilePath") + 1);
        }
        else {
            throw new Exception("请输入映射文件路径参数, --mappingsFilePath xxx");
        }

        File file = new File(mappingsFilePath);
        String content = FileUtils.readFileToString(file, "UTF-8");

        RPMPOMModel rpmpomModel = new RPMPOMModel(mappingsFilePath);
        Model model = rpmpomModel.init();
        MavenXpp3Writer mavenXpp3Writer = new MavenXpp3Writer();
        String targetRpmPath = JsonAnalysisUtil.getString(content, RPMPluginParameters.TARGETRPMPATH);
        String rpmName = JsonAnalysisUtil.getString(content, RPMPluginParameters.RPMNAME);
        String rpmVersion = JsonAnalysisUtil.getString(content, RPMPluginParameters.RPMVERSION);

        long currentTimeMillis = System.currentTimeMillis();
        File directory = new File(targetRpmPath + "/" + rpmName + "-" + rpmVersion);

        if (directory.mkdirs()) {
            Writer writer = new FileWriter(directory + "/" + "pom.xml");
            mavenXpp3Writer.write(writer, model);
        }
        else {
            throw new Exception("无法创建" + directory.getName() + "文件");
        }


    }
}
