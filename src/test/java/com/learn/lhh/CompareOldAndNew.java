package com.learn.lhh;

import com.alibaba.fastjson.JSONArray;
import java.util.*;
import com.alibaba.fastjson.JSONObject;
import com.learn.lhh.Common.ParseFile;
import com.learn.lhh.Util.JsonUtilWithFastjson;

public class CompareOldAndNew {
    public static LinkedHashMap<String, Object> compare(String object, JSONArray oldDataList, JSONArray newDataList,JSONObject newDescribe) {

        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        LinkedList<HashMap> compareResult = new LinkedList<HashMap>();

        TreeMap<String, Map> oldDataMap = new TreeMap<String, Map>();
        TreeMap<String, Map> newDataMap = new TreeMap<String, Map>();
        /**将旧接口返回的数据list转换为<数据id,详情>的map **/
        for (int i = 0; i < oldDataList.size(); i++) {
            Map oneData = (Map) oldDataList.get(i);
            String okey = (String) oneData.get("ContactID");
            oldDataMap.put(okey, oneData);
        }
        /**将新接口返回的数据list转换为<数据id,详情>的map**/
        for (int i = 0; i < newDataList.size(); i++) {
            Map oneData = (Map) newDataList.get(i);
            String nkey = (String) oneData.get("_id");
            newDataMap.put(nkey, oneData);
        }
        //获取新旧接口所有的数据ID
        HashSet oldDataIds = new HashSet(oldDataMap.keySet());
        HashSet newDataIds = new HashSet(newDataMap.keySet());

        //找到新旧接口的数据ID的交集，以及旧接口返回新接口没返回的数据，以及新接口返回旧接口没返回的数据id
        Map<String, HashSet<String>> findDiff = new HashMap<String, HashSet<String>>(findListDiff(oldDataIds,newDataIds));
        LinkedHashMap<String, HashSet<String>> findDiffResult = new LinkedHashMap<String, HashSet<String>>();
        HashSet oldOnly = new HashSet<String>(findDiff.get("0"));
        HashSet newOnly = new HashSet<String>(findDiff.get("2"));
        findDiffResult.put("只在旧接口中存在的数据",findDiff.get("0"));
        findDiffResult.put("只在新接口中存在的数据",findDiff.get("2"));
        System.out.println("只在旧接口中存在的数据: "+findDiff.get("0"));
        System.out.println("只在新接口中存在的数据: "+findDiff.get("2"));

        HashSet bothDataIds = new HashSet<String>(findDiff.get("1"));
        //System.out.println("===*****==="+findDiff);

        Iterator ite1 = bothDataIds.iterator();
        //比较两个接口所有数据的各字段值
        /**
         *开始遍历每条数据
         */
        int count = 1;
        while(ite1.hasNext()){//每条数据遍历
            System.out.println("************************开始处理第"+count+"条数据！***********************");
            count++;
            ArrayList<HashMap> onedataDiffFields = new ArrayList<HashMap>();
            ArrayList<HashMap> onedataNotTransferFields = new ArrayList<HashMap>();
            LinkedHashMap oneDataComResult = new LinkedHashMap();/**************************/
            String id = (String)ite1.next();/**比较的id**/
            //根据id获取name
            Map oldData = oldDataMap.get(id);//根据数据id获取数据的详情
            Map newData = newDataMap.get(id);
            String dataName = getObjDataNameFromOld(object,oldData);/**此条数据的name**/
            oneDataComResult.put("数据Name",dataName);
            oneDataComResult.put("数据id",id);
            System.out.println("数据Name = "+ dataName);
            System.out.println("数据id = "+ id);
            if(oldData!=null && newData!=null){
                Set dataField = oldData.keySet();//各个字段的apiname
                Iterator f1 = dataField.iterator();
                /**
                 *开始遍历每一个field
                 */
                while (f1.hasNext()) {//遍历各个field开始比较
                    LinkedHashMap<String, String> oneFieldComResult = new LinkedHashMap<String, String>();
                    HashMap notTransferApiName = new HashMap();
                    String fieldApiName = (String) f1.next(); /**字段的apiname**/
                    String fieldName = getFiledNameFromNew(object, newDescribe, fieldApiName);  /**字段的label，由于旧接口没有返回describe，所以先从新接口拿**/

                    String newkey = ParseFile.getObjValue(object, fieldApiName);
                    String oldValue = objectToString(oldData.get(fieldApiName));
                    if (newkey != null) {/**如果在配置文件中找到了newkey,则用newkey查找新的value**/
                        //HashMap diffValue = new HashMap();
                        String newValue = objectToString(newData.get(newkey));
                        if ((oldValue != null && newValue == null) || (oldValue == null && newValue != null)) {
                            oneFieldComResult.put("字段Name", fieldName);
                            oneFieldComResult.put("旧接口ApiName", fieldApiName);
                            oneFieldComResult.put("新接口ApiName", newkey);
                            oneFieldComResult.put("旧接口的字段值", oldValue);
                            oneFieldComResult.put("新接口的字段值", newValue);
                        }
                        if (oldValue != null && newValue != null) {
                            if (!oldValue.equals(newValue)) {
                                oneFieldComResult.put("字段Name", fieldName);
                                oneFieldComResult.put("旧接口ApiName", fieldApiName);
                                oneFieldComResult.put("新接口ApiName", newkey);
                                oneFieldComResult.put("旧接口的字段值", oldValue);
                                oneFieldComResult.put("新接口的字段值", newValue);
                            }
                        }
                        if(oneFieldComResult.size()!=0) {
                            onedataDiffFields.add(oneFieldComResult);
                        }
                    } else {
                        notTransferApiName.put("字段Name", fieldName);
                        notTransferApiName.put("旧接口ApiName", fieldApiName);
                        notTransferApiName.put("新接口的ApiName", newkey);
                        notTransferApiName.put("msg", "在配置文件中没有找到该key的对应值");
                        /**需要对没找到的key进行特殊处理，方法待思考**/
                        onedataNotTransferFields.add(notTransferApiName);
                    }

                }
            }
            oneDataComResult.put("Value不同的字段Map",onedataDiffFields);
            oneDataComResult.put("未找到转换关系的字段Map",onedataNotTransferFields);

            compareResult.add(oneDataComResult);
           // diffField.put(id,onedataDiffFields);
        }
        result.put("数据量比较",findDiffResult);
        result.put("字段比较结果",compareResult);
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

        return "";//为null的情况

    }

