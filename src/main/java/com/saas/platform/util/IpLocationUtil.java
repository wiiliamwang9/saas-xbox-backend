package com.saas.platform.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * IP地理位置查询工具类
 * 
 * @author SaaS Xbox Team
 * @since 2024-08-01
 */
@Component
public class IpLocationUtil {

    private static final Logger log = LoggerFactory.getLogger(IpLocationUtil.class);

    /**
     * 地理位置信息实体
     */
    public static class LocationInfo {
        private String country;
        private String region;
        private String city;

        public LocationInfo() {}

        public LocationInfo(String country, String region, String city) {
            this.country = country;
            this.region = region;
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return "LocationInfo{" +
                    "country='" + country + '\'' +
                    ", region='" + region + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }

    /**
     * 根据IP地址获取地理位置信息
     * 
     * @param ip IP地址
     * @return 地理位置信息
     */
    public LocationInfo getLocationByIp(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            log.warn("IP地址为空，无法查询地理位置");
            return new LocationInfo("未知", "未知", "未知");
        }

        // 过滤内网IP
        if (isPrivateIp(ip)) {
            log.warn("内网IP地址: {}, 返回默认地理位置", ip);
            return new LocationInfo("中国", "本地", "本地");
        }

        try {
            String url = "http://ip-api.com/json/" + ip + "?lang=zh-CN&fields=country,regionName,city";
            
            // 设置超时时间为5秒
            String response = HttpUtil.get(url, 5000);
            
            if (response == null || response.trim().isEmpty()) {
                log.warn("IP地理位置查询返回空结果, IP: {}", ip);
                return new LocationInfo("未知", "未知", "未知");
            }

            JSONObject json = JSONUtil.parseObj(response);
            
            String country = json.getStr("country", "未知");
            String region = json.getStr("regionName", "未知");
            String city = json.getStr("city", "未知");

            LocationInfo locationInfo = new LocationInfo(country, region, city);
            log.info("IP地理位置查询成功: IP={}, Location={}", ip, locationInfo);
            
            return locationInfo;
            
        } catch (Exception e) {
            log.error("查询IP地理位置失败: IP={}, Error={}", ip, e.getMessage(), e);
            return new LocationInfo("查询失败", "查询失败", "查询失败");
        }
    }

    /**
     * 判断是否为内网IP
     * 
     * @param ip IP地址
     * @return 是否为内网IP
     */
    private boolean isPrivateIp(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return false;
        }

        try {
            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            int firstOctet = Integer.parseInt(parts[0]);
            int secondOctet = Integer.parseInt(parts[1]);

            // 10.0.0.0 - 10.255.255.255
            if (firstOctet == 10) {
                return true;
            }

            // 172.16.0.0 - 172.31.255.255
            if (firstOctet == 172 && secondOctet >= 16 && secondOctet <= 31) {
                return true;
            }

            // 192.168.0.0 - 192.168.255.255
            if (firstOctet == 192 && secondOctet == 168) {
                return true;
            }

            // 127.0.0.0 - 127.255.255.255 (本地回环)
            if (firstOctet == 127) {
                return true;
            }

            return false;
        } catch (NumberFormatException e) {
            log.warn("无效的IP地址格式: {}", ip);
            return false;
        }
    }

    /**
     * 获取地理位置的简化描述（国家/地区）
     * 
     * @param ip IP地址
     * @return 国家/地区
     */
    public String getLocationDescription(String ip) {
        LocationInfo locationInfo = getLocationByIp(ip);
        
        if ("未知".equals(locationInfo.getCountry()) || "查询失败".equals(locationInfo.getCountry())) {
            return locationInfo.getCountry();
        }
        
        if (locationInfo.getRegion() != null && !locationInfo.getRegion().isEmpty() 
            && !"未知".equals(locationInfo.getRegion())) {
            return locationInfo.getCountry() + "/" + locationInfo.getRegion();
        }
        
        return locationInfo.getCountry();
    }
}