package com.zh.ch.bigdata.rpm.build;

import junit.framework.TestCase;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

import java.io.FileWriter;
import java.io.Writer;

public class RPMMavenModelBaseTest extends TestCase {

    public void testInit() throws Exception {

        RPMMavenModelBase rpmMavenModelBase = new RPMMavenModelBase("src/main/resources/example.json");
        Model model = rpmMavenModelBase.init();
        MavenXpp3Writer mavenXpp3Writer = new MavenXpp3Writer();
        Writer writer = new FileWriter("src/main/resources/template1.xml");
        mavenXpp3Writer.write(writer, model);
    }
}