package com.learn.lhh;

import com.alibaba.fastjson.JSONArray;

import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.learn.lhh.Common.ParseFile;

public class CompareOldAndNew {
    public static Map<String, Object> compare(String object, JSONArray oldDataList, JSONArray newDataList) {
        Map<String, Object> result = new HashMap<String, Object>();

        TreeMap<String, Map> oldDataMap = new TreeMap<String, Map>();
        TreeMap<String, Map> newDataMap = new TreeMap<String, Map>();

        //将旧接口返回的数据list转换为<数据id,详情>的map
        for (int i = 0; i < oldDataList.size(); i++) {
            Map oneData = (Map) oldDataList.get(i);
            String okey = (String) oneData.get("ContactID");
            oldDataMap.put(okey, oneData);
        }
        //将新接口返回的数据list转换为<数据id,详情>的map
        for (int i = 0; i < newDataList.size(); i++) {
            Map oneData = (Map) newDataList.get(i);
            String nkey = (String) oneData.get("_id");
            newDataMap.put(nkey, oneData);
        }
        //获取新旧接口所有的数据ID
        HashSet oldDataIds = new HashSet(oldDataMap.keySet());
        HashSet newDataIds = new HashSet(newDataMap.keySet());

        //找到新旧接口的数据ID的交集，以及旧接口返回新接口没返回的数据，以及新接口返回旧接口没返回的数据id
        Map<Integer, HashSet<String>> findDiff = findListDiff(oldDataIds,newDataIds);
        HashSet oldOnly = new HashSet<String>(findDiff.get(0));
        HashSet newOnly = new HashSet<String>(findDiff.get(2));
        HashSet bothDataIds = new HashSet<String>(findDiff.get(1));
        System.out.println("===*****==="+findDiff);
        if (oldDataList == null) {
            result.put("error", "true");
            result.put("msg", "旧接口返回的数据为0，无需比较！");
            result.put("diffData",findDiff);
            return result;
        }
        if (newDataList == null) {
            result.put("error", "true");
            result.put("msg", "新接口返回的数据为0，无需比较！");
            result.put("diffData",findDiff);
            return result;
        }
        if (!(oldDataList.size() == newDataList.size())||!newDataIds.containsAll(oldDataIds)) {
            result.put("error", "true");
            result.put("msg", "两个接口的返回的数据不相同！");
            result.put("diffData",findDiff);
            return result;
        }
        HashMap<String,ArrayList<HashMap>> diffField = new HashMap<String, ArrayList<HashMap>>();
        Iterator ite1 = bothDataIds.iterator();
        //比较两个接口所有数据的各字段值
        int count = 1;
        while(ite1.hasNext()){//每条数据遍历
            ArrayList<HashMap> onedataDiffFields = new ArrayList<HashMap>();
            System.out.println("************************开始处理第"+count+"条数据！***********************");
            count++;
            String id = (String)ite1.next();//比较的id
            Map oldData = oldDataMap.get(id);//根据数据id获取数据的详情
            Map newData = newDataMap.get(id);
            if(oldData!=null && newData!=null){

                Set dataField = oldData.keySet();//各个字段的apiname
                Iterator f1 = dataField.iterator();
                while (f1.hasNext()) {//开始比较
                    String fieldname = (String) f1.next();
                    System.out.println("开始比较的字段为:" + fieldname);
                    if (!(fieldname.equals("UDFieldDatas"))) {//忽略掉的字段
                        System.out.println("filed的值old：" + fieldname);
                        String newkey = ParseFile.getObjValue(object, fieldname);
                        System.out.println("filed的值new：" + fieldname);
                        String oldValue = objectToString(oldData.get(fieldname));
                        if (newkey != null) {
                          //  System.out.println("oldValue=" + oldData.get(fieldname));
                          //  System.out.println("newValue=" + newData.get(newkey));
                            String newValue = objectToString(newData.get(newkey));
                           // System.out.println("oldValue(String)=" + oldValue);
                           // System.out.println("newValue(String)=" + newValue);
                            if((oldValue != null && newValue == null)||(oldValue == null && newValue != null)){
                                HashMap field = new HashMap();
                                field.put("old_"+fieldname,oldValue);
                                field.put("new_"+newkey,newValue);
                                onedataDiffFields.add(field);
                            }

                            if (oldValue != null && newValue != null) {
                                if (!oldValue.equals(newValue)) {
                                    HashMap field = new HashMap();
                                    field.put("old_"+fieldname,oldValue);
                                    field.put("new_"+newkey,newValue);
                                    onedataDiffFields.add(field);
                                }
                           }
                        } else {
//                            System.out.println("oldValue=" + oldData.get(fieldname));
//                            System.out.println("newValue=" + newData.get(fieldname));

                            String newValue = objectToString(newData.get(fieldname));
//                            System.out.println("oldValue(String)=" + oldValue);
//                            System.out.println("newValue(String)=" + newValue);
                            if((oldValue != null && newValue == null)||(oldValue == null && newValue != null)){
                                HashMap field = new HashMap();
                                field.put("old_"+fieldname,oldValue);
                                field.put("new_"+fieldname,newValue);
                                onedataDiffFields.add(field);
                            }
                            if (oldValue != null && newValue != null) {
                                if (!oldValue.equals(newValue)) {
                                    HashMap field = new HashMap();
                                    field.put("old_"+fieldname,oldValue);
                                    field.put("new_"+fieldname,newValue);
                                    onedataDiffFields.add(field);
                                }
                            }

                        }

                    }
                }
            }
            diffField.put(id,onedataDiffFields);
        }
        if((diffField.get("0")!=null && diffField.get("2")!=null)&&diffField.size()==0){
            result.put("error", "false");
            result.put("msg", "比较完成，新旧接口相同");
            result.put("diffData",findDiff);
            result.put("diffFiled",diffField);
            return result;
        }

        result.put("error", "true");
        result.put("msg", "比较完成，有字段值在新旧接口中不同！");
        result.put("diffData",findDiff);
        result.put("diffFiled",diffField);
        return result;

    }
//    public static HashMap getFieldMap(String key1,String value1,String key2,String value){
//
//    }
    /**
     * 判断两个object是否相等
     * @param param
     * return true：相等，false：不相等
     */
    public static String objectToString(Object param){

        if (param instanceof Integer) {
             String value = ((Integer) param).toString();
            return value;
        }
        if (param instanceof String) {
            String value = (String)param;
            return value;
        }
        if (param instanceof Double) {
            String value = ((Double) param).toString();
            return value;
        }
        if (param instanceof Float) {
            String value = ((Float) param).toString();
            return value;
        }
        if (param instanceof Long) {
            String value = ((Long) param).toString();
            return value;
        }
        if (param instanceof Boolean) {
            String  value = ((Boolean) param).toString();
            return value;
        }
        if (param instanceof Date) {
            String value = ((Date) param).toString();
            return value;
        }
        if ((param instanceof List) ){//|| param instanceof ArrayList) {
            List  list = ((List) param);
            StringBuilder value = new StringBuilder();
            for(int i=0;i<list.size();i++){
                value.append(list.get(i));
            }
            return value.toString();
        }

        return "";

    }

