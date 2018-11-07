package com.learn.lhh;
import java.io.*;
import java.util.*;

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
    //根据Key读取Value
    public static String getValueByKey(String filePath, String key) {
        Properties pps = new Properties();
        try {
            //InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            InputStream in = ClassLoader.class.getResourceAsStream(filePath);
            pps.load(in);
            String value = pps.getProperty(key);
            System.out.println(key + " = " + value);
            return value;

        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //读取Properties的全部信息
    public static HashMap getAllProperties(String filePath) throws IOException {
        Properties pps = new Properties();
       // InputStream in = new BufferedInputStream(new FileInputStream(filePath));
        InputStream in = ClassLoader.class.getResourceAsStream(filePath);
        pps.load(in);
        Enumeration en = pps.propertyNames(); //得到配置文件的名字
        System.out.println("en:"+en);
        HashMap configMap = new HashMap();
        while(en.hasMoreElements()) {
            String strKey = (String) en.nextElement();
            String strValue = pps.getProperty(strKey);
            configMap.put(strKey,strValue);
            System.out.println(strKey + "=" + strValue);
        }
        return configMap;

    }
    //写入Properties信息
    public static void writeProperties (String filePath, String pKey, String pValue) throws IOException {
        Properties pps = new Properties();
        InputStream in = new FileInputStream(filePath);
        //从输入流中读取属性列表（键和元素对）
        pps.load(in);
        //调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
        //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
        OutputStream out = new FileOutputStream(filePath);
        pps.setProperty(pKey, pValue);
        //以适合使用 load 方法加载到 Properties 表中的格式，
        //将此 Properties 表中的属性列表（键和元素对）写入输出流
        pps.store(out, "Update " + pKey + " name");//"Update " + pKey + " name” 是注释信息
    }
}
