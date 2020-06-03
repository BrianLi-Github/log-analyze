package com.spade.storm.loganalyze.utils;

import com.google.gson.Gson;
import com.spade.storm.loganalyze.dao.LogAnalyzeDao;
import com.spade.storm.loganalyze.entity.LogAnalyzeJob;
import com.spade.storm.loganalyze.entity.LogAnalyzeJobCondition;
import com.spade.storm.loganalyze.entity.LogMessage;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.ShardedJedis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/02/9:23
 * @Description:
 * 业务处理工具类
 */
public class LogAnalyzeHandler {
    private static boolean reloaded = true;    //标识当前时间点已经reload
    //用来保存job信息，key为jobType，value为该类别下所有的任务。
    private static Map<String, List<LogAnalyzeJob>> jobMap;
    //用来保存job的判断条件，key为jobId,value为list，list中封装了很多判断条件。
    private static Map<String, List<LogAnalyzeJobCondition>> jobConditionMap;

    static {
        //加载任务信息
        loadJob();
        loadJobCondition();
    }

    private static synchronized void loadJobCondition() {
        List<LogAnalyzeJobCondition> jobConditions = LogAnalyzeDao.findJobConditions();
        jobConditionMap = jobConditions.parallelStream().collect(Collectors.groupingBy((LogAnalyzeJobCondition condition) -> String.valueOf(condition.getJobId())));
    }

    private static synchronized void loadJob() {
        List<LogAnalyzeJob> jobList = LogAnalyzeDao.findJobList();
        jobMap = jobList.parallelStream()
                .filter(job -> isValidType(job.getJobType()))
                .collect(Collectors.groupingBy((LogAnalyzeJob job) -> String.valueOf(job.getJobType())));
    }

    /**
     * 解析日志，如果符合规则，则构造成LogMessage对象
     * @param logMsg
     * @return
     */
    public static LogMessage parser(String logMsg) {
        if (StringUtils.isBlank(logMsg)) {
            return null;
        }
        LogMessage message = new Gson().fromJson(logMsg, LogMessage.class);
        if (!isValidType(message.getType())) {
            return null;
        }
        return message;
    }


    /**
     * 校验日志类型，将不是系统定义的类型的日志过滤掉
     * @param jobType
     * @return
     */
    private static boolean isValidType(int jobType) {
        return LogTypeConstant.validTypes().contains(jobType);
    }

    public static void reloadJobCondition() {
        String date = DateUtils.getDateTime();
        int now = Integer.parseInt(date.split(":")[1]);
        if (now % 10 == 0) {//每10分钟加载一次
            reloadData();
        } else {
            reloaded = false;
        }
    }

    private static synchronized void reloadData() {
         if (!reloaded) {
             loadJob();
             loadJobCondition();
             reloaded = true;
         }
    }

    /**
     * 处理日志信息，将不同类型日志的PV和UV保存到redis中
     * @param message
     */
    public static void process(LogMessage message) {
        //获取Mysql中的任务，根据任务的多个过滤条件进行过滤日志信息，计算符合条件的日志对应的PV和UV
        List<LogAnalyzeJob> jobList = jobMap.get(String.valueOf(message.getType()));
        ShardedJedis jedis = JedisUtil.getInstance();
        jobList.parallelStream().forEach(job -> {
            //获取任务的多个条件
            List<LogAnalyzeJobCondition> conditions = jobConditionMap.get(String.valueOf(job.getJobId()));
            AtomicReference<Boolean> isMatch = new AtomicReference<>(false);
            conditions.parallelStream().forEach(condition -> {
               String fieldValue = LogMessage.getFieldValue(condition.getField(), message);
               if (StringUtils.isBlank(fieldValue)) {
                   return;
               }
                LogCompareType compareType = LogCompareType.getByCode(condition.getCompare());
                if (compareType == null) {
                    return;
                }
                String conditionValue = condition.getValue();
                //1:包含 2:等于 3：正则
                switch (compareType) {
                    case CONTAINS:
                        if (fieldValue.contains(conditionValue)) {
                            isMatch.set(true);
                            return;
                        }
                        break;
                    case EQUALS:
                        if (fieldValue.equals(conditionValue)) {
                            isMatch.set(true);
                            return;
                        }
                        break;
                    case REGEX:
                        if (fieldValue.matches(conditionValue)) {
                            isMatch.set(true);
                            return;
                        }
                        break;
                    default:
                        break;
                }
            });

            //匹配成功，将redis里面的PV和UV的值自增
            if (isMatch.get()) {
                //log:spade_p1002:pv:20200602
                String pvKey = "log:" + job.getJobName() + ":pv:" + DateUtils.getDate();
                //log:spade_p1002:uv:20200602
                String uvKey = "log:" + job.getJobName() + ":uv:" + DateUtils.getDate();

                jedis.incr(pvKey);
                //UV值  -->  set集合中加一(自动去重)
                jedis.sadd(uvKey, message.getUserName());

            }
        });
        jedis.close();
    }
}
