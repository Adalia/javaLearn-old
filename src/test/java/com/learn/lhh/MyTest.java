package com.learn.lhh;

import org.testng.annotations.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.learn.lhh.Util.PropertiesUtil;
import com.learn.lhh.Util.ExcelUtilWithXSSF;
import com.learn.lhh.Util.ExcelUtilWithHSSF;

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
        for(Map.Entry<String,String> keyvalue:configMap.entrySet()) {
            System.out.println("key:" + keyvalue.getKey() + ", value:" + keyvalue.getValue());
        }
    }

    @Test(description = "test get cell value")
    public void testGetCellValue(){
        System.out.println("=========start test:get cell value=================");
        //String file = "/data/customObj_online_2.xls";
        String file1 = "/data/customObj_online_1.xlsx";
        //String value = ExcelUtilWithHSSF.getCellValue(file,"List",1,1);
        //System.out.println("取值为:"+value);
        String value1 = ExcelUtilWithXSSF.getCellValue(file1,"List",1,1);
        System.out.println("取值1为:"+value1);
        Map value2 = ExcelUtilWithXSSF.findValueFromExcel(file1,"Post");
        System.out.println("取值1为:"+value2);

    }


}
