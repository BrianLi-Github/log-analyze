package com.spade.storm.loganalyze.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/02/14:29
 * @Description:
 * 日志匹配类型
 * 1:包含 2:等于 3：正则
 */
public enum LogCompareType {
    CONTAINS(1),EQUALS(2),REGEX(3);

    private Integer code;
    LogCompareType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public static LogCompareType getByCode(Integer code) {
        for(LogCompareType type : LogCompareType.values()){
            if (code.equals(type.getCode())) {
                return type;
            }
        }
        return null;
    }
}
