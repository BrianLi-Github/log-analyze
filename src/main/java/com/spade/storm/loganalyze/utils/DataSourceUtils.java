package com.spade.storm.loganalyze.utils;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/02/10:30
 * @Description:
 */
public class DataSourceUtils {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceUtils.class);

    private static DataSource dataSource;

    static {
        DruidDataSource ds = new DruidDataSource();
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = DataSourceUtils.class.getResourceAsStream("/data-source.properties");
            properties.load(in);
        } catch (FileNotFoundException e) {
            logger.error("data-source.properties文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("data-source.properties文件流关闭出现异常");
            }
        }
        ds.configFromPropety(properties);
        dataSource = ds;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void main(String[] args) {
        DataSource dataSource = DataSourceUtils.getDataSource();
        System.out.println(dataSource);
    }
}
