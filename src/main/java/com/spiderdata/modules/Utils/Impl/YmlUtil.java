package com.spiderdata.modules.Utils.Impl;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
* @author Wangjs
* @version 1.0
* @date 2021/1/28 17:25
*/

public class YmlUtil {
    private static String bootstrap_file = "application.yml";
    private static String biliConfig = "biliconfig.yml";
    private static Map<String,String> result = new HashMap<>();
    /**
     * 根据文件名获取yml的文件内容
     * @return
     */
    public static Map<String,String> getYmlByFileName(String file){
        result = new HashMap<>();
        if(file == null) {
            file = bootstrap_file;
        }
        InputStream in = YmlUtil.class.getClassLoader().getResourceAsStream(file);
        Yaml props = new Yaml();
        Object obj = props.loadAs(in, Map.class);
        Map<String,Object> param = (Map<String, Object>) obj;

        for(Map.Entry<String,Object> entry:param.entrySet()){
            String key = entry.getKey();
            Object val = entry.getValue();

            if(val instanceof Map){
                forEachYaml(key,(Map<String, Object>) val);
            }else{
                result.put(key,val.toString());
            }
        }
        return result;
    }

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    public static String getValue(String key){
        Map<String,String> map = getYmlByFileName(null);
        if(map==null) {
            return null;
        }
        return map.get(key);
    }


    /**
     * 遍历yml文件，获取map集合
     * @param key_str
     * @param obj
     * @return
     */
    public static Map<String,String> forEachYaml(String key_str, Map<String, Object> obj){
        for(Map.Entry<String,Object> entry:obj.entrySet()){
            String key = entry.getKey();
            Object val = entry.getValue();

            String str_new = "";
            if(StringUtil.isNotEmpty(key_str)){
                str_new = key_str+ "."+key;
            }else{
                str_new = key;
            }
            if(val instanceof Map){
                forEachYaml(str_new, (Map<String, Object>) val);
            }else{
                result.put(str_new, val.toString());
            }
        }
        return result;
    }


    /**
     * 获取bootstrap.yml的name
     * @return
     */
    public static String getApplicationName(){
        return getYmlByFileName(bootstrap_file).get("spring.application.name");
    }

    /**
     * 获取bootstrap.yml的name
     * @return
     */
    public static String getApplicationName1(){
        String name =  getYmlByFileName(bootstrap_file).get("spring.application.name");
        return name + "center";
    }

    public static void main(String[] args) {
        System.out.println(getYmlByFileName(biliConfig));
    }
}

