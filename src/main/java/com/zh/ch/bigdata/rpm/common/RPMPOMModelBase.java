package com.zh.ch.bigdata.rpm.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zh.ch.bigdata.base.util.exception.ProjectException;
import com.zh.ch.bigdata.base.util.json.JsonAnalysisUtil;
import com.zh.ch.bigdata.base.util.properties.PropertiesAnalyzeUtil;
import com.zh.ch.bigdata.rpm.common.RPMXpp3Dom;
import com.zh.ch.bigdata.rpm.constant.POMXMLConfig;
import com.zh.ch.bigdata.rpm.constant.POMXMLConfigDefaultValue;
import com.zh.ch.bigdata.rpm.constant.RPMPluginConfiguration;
import com.zh.ch.bigdata.rpm.constant.RPMPluginConfigurationDefaultValue;
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
import java.util.List;
import java.util.Properties;

public class RPMPOMModelBase {

    private static final Logger LOG = LoggerFactory.getLogger(RPMPOMModelBase.class);

    private String configBaseFilePath = null;

    private Xpp3Dom rpmConfigurationDom = null;

    private String mappingsFilePath = null;

    private String mappingsString = null;


    public RPMPOMModelBase(String mappingsFilePath) {
        this.mappingsFilePath = mappingsFilePath;

    }

    public RPMPOMModelBase(String configBaseFilePath, String mappingsFilePath) {
        this.configBaseFilePath = configBaseFilePath;
        this.mappingsFilePath = mappingsFilePath;
    }

