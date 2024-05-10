package com.loong.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Component
public class BaiduMapUtil {

    static final String coordinateUrl = "https://api.map.baidu.com/geocoding/v3";
    static final String routePlanUrl = "https://api.map.baidu.com/directionlite/v1/driving";

    public static Map getCoordinate(String address, String ak) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        Map param = new HashMap<>();
        param.put("address", address);
        param.put("ak", ak);
        param.put("output", "json");
        String response = httpClientUtil.doGet(coordinateUrl, param);
        Map responseMap = JSON.parseObject(response, HashMap.class);
        String result = JSON.toJSONString(responseMap.get("result"));
        Map resultMap = JSON.parseObject(result, HashMap.class);
        Map coordinate = (Map) resultMap.get("location");
        return coordinate;
    }

    // https://api.map.baidu.com/directionlite/v1/driving?origin=40.01116,116.339303&destination=39.936404,116.452562&ak=您的AK
    public static Map getRoute(String ak, String origin, String destination) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        Map param = new HashMap<>();
        param.put("ak", ak);
        param.put("origin", origin);
        param.put("destination", destination);
        String response = httpClientUtil.doGet(routePlanUrl, param);
        Map responseMap = JSON.parseObject(response, HashMap.class);
        String result = JSON.toJSONString(responseMap.get("result"));
        Map resultMap = JSON.parseObject(result, HashMap.class);
        List routes = (List) resultMap.get("routes");
        Map routesMap = (Map) routes.get(0);
        return routesMap;
    }

}