    /**
     * 获取两个集合不同
     * @param rps1  rps1数据
     * @param rps2  rps2数据
     * @return  0:rps1中独有的数据;1:交集的数据;2:rps2中的独有数据
     */
    private static Map<String, HashSet<String>> findListDiff(HashSet rps1,HashSet rps2){
        //判断不能为空
        if(rps1 == null || rps1.isEmpty() || rps2 == null || rps1.isEmpty()) return null;

        //保存最后的数据
        Map<String, HashSet<String>>  mapList = new HashMap<String, HashSet<String>>(3);

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

        mapList.put("0", rps1);//rps1中独有的数据
        mapList.put("1", rps1_bak);//交集的数据
        mapList.put("2", rps2);//rps2中的独有数据

        return mapList;
    }
    public static String getObjDataNameFromOld(String object,Map oldData){
        if(object.equals("ContactObj")){
            return objectToString(oldData.get("Name"));
        }

        return "";
    }
    public static String getObjDataNameFromNew(String object,Map newData){
        return objectToString(newData.get("Name"));
    }
    public static String getFiledNameFromNew(String object,JSONObject newDescribe,String fieldApiName){
        String newkey = ParseFile.getObjValue(object,fieldApiName);
        //System.out.println(newDescribe);
        Map describe = newDescribe.getJSONObject(newkey);
        if(newkey!=null && describe!=null) {
            String newName = objectToString(describe.get("label"));
            return newName;
        }
        return "***";
    }
    public static String getFiledNameFromOld(String object,JSONObject oldDescribe,String fieldApiName){
//        String newkey = ParseFile.getObjValue(object,fieldApiName);
//
//        if(newkey!=null) {
//            Map describe = newDescribe.getJSONObject(newkey);
//            return objectToString(describe.get("label"));
//
//        }
        return null;
    }

}

