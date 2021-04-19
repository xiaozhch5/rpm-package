package com.zh.ch.bigdata.rpm.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zh.ch.bigdata.base.util.exception.ProjectException;
import com.zh.ch.bigdata.base.util.json.JsonAnalysisUtil;
import com.zh.ch.bigdata.base.util.properties.PropertiesAnalyzeUtil;
import com.zh.ch.bigdata.rpm.constant.*;
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
import java.util.*;

import static com.zh.ch.bigdata.rpm.constant.RPMPluginParameters.LOCATION;

public class RPMPOMModelBase {

    private static final Logger LOG = LoggerFactory.getLogger(RPMPOMModelBase.class);

    private String mappingsFilePath = null;

    private String mappingsString = null;


    public RPMPOMModelBase(String mappingsFilePath) {
        this.mappingsFilePath = mappingsFilePath;

    }

    public RPMPOMModelBase() {
    }

    public Model init() throws Exception {

        parseMappingsFileToString(mappingsFilePath);

        Model model = new Model();
        try {
            model.setModelVersion(BaseConfigParametersDefaultValue.modelVersionDefaultValue);
            model.setArtifactId(BaseConfigParametersDefaultValue.artifactIdDefaultValue);
            model.setGroupId(BaseConfigParametersDefaultValue.groupIdDefaultValue);
            model.setVersion(BaseConfigParametersDefaultValue.versionDefaultValue);
            model.setName(BaseConfigParametersDefaultValue.nameDefaultValue);
            Properties pomXMLProperties = new Properties();
            pomXMLProperties.setProperty("project.build.sourceEncoding",
                    BaseConfigParametersDefaultValue.projectBuildSourceEncodingPropertiesDefaultValue);
            model.setProperties(pomXMLProperties);
            Plugin rpmPlugin = getRPMPlugin();
            Build build = new Build();
            build.setPlugins(new ArrayList<Plugin>(Collections.singleton(rpmPlugin)));
            model.setBuild(build);
        } catch (ClassNotFoundException | ProjectException e) {
            LOG.error("pom文件生成异常", e);
            throw e;
        }
        return model;
    }

    public Plugin getRPMPlugin() throws ProjectException, ClassNotFoundException {
        Plugin rpmPlugin = new Plugin();
        rpmPlugin.setGroupId(BaseConfigParametersDefaultValue.rpmPluginGroupIdDefaultValue);
        rpmPlugin.setArtifactId(BaseConfigParametersDefaultValue.rpmPluginArtifactIdDefaultValue);
        rpmPlugin.setVersion(BaseConfigParametersDefaultValue.rpmPluginVersionDefaultValue);
        PluginExecution pluginExecution = new PluginExecution();
        pluginExecution.setPhase(BaseConfigParametersDefaultValue.rpmPluginExecutionPhaseDefaultValue);
        pluginExecution.setGoals(new ArrayList<>(Collections.singleton(BaseConfigParametersDefaultValue.rpmPluginExecutionPhaseGoalDefaultValue)));
        rpmPlugin.setExecutions(new ArrayList<>(Collections.singleton(pluginExecution)));
        Xpp3Dom rpmPluginConfigurationDom = getRPMPluginConfiguration();
        rpmPlugin.setConfiguration(rpmPluginConfigurationDom);
        return rpmPlugin;
    }


    public Xpp3Dom getRPMPluginConfiguration() throws ProjectException, ClassNotFoundException {
        Xpp3Dom rpmConfigurationDom = new Xpp3Dom(RPMPluginParameters.CONFIGURATION);
        Xpp3Dom nameDom = new Xpp3Dom(RPMPluginParameters.NAME);
        Xpp3Dom versionDom = new Xpp3Dom(RPMPluginParameters.VERSION);
        Xpp3Dom needarchDom = new Xpp3Dom(RPMPluginParameters.NEEDARCH);
        Xpp3Dom licenseDom = new Xpp3Dom(RPMPluginParameters.LICENSE);
        Xpp3Dom groupDom = new Xpp3Dom(RPMPluginParameters.GROUP);
        Xpp3Dom descriptionDom = new Xpp3Dom(RPMPluginParameters.DESCRIPTION);
        Xpp3Dom autoRequiresDom = new Xpp3Dom(RPMPluginParameters.AUTOREQUIRES);
        Xpp3Dom prefixDom = new Xpp3Dom(RPMPluginParameters.PREFIX);
        if (JsonAnalysisUtil.getString(this.mappingsString, RPMPluginParameters.REQUIRE) != null) {
            Xpp3Dom requiresDom = new Xpp3Dom(RPMPluginParameters.REQUIRES);
            Xpp3Dom requireDom = new Xpp3Dom(RPMPluginParameters.REQUIRE);
            requireDom.setValue(JsonAnalysisUtil.getString(this.mappingsString, RPMPluginParameters.REQUIRE));
            requiresDom.addChild(requireDom);
            rpmConfigurationDom.addChild(requiresDom);
        }
        nameDom.setValue(JsonAnalysisUtil.getString(this.mappingsString, RPMPluginParameters.RPMNAME));
        versionDom.setValue(JsonAnalysisUtil.getString(this.mappingsString, RPMPluginParameters.RPMVERSION));
        needarchDom.setValue(JsonAnalysisUtil.getString(this.mappingsString, RPMPluginParameters.RPMNEEDARCH));
        licenseDom.setValue(RPMPluginParametersDefaultValue.LICENSEDEFAULTVALUE);
        groupDom.setValue(RPMPluginParametersDefaultValue.GROUPDEFAULTVALUE);
        descriptionDom.setValue(RPMPluginParametersDefaultValue.DESCRIPTIONDEFAULTVALUE);
        autoRequiresDom.setValue(RPMPluginParametersDefaultValue.AUTOREQUIRESDEFAULTVALUE);
        prefixDom.setValue(RPMPluginParametersDefaultValue.PREFIXDEFAULTVALUE);
        rpmConfigurationDom.addChild(nameDom);
        rpmConfigurationDom.addChild(versionDom);
        rpmConfigurationDom.addChild(needarchDom);
        rpmConfigurationDom.addChild(licenseDom);
        rpmConfigurationDom.addChild(groupDom);
        rpmConfigurationDom.addChild(descriptionDom);
        rpmConfigurationDom.addChild(autoRequiresDom);
        rpmConfigurationDom.addChild(prefixDom);
        rpmConfigurationDom.addChild(getMappingsDom());
        for (RPMXpp3Dom scriptletDom : getScriptletsDom()) {
            rpmConfigurationDom.addChild(scriptletDom);
        }
        return rpmConfigurationDom;
    }

