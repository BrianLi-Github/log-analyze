package com.spade.storm.loganalyze.blot;

import com.spade.storm.loganalyze.entity.LogMessage;
import com.spade.storm.loganalyze.utils.LogAnalyzeHandler;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/01/23:02
 * @Description:
 */
public class ProcessBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        LogMessage message = (LogMessage) input.getValueByField("message");
        LogAnalyzeHandler.process(message);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
