package com.spade.storm.loganalyze.app;

import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/02/22:10
 * @Description:
 * 增量指标实体类，记录各个维度的pv，uv指标
 */
@Data
public class AppendRecord {
     private String indexName;
     private Integer pv;
     private Long uv;
     private Date executeTime;
     private Date createTime;

     public AppendRecord(String indexName, Integer pv, Long uv, Date executeTime) {
         this.indexName = indexName;
         this.pv = pv;
         this.uv = uv;
         this.executeTime = executeTime;
     }
}
