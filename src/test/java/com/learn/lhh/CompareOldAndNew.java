package com.learn.lhh;

import com.alibaba.fastjson.JSONArray;

import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.learn.lhh.Common.ParseFile;

public class CompareOldAndNew {
    public static Map<String, String> compare(String object, JSONArray oldDataList, JSONArray newDataList) {
        Map<String, String> result = new HashMap<String, String>();

        if (oldDataList == null) {
            result.put("error", "true");
            result.put("msg", "旧接口返回的数据为0，无需比较！");
            return result;
        }
        if (newDataList == null) {
            result.put("error", "true");
            result.put("msg", "新接口返回的数据为0，无需比较！");
            return result;
        }
        if (!(oldDataList.size() == newDataList.size())) {
            result.put("error", "true");
            result.put("msg", "两个接口的返回的数据数量不相等！");
            return result;
        }

        TreeMap<String, Map> oldDataMap = new TreeMap<String, Map>();
        TreeMap<String, Map> newDataMap = new TreeMap<String, Map>();

        //将旧接口返回的数据list转换为<数据id,详情>的map
        for (int i = 0; i < oldDataList.size(); i++) {
            Map oneData = (Map) oldDataList.get(i);
            String okey = (String) oneData.get("ContactID");
            oldDataMap.put(okey, oneData);
            //System.out.println("=============qqqqqqq");
        }
        for (int i = 0; i < newDataList.size(); i++) {
            Map oneData = (Map) newDataList.get(i);
            String nkey = (String) oneData.get("_id");
            newDataMap.put(nkey, oneData);
           // System.out.println("---------------wwwwwwww");
        }
        Set oldDataIds = oldDataMap.keySet();
        Set newDataIds = newDataMap.keySet();
        Iterator ite1 = oldDataIds.iterator();

        if(!newDataIds.containsAll(oldDataIds)){
            result.put("error", "true");
            result.put("msg", "两个接口的返回的数据ID不相同！");
            return result;
        }
        //比较所有数据的各字段值
        int count = 1;
        while(ite1.hasNext()){//每条数据遍历
            System.out.println("开始处理第"+count+"条数据！");
            count++;
            String id = (String)ite1.next();
            Map oldData = oldDataMap.get(id);//根据数据id获取数据的详情
            Map newData = newDataMap.get(id);
            if(oldData!=null && newData!=null){

                Set dataField = oldData.keySet();//各个字段的apiname
                Iterator f1 = dataField.iterator();
                while (f1.hasNext()) {//开始比较
                    String fieldname = (String) f1.next();
                    System.out.println("开始比较的字段为:" + fieldname);
                    if (!fieldname.equals("UDFieldDatas")) {
                        System.out.println("filed的值old：" + fieldname);
                        String newkey = ParseFile.getObjValue(object, fieldname);
                        System.out.println("filed的值new：" + fieldname);
                        if (newkey != null) {
                            System.out.println("oldValue=" + oldData.get(fieldname));
                            System.out.println("newValue=" + newData.get(newkey));
                            if (oldData.get(fieldname) != null && newData.get(newkey) != null) {
                                if (!oldData.get(fieldname).equals(newData.get(newkey))) {
                                    result.put("error", "true");
                                    result.put("msg", "1相同数据出现了不同的值:" + fieldname);
                                    return result;
                                }
                            }
                        } else {
                            System.out.println("oldValue=" + oldData.get(fieldname));
                            System.out.println("newValue=" + newData.get(fieldname));
                            if (oldData.get(fieldname) != null && newData.get(newkey) != null) {
                                if (!oldData.get(fieldname).equals(newData.get(fieldname))) {
                                    result.put("error", "true");
                                    result.put("msg", "2相同数据出现了不同的值:" + fieldname);
                                    return result;
                                }
                            }
                        }

                    }
                }
            }
        }

        return result;
    }

}

