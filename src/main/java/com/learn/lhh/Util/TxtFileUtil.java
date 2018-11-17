package com.learn.lhh.Util;

import java.io.*;
import java.util.ArrayList;

public class TxtFileUtil {

    public static ArrayList<String> readLineToArray(String name) {
        // 使用ArrayList来存储每行读取到的字符串
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            InputStream in = ClassLoader.class.getResourceAsStream(name);
            System.out.println(in);
            InputStreamReader inputReader = new InputStreamReader(in);
            BufferedReader bf = new BufferedReader(inputReader);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                //System.out.println(str.length());
                if(!(str.length()==0)) {
                    if (!str.contains("===")) {
                        arrayList.add(str);
                    }
                }
            }
            bf.close();
            inputReader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对ArrayList中存储的字符串进行处理

        return arrayList;
    }

}
