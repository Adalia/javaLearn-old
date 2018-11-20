package com.learn.lhh.Common;
import com.learn.lhh.Util.TxtFileUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class ParseFile {
    public static HashMap<String,HashMap<String, String>> convertRelationList = new HashMap<String,HashMap<String, String>> ();
    public static String file = "/data/transferConfig.txt";
    public static HashMap<String,HashMap<String, String>> parseTransferConfig(String file){
        //HashMap<String,HashMap<String, String>> convertRelationList = new HashMap<String,HashMap<String, String>> ();
        ArrayList<String> fileLines = TxtFileUtil.readLineToArray(file);
        if(fileLines.size()>0){
            for(int i=0;i<fileLines.size();i++){
                //System.out.println("开始处理第"+(i+1)+"行");
                //获取每一行
                String line = fileLines.get(i);
                //用"="分隔，得到map的key
                String key = line.split("=")[0];
                //value经过处理后将得到内层map
                String value = line.split("=")[1];
                HashMap<String, String> oneObjectRelationList = new HashMap<String,String>();
                if(!value.equals(null)|| !value.equals("")){
                    //用"，"分隔，得到内层map的键值对list
                    String[] list = value.split(",");
                    //System.out.println("一共有"+list.length+"个键值对");
                    //遍历对象下存在的转换关系
                    for(int j=0;j<list.length;j++) {
                        if(list[j].contains(":")) {
                            String innerKey = list[j].split(":")[0];
                            String innerValue = list[j].split(":")[1];
                           // System.out.println("innerkey:" + innerKey + ", innervalue:" + innerValue);
                            oneObjectRelationList.put(innerKey, innerValue);
                        }else {
                            //如果不是冒号形式的，就用j做key
                            String innerValue = list[j];
                            oneObjectRelationList.put(String.valueOf(j), innerValue);
                        }
                    }
                    convertRelationList.put(key,oneObjectRelationList);

                }

            }
        }
        return convertRelationList;
    }
    public static String getObjValue(String obj,String oldName){
        parseTransferConfig(file);
        HashMap<String, String> allObjValue = convertRelationList.get(obj);
        String objvalue = allObjValue.get(oldName);
        return objvalue;
    }
}
