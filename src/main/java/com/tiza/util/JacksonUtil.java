package com.tiza.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Description: JacksonUtil
 * Author: DIYILIU
 * Update: 2016-03-22 9:25
 */
public class JacksonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object obj){

        String rs = null;

        try {
            rs = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
