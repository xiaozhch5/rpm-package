package org.example;

import static org.junit.Assert.assertTrue;

import com.zh.ch.bigdata.base.util.exception.ProjectException;
import com.zh.ch.bigdata.base.util.properties.PropertiesAnalyzeUtil;
import com.zh.ch.bigdata.rpm.constant.POMXMLConfig;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws Exception
    {

        try {
            Properties properties = PropertiesAnalyzeUtil.loadProperties("src/main/resources/config-base.properties", Class.forName("com.zh.ch.bigdata.rpm.constant.POMXMLConfig"));
            MavenXpp3Writer mavenXpp3Writer = new MavenXpp3Writer();
            Model model = new Model();
            model.setModelVersion(properties.getProperty(POMXMLConfig.modelVersion));
            model.setGroupId(properties.getProperty(POMXMLConfig.groupId));
            model.setArtifactId(properties.getProperty(POMXMLConfig.artifactId));
            model.setVersion(properties.getProperty(POMXMLConfig.version));
            model.setName(properties.getProperty(POMXMLConfig.name));
            Writer writer = new FileWriter("src/main/resources/template.xml");
            mavenXpp3Writer.write(writer, model);
        } catch (ProjectException | ClassNotFoundException | IOException e) {
            throw e;
        }

    }
}
