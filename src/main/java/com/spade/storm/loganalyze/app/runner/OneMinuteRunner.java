package com.spade.storm.loganalyze.app.runner;

import com.spade.storm.loganalyze.app.AppendRecord;
import com.spade.storm.loganalyze.dao.LogAnalyzeDao;
import com.spade.storm.loganalyze.entity.LogAnalyzeJob;
import com.spade.storm.loganalyze.utils.DateUtils;
import com.spade.storm.loganalyze.utils.JedisUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/02/21:34
 * @Description:
 * 计算每分钟的指标增量信息，并保存到数据库中
 */
public class OneMinuteRunner implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(OneMinuteRunner.class);

    @Override
    public void run() {
         //每天0点将cache中的pv和uv值清空
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.HOUR_OF_DAY) == 0) {
            OneMinuteDataCache.resetPvMap();
            OneMinuteDataCache.resetUvMap();
        }

        List<AppendRecord> records = genIndexAppendRecords();
        LogAnalyzeDao.saveOneMinuteRecords(records);
    }



    private List<AppendRecord> genIndexAppendRecords() {
        List<AppendRecord> recordList = new ArrayList<>();
        List<LogAnalyzeJob> jobList = LogAnalyzeDao.findJobList();
        ShardedJedis jedis = JedisUtil.getInstance();
        jobList.forEach(job -> {
            //log:spade_p1002:pv:20200602
            String pvKey = "log:" + job.getJobName() + ":pv:" + DateUtils.getDate();
            //log:spade_p1002:uv:20200602
            String uvKey = "log:" + job.getJobName() + ":uv:" + DateUtils.getDate();
            //获取redis中所有指标的最新值
            String pv = jedis.get(pvKey);
            long uv = jedis.scard(uvKey);

            logger.info("Get current pv:{}, uv:{} from redis.", pv, uv);
            //计算增量指标
            Long appendPv = OneMinuteDataCache.getPv(Long.parseLong(pv), job.getJobName());
            Long appendUv = OneMinuteDataCache.getUv(uv, job.getJobName());

            logger.info("Calculate pv, uv append -->  appendPv:{}, appendUv:{} from redis.", appendPv, appendUv);
            recordList.add(new AppendRecord(job.getJobName(), appendPv.intValue(), appendUv, new Date()));
        });
        jedis.close();
        return recordList;
    }

    @Data
    static class OneMinuteDataCache {
        public static Map<String, Long> pvMap = new HashMap<>();
        public static Map<String, Long> uvMap = new HashMap<>();;

        public static Long getPv(long currentPv, String jobName) {
            Long cachePv = pvMap.get(jobName);
            if (cachePv == null) {
                cachePv = 0L;
                pvMap.put(jobName, cachePv);
            }
            if (currentPv < cachePv) {
                return 0L;
            }
            //将当前时间点的pv值放到cache中
            pvMap.put(jobName, currentPv);
            return currentPv - cachePv;
        }

        public static Long getUv(long currentUv, String jobName) {
            Long cacheUv = uvMap.get(jobName);
            if (cacheUv == null) {
                cacheUv = 0L;
                uvMap.put(jobName, cacheUv);
            }
            if (currentUv < cacheUv) {
                return 0L;
            }
            //将当前时间点的uv值放到cache中
            uvMap.put(jobName, currentUv);
            return currentUv - cacheUv;
        }

        public static void resetPvMap() {
            pvMap =  new HashMap<String, Long>();
        }

        public static void resetUvMap() {
            uvMap = new HashMap<String, Long>();
        }
    }
}
