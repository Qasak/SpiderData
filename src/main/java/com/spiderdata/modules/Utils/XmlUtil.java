package com.spiderdata.modules.Utils;

import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Wangjs
 * @version 1.0
 * @date 2021/1/28 23:02
 */
public class XmlUtil {
    private static Map<String, String> map = YmlUtil.getYmlByFileName("biliconfig.yml");
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
    public static void getP(String fileName) {
        String content = null;
        try {
            content = getAll(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String c = "\"(.*?)</d" ;
        Pattern a = Pattern.compile(c);
        Matcher m = a.matcher(content);
        while(m.find()){
            String line = m.group();
            line = line.replace("</d","") ;
            line = line.replace("\"","") ;

            String[] str=line.split(">");
            // 1.0 encoding=UTF-8?
            if(str[0].length() == 19) {
                continue;
            }
            String[] p = str[0].split(",");
            long biliDbId = Long.parseLong(p[7]);
            Date sendTime = new Date(Long.parseLong(p[4]) * 1000);
            String danmaku = str[1];
            String userHash = p[6];
            float appearTime = Float.parseFloat(p[0]);
            System.out.println(biliDbId + " " + sendTime + " " + danmaku + " " + userHash + " " + appearTime);

        }
    }

    public static void main(String[] args) throws IOException {
        getP("C:\\Users\\qasak\\Documents\\bili\\bilibili献给新一代的演讲《后浪》_BV1FV411d7u7\\2020-05-03.xml");
    }
}
