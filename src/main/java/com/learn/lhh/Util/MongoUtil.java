//package com.learn.lhh.Util;
public class MongoUtil{
    public static void main(String[] args){
        //String tenantId=args[1];
        String tenantId="158833";
        System.out.println(Math.abs(tenantId.hashCode()%100));
    }
}