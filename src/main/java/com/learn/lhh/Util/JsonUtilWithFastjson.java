package com.learn.lhh.Util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import org.apache.commons.io.IOUtils;
import java.util.HashMap;

public class JsonUtilWithFastjson {
    /*
    读取附件的文件，读取产品的所有场景的中文以及对应的id, 存储为hashmap 结构：
    HashMap<String, String> filterMains  =  new HashMap<>();
     */
    public static HashMap<String, String> getFilterMains(String file){
        HashMap<String, String> filterMains  =  new HashMap<String, String>();
        InputStream in = ClassLoader.class.getResourceAsStream(file);
        String text = "";
        try {
            text = IOUtils.toString(in, "utf8");
        }catch (IOException e){
            System.out.println(e);
        }
        text = text.replaceAll("(\r\n|\r|\n|\n\r)", "");
        System.out.println("json文件字符串"+text);
        JSONObject jsonObject = JSONObject.parseObject(text);
        JSONArray tableDetails = jsonObject.getJSONObject("Value").getJSONArray("FilterMains");

        System.out.println("json文件中templates的array"+tableDetails);

        if(tableDetails.size()>0){
            for(int i=0;i<tableDetails.size();i++){
                JSONObject scene = tableDetails.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                filterMains.put(scene.getString("FilterName"),scene.getString("FilterMainID"));
            }
        }
        return filterMains;

    }
    public static JSONObject getJsonObjFromFile(String file){
        InputStream in = ClassLoader.class.getResourceAsStream(file);
        String text = "";
        try {
            text = IOUtils.toString(in, "utf8");
        }catch (IOException e){
            System.out.println(e);
        }
        text = text.replaceAll("(\r\n|\r|\n|\n\r)", "");
        System.out.println("json文件字符串"+text);
        JSONObject result = JSONObject.parseObject(text);
        return result;
    }



}