    /**
     * 获取两个集合不同
     * @param rps1  rps1数据
     * @param rps2  rps2数据
     * @return  0:rps1中独有的数据;1:交集的数据;2:rps2中的独有数据
     */
    private static Map<Integer, HashSet<String>> findListDiff(HashSet rps1,HashSet rps2){
        //判断不能为空
        if(rps1 == null || rps1.isEmpty() || rps2 == null || rps1.isEmpty()) return null;

        //保存最后的数据
        Map<Integer, HashSet<String>>  mapList = new HashMap<Integer, HashSet<String>>(3);

        //复制rps1，作为备份
        HashSet<String> rps1_bak = new HashSet<String>(rps1);

        //1、获取rps1中与rps2中不同的元素
        rps1.removeAll(rps2);

        //2、获取rps1和rps2中相同的元素
        rps1_bak.removeAll(rps1);

        //3、获取rps2中与rps1中不同的元素
        rps2.removeAll(rps1_bak);

        //经过此转换后rps1中数据与rps2中的数据完全不同
        //rps1_bak是rps1和rps2的交集
        //rps2中的数据与rps1中的数据完全不同

        mapList.put(0, rps1);//rps1中独有的数据
        mapList.put(1, rps1_bak);//交集的数据
        mapList.put(2, rps2);//rps2中的独有数据

        return mapList;
    }
}

