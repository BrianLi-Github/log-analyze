package com.spade.storm.loganalyze.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/02/10:06
 * @Description:
 */
@Data
public class LogAnalyzeJob {
     private Integer jobId;
     private String jobName;
     private Integer jobType;
     private Integer status;
     private String createUser;
     private String updateUser;
     private Date createDate;
     private Date updateDate;
}
