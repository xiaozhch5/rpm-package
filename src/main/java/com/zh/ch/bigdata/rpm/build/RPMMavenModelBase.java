package com.zh.ch.bigdata.rpm.build;

import org.apache.maven.model.Model;

public class RPMMavenModelBase {

    private Model model = null;

    private String configBaseFilePath = null;



    public void init() {
        this.model = new Model();
        if (this.configBaseFilePath == null) {
            this.configBaseFilePath = "src/main/resources/config-base.properties";
        }
    }

    public RPMMavenModelBase() {
    }

    public RPMMavenModelBase(String configBaseFilePath) {
        this.configBaseFilePath = configBaseFilePath;
    }


}