    public Model init() throws Exception {

        parseMappingsFileToString(mappingsFilePath);

        Model model = new Model();
        if (this.configBaseFilePath == null) {
            this.configBaseFilePath = "src/main/resources/base-config.properties";
        }
        try {
            Properties properties = PropertiesAnalyzeUtil.loadProperties(this.configBaseFilePath, Class.forName("com.zh.ch.bigdata.rpm.constant.POMXMLConfig"));
            model.setModelVersion(properties.getProperty(POMXMLConfig.modelVersion, POMXMLConfigDefaultValue.modelVersionDefaultValue));
            model.setArtifactId(properties.getProperty(POMXMLConfig.artifactId, POMXMLConfigDefaultValue.artifactIdDefaultValue));
            model.setGroupId(properties.getProperty(POMXMLConfig.groupId, POMXMLConfigDefaultValue.groupIdDefaultValue));
            model.setVersion(properties.getProperty(POMXMLConfig.version, POMXMLConfigDefaultValue.versionDefaultValue));
            model.setName(properties.getProperty(POMXMLConfig.name, POMXMLConfigDefaultValue.nameDefaultValue));

            Properties pomXMLProperties = new Properties();
            pomXMLProperties.setProperty(POMXMLConfig.projectBuildSourceEncodingProperties, POMXMLConfigDefaultValue.projectBuildSourceEncodingPropertiesDefaultValue);
            model.setProperties(pomXMLProperties);

            Plugin plugin = new Plugin();
            plugin.setGroupId(properties.getProperty(POMXMLConfig.rpmPluginGroupId, POMXMLConfigDefaultValue.rpmPluginGroupIdDefaultValue));
            plugin.setArtifactId(properties.getProperty(POMXMLConfig.rpmPluginArtifactId, POMXMLConfigDefaultValue.rpmPluginArtifactIdDefaultValue));
            plugin.setVersion(properties.getProperty(POMXMLConfig.rpmPluginVersion, POMXMLConfigDefaultValue.rpmPluginVersionDefaultValue));

            PluginExecution pluginExecution = new PluginExecution();
            pluginExecution.setPhase(properties.getProperty(POMXMLConfig.rpmPluginExecutionPhase, POMXMLConfigDefaultValue.rpmPluginExecutionPhaseDefaultValue));
            pluginExecution.setGoals(new ArrayList<String>(Collections.singleton(properties.getProperty(POMXMLConfig.rpmPluginExecutionPhaseGoal, POMXMLConfigDefaultValue.rpmPluginExecutionPhaseGoalDefaultValue))));
            plugin.setExecutions(new ArrayList<PluginExecution>(Collections.singleton(pluginExecution)));

            setRPMConfiguration(properties);

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


    public void setRPMConfiguration(Properties properties) throws ProjectException, IOException, ClassNotFoundException {
        rpmConfigurationDom = new Xpp3Dom(RPMPluginConfigurationDefaultValue.configuration);
        Xpp3Dom copyrightDom = new Xpp3Dom(RPMPluginConfigurationDefaultValue.copyright);
        Xpp3Dom groupDom = new Xpp3Dom(RPMPluginConfigurationDefaultValue.group);
        Xpp3Dom descriptionDom = new Xpp3Dom(RPMPluginConfigurationDefaultValue.description);
        Xpp3Dom autoRequiresDom = new Xpp3Dom(RPMPluginConfigurationDefaultValue.autoRequires);
        Xpp3Dom prefixDom = new Xpp3Dom(RPMPluginConfigurationDefaultValue.prefix);

        if (properties.getProperty(RPMPluginConfiguration.rpmConfigurationRequire) != null) {
            Xpp3Dom requiresDom = new Xpp3Dom(RPMPluginConfigurationDefaultValue.requires);
            Xpp3Dom requireDom = new Xpp3Dom(RPMPluginConfigurationDefaultValue.require);
            requireDom.setValue(properties.getProperty(RPMPluginConfiguration.rpmConfigurationRequire));
            requiresDom.addChild(requireDom);
            rpmConfigurationDom.addChild(requiresDom);
        }

        copyrightDom.setValue(properties.getProperty(RPMPluginConfiguration.rpmConfigurationCopyright, RPMPluginConfigurationDefaultValue.rpmConfigurationCopyrightDefaultValue));
        groupDom.setValue(properties.getProperty(RPMPluginConfiguration.rpmConfigurationGroup, RPMPluginConfigurationDefaultValue.rpmConfigurationGroupDefaultValue));
        descriptionDom.setValue(properties.getProperty(RPMPluginConfiguration.rpmConfigurationDescription, RPMPluginConfigurationDefaultValue.rpmConfigurationDescriptionDefaultValue));
        autoRequiresDom.setValue(properties.getProperty(RPMPluginConfiguration.rpmConfigurationAutoRequires, RPMPluginConfigurationDefaultValue.rpmConfigurationAutoRequiresDefaultValue));
        prefixDom.setValue(properties.getProperty(RPMPluginConfiguration.rpmConfigurationPrefix, RPMPluginConfigurationDefaultValue.rpmConfigurationPrefixDefaultValue));
        rpmConfigurationDom.addChild(copyrightDom);
        rpmConfigurationDom.addChild(groupDom);
        rpmConfigurationDom.addChild(descriptionDom);
        rpmConfigurationDom.addChild(autoRequiresDom);
        rpmConfigurationDom.addChild(prefixDom);
        rpmConfigurationDom.addChild(getMappingsDom());
        for (RPMXpp3Dom scriptletDom : getScriptletsDom()) {
            rpmConfigurationDom.addChild(scriptletDom);
        }
    }

    private RPMXpp3Dom getMappingsDom() throws ProjectException, ClassNotFoundException {

        RPMXpp3Dom dirMappingsDom = new RPMXpp3Dom("mappings");
        try {
            String originalPath = JsonAnalysisUtil.getString(mappingsString, "originalPath");
            JSONArray dirMappingsJsonArray = JsonAnalysisUtil.getJsonArray(mappingsString, "dirMappings");
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
        } catch (ProjectException | ClassNotFoundException e) {
            LOG.error("mapping文件读取失败", e);
            throw e;
        }
        return dirMappingsDom;
    }

    private List<RPMXpp3Dom> getScriptletsDom() throws ProjectException {
        List<RPMXpp3Dom> rpmXpp3DomList = new ArrayList<>();
        String[] scriptletTypes = {"prepareScriptlet", "preinstallScriptlet", "installScriptlet", "postinstallScriptlet", "preRemoveScriptlet",
                "postRemoveScriptlet", "verifyScriptlet", "cleanScriptlet", "preTransScriptlet", "postTransScriptlet"};

        for (String scriptletType : scriptletTypes) {
            if (JsonAnalysisUtil.getJsonObject(mappingsString, scriptletType) != null) {
                rpmXpp3DomList.add(getScriptLetDom(scriptletType));
            }
        }
        return rpmXpp3DomList;
    }

    private RPMXpp3Dom getScriptLetDom(String scriptletType) throws ProjectException {
        RPMXpp3Dom scriptletDom = new RPMXpp3Dom(scriptletType);
        RPMXpp3Dom scriptFileDom = new RPMXpp3Dom("scriptFile");
        scriptFileDom.setValue(JsonAnalysisUtil.getJsonObject(mappingsString, scriptletType).getString("scriptFile"));
        RPMXpp3Dom fileEncodingDom = new RPMXpp3Dom("fileEncoding");
        String fileEncoding = JsonAnalysisUtil.getJsonObject(mappingsString, scriptletType).getString("fileEncoding");
        fileEncodingDom.setValue(fileEncoding == null ? "UTF-8" : fileEncoding);
        scriptletDom.addChild(scriptFileDom);
        scriptletDom.addChild(fileEncodingDom);
        return scriptletDom;
    }

    public void parseMappingsFileToString(String mappingsFilePath) throws IOException {
        File file = new File(mappingsFilePath);
        this.mappingsString = FileUtils.readFileToString(file, "UTF-8");
    }
}
