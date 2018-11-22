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
            result.put("result",findDiff);
            return result;
        }
        if (newDataList == null) {
            result.put("error", "true");
            result.put("msg", "新接口返回的数据为0，无需比较！");
            result.put("result",findDiff);
            return result;
        }
        if (!(oldDataList.size() == newDataList.size())||!newDataIds.containsAll(oldDataIds)) {
            result.put("error", "true");
            result.put("msg", "两个接口的返回的数据不相同！");
            result.put("result",findDiff);
            return result;
        }

        Iterator ite1 = bothDataIds.iterator();


        //比较两个接口所有数据的各字段值
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

