package com.spade.storm.loganalyze;

import com.google.gson.Gson;
import com.spade.storm.loganalyze.entity.LogMessage;
import com.spade.storm.loganalyze.utils.Constants;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/02/15:10
 * @Description:
 */
public class LogSender {

    public static void main(String[] args) {
        List<LogMessage> list = new ArrayList();
        list.add(new LogMessage(1,"http://www.spade.cn/product?id=1002",
                "http://www.spade.cn/","Brian"));
        list.add(new LogMessage(1,"http://www.spade.cn/product?id=1002",
                "http://www.spade.cn/","Jane"));
        list.add(new LogMessage(1,"http://www.spade.cn/product?id=1002",
                "http://www.spade.cn/","Bryant"));
        list.add(new LogMessage(1,"http://www.spade.cn/product?id=1002",
                "http://www.spade.cn/","Brittany"));
        list.add(new LogMessage(1,"http://www.spade.cn/product?id=1002",
                "http://www.spade.cn/","James"));
        list.add(new LogMessage(1,"http://www.spade.cn/product?id=1002",
                "http://www.spade.cn/","Kit"));
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", Constants.KAFKA_SERVERS);
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("request.required.acks", "1");
        properties.put("partitioner.class", "org.apache.kafka.clients.producer.internals.DefaultPartitioner");

        Producer<String, String> producer = new KafkaProducer<String, String>(properties);
        final Random rand = new Random();
        for (int i=0; i< 10000; i++) {
            LogMessage msg = list.get(rand.nextInt(list.size()));
            System.out.println("Sending index:" + i);
            producer.send(new ProducerRecord<>(Constants.KAFKA_TOPIC, String.valueOf(i), new Gson().toJson(msg)));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
