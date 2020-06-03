package com.spade.storm.loganalyze.blot;

import com.spade.storm.loganalyze.entity.LogMessage;
import com.spade.storm.loganalyze.utils.LogAnalyzeHandler;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/01/23:02
 * @Description:
 */
public class FilterBolt extends BaseBasicBolt {

    private static final Logger logger = LoggerFactory.getLogger(FilterBolt.class);

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //接收Kafka中消费的数据
        String line = (String) input.getValue(4);
        //校验日志信息是否符合任务规则
        LogMessage message = LogAnalyzeHandler.parser(line);
        if (message == null) {
            return;
        }
        logger.info("FilterBolt ----> message: " + message.toString());
        collector.emit(new Values(message));
        //定时更新MySQL中的任务规则到内存中
        LogAnalyzeHandler.reloadJobCondition();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));
    }
}