    private RPMXpp3Dom getMappingsDom() throws ProjectException, ClassNotFoundException {

        RPMXpp3Dom dirMappingsDom = new RPMXpp3Dom(RPMPluginParameters.MAPPINGS);
        try {
            String originalPath = JsonAnalysisUtil.getString(mappingsString, RPMPluginParameters.ORIGINALPATH);
            JSONArray dirMappingsJsonArray = JsonAnalysisUtil.getJsonArray(mappingsString, RPMPluginParameters.DIRMAPPINGS);
            for (Object dirMappingObject : dirMappingsJsonArray) {
                DirMapping dirMapping = (DirMapping) JSONObject.toJavaObject((JSONObject) dirMappingObject, Class.forName("com.zh.ch.bigdata.rpm.domain.DirMapping"));
                RPMXpp3Dom dirMappingDom = new RPMXpp3Dom(RPMPluginParameters.MAPPING);
                RPMXpp3Dom directoryDom = new RPMXpp3Dom(RPMPluginParameters.DIRECTORY);
                directoryDom.setValue(dirMapping.getTo());
                RPMXpp3Dom usernameDom = new RPMXpp3Dom(RPMPluginParameters.USERNAME);
                usernameDom.setValue(dirMapping.getUserName(), RPMPluginParametersDefaultValue.USERNAMEDEFAULTVALUE);
                RPMXpp3Dom groupNameDom = new RPMXpp3Dom(RPMPluginParameters.GROUPNAME);
                groupNameDom.setValue(dirMapping.getGroupName(), RPMPluginParametersDefaultValue.GROUPNAMEDEFAULTVALUE);
                if (dirMapping.getDirectoryIncluded() != null) {
                    RPMXpp3Dom directoryIncludedDom = new RPMXpp3Dom(RPMPluginParameters.DIRECTORYINCLUDED);
                    directoryIncludedDom.setValue(dirMapping.getDirectoryIncluded());
                    dirMappingDom.addChild(directoryIncludedDom);
                }
                RPMXpp3Dom sourcesDom = new RPMXpp3Dom(RPMPluginParameters.SOURCES);
                RPMXpp3Dom sourceDom = new RPMXpp3Dom(RPMPluginParameters.SOURCE);
                RPMXpp3Dom locationDom = new RPMXpp3Dom(LOCATION);
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
        String[] scriptletTypes = {
                "prepareScriptlet",
                "preinstallScriptlet",
                "installScriptlet",
                "postinstallScriptlet",
                "preremoveScriptlet",
                "postremoveScriptlet",
                "verifyScriptlet",
                "cleanScriptlet",
                "pretransScriptlet",
                "posttransScriptlet"
        };
        for (String scriptletType : scriptletTypes) {
            if (JsonAnalysisUtil.getJsonObject(mappingsString, scriptletType) != null) {
                rpmXpp3DomList.add(getScriptLetDom(scriptletType));
            }
        }
        return rpmXpp3DomList;
    }

    private RPMXpp3Dom getScriptLetDom(String scriptletType) throws ProjectException {
        RPMXpp3Dom scriptletDom = new RPMXpp3Dom(scriptletType);
        RPMXpp3Dom scriptFileDom = new RPMXpp3Dom(RPMPluginParameters.SCRIPTFILE);
        scriptFileDom.setValue(Objects.requireNonNull(JsonAnalysisUtil.getJsonObject(mappingsString, scriptletType)).getString(RPMPluginParameters.SCRIPTFILE));
        RPMXpp3Dom fileEncodingDom = new RPMXpp3Dom(RPMPluginParameters.FILEENCODING);
        String fileEncoding = Objects.requireNonNull(JsonAnalysisUtil.getJsonObject(mappingsString, scriptletType)).getString(RPMPluginParameters.SCRIPTFILE);
        fileEncodingDom.setValue(fileEncoding == null ? RPMPluginParametersDefaultValue.FILEENCODINGDEFAULTVALUE : fileEncoding);
        scriptletDom.addChild(scriptFileDom);
        scriptletDom.addChild(fileEncodingDom);
        return scriptletDom;
    }

    public void parseMappingsFileToString(String mappingsFilePath) throws IOException {
        File file = new File(mappingsFilePath);
        this.mappingsString = FileUtils.readFileToString(file, "UTF-8");
    }
}
