package com.zh.ch.bigdata.rpm.common;

import org.codehaus.plexus.util.xml.Xpp3Dom;

public class RPMXpp3Dom extends Xpp3Dom {
    public RPMXpp3Dom(String name) {
        super(name);
    }

    public RPMXpp3Dom(String name, Object inputLocation) {
        super(name, inputLocation);
    }

    public RPMXpp3Dom(Xpp3Dom src) {
        super(src);
    }

    public RPMXpp3Dom(Xpp3Dom src, String name) {
        super(src, name);
    }

    public void setValue(String value, String defaultValue) {
        if (value == null) {
            setValue(defaultValue);
        }
        else {
            setValue(value);
        }
    }


}
