package com.learn.lhh.common;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;


public class PropertiesUtil {

    private static String proFileName = "/config/config.properties";
    private static Properties pro;

    static{
        try {
            pro = new Properties();
            InputStream in = ClassLoader.class.getResourceAsStream(proFileName);
            pro.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key){
        String value = pro.getProperty(key);
        return value;
    }
}
