package com.spade.storm.loganalyze.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/01/22:54
 * @Description:
 */
public class Constants {

    public static final String TOPOLOGY_NAME = "LogAnalyzeTopology";
    public static final String KAFKA_TOPIC = "log-analyze";
    public static final String KAFKA_SPOUT_NAME = "LogAnalyzeSpout";
    public static final String KAFKA_CONSUMER_GROUP = "LogAnalyzeConsumerGroup";//Kafka消费组
    public static final String FILTER_BOLT_NAME = "LogAnalyzeFilterBolt";
    public static final String PROCESS_BOLT_NAME = "LogAnalyzeProcessBolt";
    public static final String KAFKA_SERVERS = "shizhan01:9092,shizhan02:9092,shizhan03:9092";
    public static final String REDIS_HOST = "192.168.71.1";
    public static final String REDIS_PWD = "123456";
}
