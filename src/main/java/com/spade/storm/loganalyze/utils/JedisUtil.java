package com.spade.storm.loganalyze.utils;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/05/24/22:03
 * @Description:
 * Redis 工具类
 */
public class JedisUtil {
    private static ShardedJedisPool shardedJedisPool = null;
    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        poolConfig.setMaxIdle(10);
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        poolConfig.setMaxTotal(1000 * 10);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        poolConfig.setMaxWaitMillis(10000);
        /**
         *如果你遇到 java.net.SocketTimeoutException: Read timed out exception的异常信息
         *请尝试在构造JedisPool的时候设置自己的超时值. JedisPool默认的超时时间是2秒(单位毫秒)
         */
        //创建四个redis服务实例，并封装在list中
        List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
        JedisShardInfo jedisShardInfo = new JedisShardInfo(Constants.REDIS_HOST, 6379);
        jedisShardInfo.setPassword(Constants.REDIS_PWD);
        jedisShardInfo.setConnectionTimeout(10000);
        jedisShardInfo.setSoTimeout(10000);
        list.add(jedisShardInfo);

        //创建具有分片功能的的Jedis连接池
        shardedJedisPool = new ShardedJedisPool(poolConfig, list);
    }

    public static ShardedJedis getInstance() {
         return shardedJedisPool.getResource();
    }
}
