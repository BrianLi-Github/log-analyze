package com.spade.storm.loganalyze.entity;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/02/10:19
 * @Description:
 */
@Data
public class LogAnalyzeJobCondition {
    private Integer jobId;   //对应的任务
    private String field;   //匹配的字段
    private String value;    //匹配的字段值
    private Integer compare;  //匹配类型   1:包含   2:相等
}
