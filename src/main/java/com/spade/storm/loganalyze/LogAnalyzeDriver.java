package com.spade.storm.loganalyze;

import clojure.lang.IFn;
import com.spade.storm.loganalyze.blot.FilterBolt;
import com.spade.storm.loganalyze.blot.ProcessBolt;
import com.spade.storm.loganalyze.utils.Constants;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/01/22:07
 * @Description:
 * 点击流日志分析系统：（假设：Flume对日志信息进行采集，下沉到kafka的topic中）
 * 1、利用KafkaSpout对Kafka中的topic进行消费，将日志信息发射到FilterBolt中处理
 * 2、FilterBolt接收日志信息，解析，校验，将符合的信息构造成LogMessage对象，发射到ProcessBolt中进行保存
 * 3、ProcessBolt对实时信息进行分析，并保存数据在redis中
 * 4、同步程序，定时从redis中获取数据，计算出与上一次结果的增量信息，并保存到MySQL中
 */
public class LogAnalyzeDriver {

    public static void main(String[] args) throws Exception {
        //创建Topology
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        KafkaSpoutConfig.Builder<String, String> kafkaSpoutBuilder = new KafkaSpoutConfig.Builder<String, String>(Constants.KAFKA_SERVERS, Constants.KAFKA_TOPIC);
        kafkaSpoutBuilder.setProcessingGuarantee(KafkaSpoutConfig.ProcessingGuarantee.AT_MOST_ONCE);
        kafkaSpoutBuilder.setProp("group.id", Constants.KAFKA_CONSUMER_GROUP);
        kafkaSpoutBuilder.setProp("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaSpoutBuilder.setProp("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaSpout<String, String> spout = new KafkaSpout<String, String>(kafkaSpoutBuilder.build());
        topologyBuilder.setSpout(Constants.KAFKA_SPOUT_NAME, spout, 2);
        topologyBuilder.setBolt(Constants.FILTER_BOLT_NAME, new FilterBolt(), 2).shuffleGrouping(Constants.KAFKA_SPOUT_NAME);
        topologyBuilder.setBolt(Constants.PROCESS_BOLT_NAME, new ProcessBolt(), 2).fieldsGrouping(Constants.FILTER_BOLT_NAME, new Fields("message"));

        Config config = new Config();
        config.setNumWorkers(1);
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology(Constants.TOPOLOGY_NAME, config, topologyBuilder.createTopology());
    }


}
