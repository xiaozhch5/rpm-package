package com.zh.ch.bigdata.rpm.build;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zh.ch.bigdata.base.util.exception.ProjectException;
import com.zh.ch.bigdata.base.util.json.JsonAnalysisUtil;
import com.zh.ch.bigdata.base.util.properties.PropertiesAnalyzeUtil;
import com.zh.ch.bigdata.rpm.common.RPMXpp3Dom;
import com.zh.ch.bigdata.rpm.constant.POMXMLConfig;
import com.zh.ch.bigdata.rpm.constant.POMXMLConfigDefaultValue;
import com.zh.ch.bigdata.rpm.constant.RPMConfiguration;
import com.zh.ch.bigdata.rpm.constant.RPMConfigurationDefaultValue;
import com.zh.ch.bigdata.rpm.domain.DirMapping;
import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class RPMMavenModelBase {

    private static final Logger LOG = LoggerFactory.getLogger(RPMMavenModelBase.class);

    private String configBaseFilePath = null;

    private Xpp3Dom rpmConfigurationDom = null;

    private String mappingsFilePath = null;


    public RPMMavenModelBase(String mappingsFilePath) {
        this.mappingsFilePath = mappingsFilePath;

    }

    public RPMMavenModelBase(String configBaseFilePath, String mappingsFilePath) {
        this.configBaseFilePath = configBaseFilePath;
    }

    public Model init() throws Exception {
        Model model = new Model();
        if (this.configBaseFilePath == null) {
            this.configBaseFilePath = "src/main/resources/config-base.properties";
        }
        try {
            Properties properties = PropertiesAnalyzeUtil.loadProperties(this.configBaseFilePath, Class.forName("com.zh.ch.bigdata.rpm.constant.POMXMLConfig"));
            model.setModelVersion(properties.getProperty(POMXMLConfig.modelVersion, POMXMLConfigDefaultValue.modelVersionDefaultValue));
            model.setArtifactId(properties.getProperty(POMXMLConfig.artifactId, POMXMLConfigDefaultValue.artifactIdDefaultValue));
            model.setGroupId(properties.getProperty(POMXMLConfig.groupId, POMXMLConfigDefaultValue.groupIdDefaultValue));
            model.setVersion(properties.getProperty(POMXMLConfig.version, POMXMLConfigDefaultValue.versionDefaultValue));
            model.setName(properties.getProperty(POMXMLConfig.name, POMXMLConfigDefaultValue.nameDefaultValue));

            Plugin plugin = new Plugin();
            plugin.setGroupId(properties.getProperty(POMXMLConfig.rpmPluginGroupId, POMXMLConfigDefaultValue.rpmPluginGroupIdDefaultValue));
            plugin.setArtifactId(properties.getProperty(POMXMLConfig.rpmPluginArtifactId, POMXMLConfigDefaultValue.rpmPluginArtifactIdDefaultValue));
            plugin.setVersion(properties.getProperty(POMXMLConfig.rpmPluginVersion, POMXMLConfigDefaultValue.rpmPluginVersionDefaultValue));

            PluginExecution pluginExecution = new PluginExecution();
            pluginExecution.setPhase(properties.getProperty(POMXMLConfig.rpmPluginExecutionPhase, POMXMLConfigDefaultValue.rpmPluginExecutionPhaseDefaultValue));
            pluginExecution.setGoals(new ArrayList<String>(Collections.singleton(properties.getProperty(POMXMLConfig.rpmPluginExecutionPhaseGoal, POMXMLConfigDefaultValue.rpmPluginExecutionPhaseGoalDefaultValue))));
            plugin.setExecutions(new ArrayList<PluginExecution>(Collections.singleton(pluginExecution)));

            setRPMConfiguration(properties, this.mappingsFilePath);

            plugin.setConfiguration(rpmConfigurationDom);

            Build build = new Build();
            build.setPlugins(new ArrayList<Plugin>(Collections.singleton(plugin)));

            model.setBuild(build);
        } catch (ClassNotFoundException | IOException | ProjectException e) {
            LOG.error("pom文件生成异常", e);
            throw e;
        }
        return model;

    }



    public void setRPMConfiguration(Properties properties, String mappingsFilePath) throws ProjectException, IOException, ClassNotFoundException {
        rpmConfigurationDom = new Xpp3Dom(RPMConfigurationDefaultValue.configuration);
        Xpp3Dom copyrightDom = new Xpp3Dom(RPMConfigurationDefaultValue.copyright);
        Xpp3Dom groupDom = new Xpp3Dom(RPMConfigurationDefaultValue.group);
        Xpp3Dom descriptionDom = new Xpp3Dom(RPMConfigurationDefaultValue.description);
        Xpp3Dom autoRequiresDom = new Xpp3Dom(RPMConfigurationDefaultValue.autoRequires);
        Xpp3Dom prefixDom = new Xpp3Dom(RPMConfigurationDefaultValue.prefix);
        Xpp3Dom requiresDom = new Xpp3Dom(RPMConfigurationDefaultValue.requires);
        copyrightDom.setValue(properties.getProperty(RPMConfiguration.rpmConfigurationCopyright, RPMConfigurationDefaultValue.rpmConfigurationCopyrightDefaultValue));
        groupDom.setValue(properties.getProperty(RPMConfiguration.rpmConfigurationGroup, RPMConfigurationDefaultValue.rpmConfigurationGroupDefaultValue));
        descriptionDom.setValue(properties.getProperty(RPMConfiguration.rpmConfigurationDescription, RPMConfigurationDefaultValue.rpmConfigurationDescriptionDefaultValue));
        autoRequiresDom.setValue(properties.getProperty(RPMConfiguration.rpmConfigurationAutoRequires, RPMConfigurationDefaultValue.rpmConfigurationAutoRequiresDefaultValue));
        prefixDom.setValue(properties.getProperty(RPMConfiguration.rpmConfigurationPrefix, RPMConfigurationDefaultValue.rpmConfigurationPrefixDefaultValue));
        requiresDom.setValue(properties.getProperty(RPMConfiguration.rpmConfigurationRequires, RPMConfigurationDefaultValue.rpmConfigurationRequiresDefaultValue));
        rpmConfigurationDom.addChild(copyrightDom);
        rpmConfigurationDom.addChild(groupDom);
        rpmConfigurationDom.addChild(descriptionDom);
        rpmConfigurationDom.addChild(autoRequiresDom);
        rpmConfigurationDom.addChild(prefixDom);
        rpmConfigurationDom.addChild(requiresDom);
        rpmConfigurationDom.addChild(getMappingsDom(mappingsFilePath));
    }

    private RPMXpp3Dom getMappingsDom(String mappingsFilePath) throws ProjectException, IOException, ClassNotFoundException {

        RPMXpp3Dom dirMappingsDom = new RPMXpp3Dom("mappings");
        try {
            File file = new File(mappingsFilePath);
            String content = FileUtils.readFileToString(file, "UTF-8");

            String originalPath = JsonAnalysisUtil.getString(content, "originalPath");

            JSONArray dirMappingsJsonArray = JsonAnalysisUtil.getJsonArray(content, "dirMappings");
            for (Object dirMappingObject : dirMappingsJsonArray) {
                DirMapping dirMapping = (DirMapping) JSONObject.toJavaObject((JSONObject) dirMappingObject, Class.forName("com.zh.ch.bigdata.rpm.domain.DirMapping"));
                RPMXpp3Dom dirMappingDom = new RPMXpp3Dom("mapping");
                RPMXpp3Dom directoryDom = new RPMXpp3Dom("directory");
                directoryDom.setValue(dirMapping.getTo());
                RPMXpp3Dom usernameDom = new RPMXpp3Dom("username");
                usernameDom.setValue(dirMapping.getUserName(), "root");
                RPMXpp3Dom groupNameDom = new RPMXpp3Dom("groupname");
                groupNameDom.setValue(dirMapping.getGroupName(), "root");
                if (dirMapping.getDirectoryIncluded() != null) {
                    RPMXpp3Dom directoryIncludedDom = new RPMXpp3Dom("directoryIncluded");
                    directoryIncludedDom.setValue(dirMapping.getDirectoryIncluded());
                    dirMappingDom.addChild(directoryIncludedDom);
                }
                RPMXpp3Dom sourcesDom = new RPMXpp3Dom("sources");
                RPMXpp3Dom sourceDom = new RPMXpp3Dom("source");
                RPMXpp3Dom locationDom = new RPMXpp3Dom("location");
                locationDom.setValue(originalPath + dirMapping.getFrom());
                sourceDom.addChild(locationDom);
                sourcesDom.addChild(sourceDom);

                dirMappingDom.addChild(directoryDom);
                dirMappingDom.addChild(usernameDom);
                dirMappingDom.addChild(groupNameDom);
                dirMappingDom.addChild(sourcesDom);

                dirMappingsDom.addChild(dirMappingDom);
            }
        } catch (IOException | ProjectException | ClassNotFoundException e) {
            LOG.error("mapping文件读取失败", e);
            throw e;
        }

        return dirMappingsDom;
    }

}
