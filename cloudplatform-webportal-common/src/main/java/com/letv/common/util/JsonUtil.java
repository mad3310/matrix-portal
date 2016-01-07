package com.letv.common.util;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by liuhao1 on 2016/1/4.
 */
public class JsonUtil {
    public static String transToString(Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
