package com.learn.lhh;

import org.testng.annotations.Test;
import com.learn.lhh.common.PropertiesUtil;

public class MyTest {
    @Test
    public void mytest(){
        System.out.println("==========================");
        String a = PropertiesUtil.getValue("key1");
        System.out.println("key1的值是--------"+a);
    }


}
