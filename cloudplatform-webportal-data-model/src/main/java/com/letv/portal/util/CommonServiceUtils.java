package com.letv.portal.util;

import com.letv.portal.enumeration.MclusterStatus;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuhao1 on 2015/11/20.
 */
public class CommonServiceUtils {

    public static Integer transStatus(String statusStr){
        // { "meta": {"code": 200}, "response": {"status": " starting / started / stopping / stopped / destroying / destroyed / not exist / failed", "message": ""  } }
        Integer status = null;
        if("starting".equals(statusStr)) {
            status = MclusterStatus.STARTING.getValue();
        } else if("started".equals(statusStr)) {
            status = MclusterStatus.RUNNING.getValue();
        } else if("stopping".equals(statusStr)) {
            status = MclusterStatus.STOPPING.getValue();
        } else if("stopped".equals(statusStr)) {
            status = MclusterStatus.STOPED.getValue();
        } else if("destroying".equals(statusStr)) {
            status = MclusterStatus.DESTROYING.getValue();
        } else if("destroyed".equals(statusStr)) {
            status = MclusterStatus.DESTROYED.getValue();
        } else if("not exist".equals(statusStr)) {
            status = MclusterStatus.NOTEXIT.getValue();
        } else if("failed".equals(statusStr)) {
            status = MclusterStatus.CRISIS.getValue();
        } else if("danger".equals(statusStr)) {
            status = MclusterStatus.DANGER.getValue();
        } else if("crisis".equals(statusStr)) {
            status = MclusterStatus.CRISIS.getValue();
        }
        return status;
    }

    public static Map<String,Object> transResult(String result){
        ObjectMapper resultMapper = new ObjectMapper();
        Map<String,Object> jsonResult = new HashMap<String,Object>();
        if(com.mysql.jdbc.StringUtils.isNullOrEmpty(result))
            return jsonResult;
        try {
            jsonResult = resultMapper.readValue(result, Map.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

}
