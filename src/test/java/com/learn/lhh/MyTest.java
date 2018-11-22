package com.learn.lhh;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.*;

import com.learn.lhh.Util.PropertiesUtil;
import com.learn.lhh.Util.ExcelUtilWithXSSF;
import com.learn.lhh.Util.ExcelUtilWithHSSF;
import com.learn.lhh.Util.TxtFileUtil;
import com.learn.lhh.Common.ParseFile;
import com.learn.lhh.Util.JsonUtilWithFastjson;

public class MyTest {


    //2018-11-16_174505.json
//    @Test(description = "测试读取产品的所有场景的中文以及对应的id")
//    public void getFromJson(){
//        String file = "/data/2018-11-16_174505.json";
//        //ArrayList fileArray = TxtFileUtil.readLineToArray(file);
//        //System.out.println(fileArray.size());
//        HashMap<String, String> a = JsonUtilWithFastjson.getFilterMains(file);
//        System.out.println(a);
//    }
    /*
    {我下属参与的=4ac97df9cf98465f827d4b257856c1ea,
    全部产品=e329c679c3ee40ebae734d4fb4491e91,
    共享给我的=35f5ee43e0ce499a81d1322ae7369f63,
    我负责的=e7bcac63098f486ba72fb99da694a646, 下
    架产品=70a19e3797f74011a843a660481323a4,
    上架产品=c291c4b7dce3468c812ac9039e76f851,
    我参与的=3ee6d1d4a2204401ab4b76d8e6498374,
    我下属负责的=98e6fe48db1a41a5a260896a85064c25,
    我负责部门的=16431eee4b834cc488a1073551105b2b}
     */

    @Test(description = "测试将txt按行放入arrayList")
    public void compareOldAndNew(){
        //String value = ParseFile.getObjValue("ContactObj","YearOfBirth");
       // System.out.println("==========="+value);//year_of_birth
        String oldRes = "/data/oldInterface.json";
        String newRes =  "/data/newInterface.json";
        JSONArray oldDataList = JsonUtilWithFastjson.getJsonObjFromFile(oldRes).getJSONObject("Value").getJSONArray("ContactInfos");
        JSONArray newDataList = JsonUtilWithFastjson.getJsonObjFromFile(newRes).getJSONObject("Value").getJSONArray("dataList");
        //调用比较方法——待实现
        System.out.println(oldDataList);
        Map result = CompareOldAndNew.compare("ContactObj",oldDataList,newDataList);
        System.out.println(result);
//        List list = new ArrayList();
//        list.add("1001");
//        list.add(1);
//        String aa= CompareOldAndNew.objectToString(list);
//        System.out.println(aa);


    }

//    @Test(description = "getAllProperties")
//    public void getAllProperties(){
//        System.out.println("=========start test:getAllProperties=================");
//        String a = PropertiesUtil.getValue("key1");
//        System.out.println("===================="+a);
//        String filepath = "/config/config.properties";
//        HashMap<String,String> configMap = new HashMap<String,String>();
//        try {
//            configMap = PropertiesUtil.getAllProperties(filepath);
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("first iteration method，all config is:");
//        for(String key:configMap.keySet()) {
//            System.out.println("key:" + key + ", value:" + configMap.get(key));
//        }
//        System.out.println("second iteration method，all config is:");
//        for(Map.Entry<String,String> keyvalue:configMap.entrySet()) {
//            System.out.println("key:" + keyvalue.getKey() + ", value:" + keyvalue.getValue());
//        }
//    }
//
//    @Test(description = "test get cell value")
//    public void testGetCellValue(){
//        System.out.println("=========start test:get cell value=================");
//        //String file = "/data/customObj_online_2.xls";
//        String file1 = "/data/customObj_online_1.xlsx";
//        //String value = ExcelUtilWithHSSF.getCellValue(file,"List",1,1);
//        //System.out.println("取值为:"+value);
//        String value1 = ExcelUtilWithXSSF.getCellValue(file1,"List",1,1);
//        System.out.println("取值1为:"+value1);
//        Map value2 = ExcelUtilWithXSSF.findValueFromExcel(file1,"Post");
//        System.out.println("取值1为:"+value2);
//
//    }


}
