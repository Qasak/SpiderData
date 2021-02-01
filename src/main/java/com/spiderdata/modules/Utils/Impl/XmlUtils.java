package com.spiderdata.modules.Utils.Impl;

import java.io.*;
import java.util.Map;

/**
 * @author Wangjs
 * @version 1.0
 * @date 2021/1/28 23:02
 */
public class XmlUtils {
    private static Map<String, String> map = YmlUtils.getYmlByFileName("biliconfig.yml");
    private static final String ADDR = map.get("dir.addr");

    public static String getAll(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();
        return stringBuilder.toString();
    }




}
