package com.spade.storm.loganalyze.entity;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/02/9:23
 * @Description:
 */
@Data
public class LogMessage implements Serializable {

    private int type;//1：浏览日志、2：点击日志、3：搜索日志、4：购买日志
    @LogMatchField
    private String hrefTag;//标签标识
    @LogMatchField
    private String hrefContent;//标签对应的标识，主要针对a标签之后的内容
    @LogMatchField
    private String referrerUrl;//来源网址
    @LogMatchField
    private String requestUrl;//来源网址
    private String clickTime;//点击时间
    @LogMatchField
    private String appName;//浏览器类型
    private String appVersion;//浏览器版本
    private String language;//浏览器语言
    @LogMatchField
    private String platform;//操作系统
    private String screen;//屏幕尺寸
    private String coordinate;//鼠标点击时的坐标
    private String systemId; //产生点击流的系统编号
    @LogMatchField
    private String userName;//用户名称


    public LogMessage(int type, String requestUrl, String referrerUrl, String userName) {
        this.type = type;
        this.requestUrl = requestUrl;
        this.referrerUrl = referrerUrl;
        this.userName = userName;
    }

    /**
     * 根据字段名称获取对应的值
     * @param fieldName
     * @param message
     * @return
     */
    public static String getFieldValue(String fieldName, LogMessage message) {
        if (StringUtils.isBlank(fieldName)) {
            return "";
        }
        try {
            Field field = message.getClass().getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            //是过滤字段
            if (field.isAnnotationPresent(LogMatchField.class)) {
                String firstLetter = fieldName.substring(0, 1).toUpperCase();
                //getXXX()
                String getter = "get" + firstLetter + fieldName.substring(1);
                Method method = message.getClass().getDeclaredMethod(getter);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return (String) method.invoke(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String toString() {
        return "LogMessage{" +
                "type=" + type +
                ", hrefTag='" + hrefTag + '\'' +
                ", hrefContent='" + hrefContent + '\'' +
                ", referrerUrl='" + referrerUrl + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", clickTime='" + clickTime + '\'' +
                ", appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", language='" + language + '\'' +
                ", platform='" + platform + '\'' +
                ", screen='" + screen + '\'' +
                ", coordinate='" + coordinate + '\'' +
                ", systemId='" + systemId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
