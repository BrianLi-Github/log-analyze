package com.spade.storm.loganalyze.dao;

import com.spade.storm.loganalyze.app.AppendRecord;
import com.spade.storm.loganalyze.entity.LogAnalyzeJob;
import com.spade.storm.loganalyze.entity.LogAnalyzeJobCondition;
import com.spade.storm.loganalyze.utils.DataSourceUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/02/10:30
 * @Description:
 */
public class LogAnalyzeDao {

    private static JdbcTemplate jdbcTemplate;

    static {
        jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource());
    }


    public static List<LogAnalyzeJob> findJobList() {
        return jdbcTemplate.query("SELECT jobId,jobName,jobType,businessId,status,createUser,updateUser,createDate,updateDate  FROM log_analyze.log_analyze_job WHERE status =1", new BeanPropertyRowMapper<LogAnalyzeJob>(LogAnalyzeJob.class));
    }

    public static List<LogAnalyzeJobCondition> findJobConditions() {
        return jdbcTemplate.query("SELECT id,jobId,field,value,compare FROM log_analyze.log_analyze_job_condition", new BeanPropertyRowMapper<LogAnalyzeJobCondition>(LogAnalyzeJobCondition.class));
    }

    public static void saveOneMinuteRecords(List<AppendRecord> records) {
        String sql = "INSERT INTO log_analyze.log_analyze_job_minute_append (indexName,pv,uv,executeTime,createTime ) VALUES (?,?,?,?,?)";
        batchUpdate(sql, records);
    }

    private static void batchUpdate(String sql, List<AppendRecord> records) {
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                AppendRecord record = records.get(i);
                ps.setString(1, record.getIndexName());
                ps.setInt(2, record.getPv());
                ps.setLong(3, record.getUv());
                ps.setTimestamp(4, new Timestamp(record.getExecuteTime().getTime()));
                ps.setTimestamp(5, new Timestamp(new Date().getTime()));
            }

            @Override
            public int getBatchSize() {
                return records.size();
            }
        });
    }
}
