package com.learn.lhh;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyTest {
    @Test(description = "getAllProperties")
    public void getAllProperties(){
        System.out.println("=========start test:getAllProperties=================");
        String a = PropertiesUtil.getValue("key1");
        System.out.println("===================="+a);
        String filepath = "/config/config.properties";
        HashMap<String,String> configMap = new HashMap<String,String>();
        try {
            configMap = PropertiesUtil.getAllProperties(filepath);
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("first iteration method，all config is:");
        for(String key:configMap.keySet()) {
            System.out.println("key:" + key + ", value:" + configMap.get(key));
        }
        System.out.println("second iteration method，all config is:");
        for(Map.Entry<String,String> keyvalue:configMap.entrySet()){
            System.out.println("key:"+keyvalue.getKey()+", value:"+keyvalue.getValue());
        }


    }


}
