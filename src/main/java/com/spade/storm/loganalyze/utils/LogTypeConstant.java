package com.spade.storm.loganalyze.utils;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/02/9:40
 * @Description:
 * 定义日志的类型：
 *  点击，购买，浏览，查询
 */
public class LogTypeConstant {
    //浏览类型的数据
    public static final int VIEW = 1;
    //点击类型的数据
    public static final int CLICK = 2;
    //搜索类型的数据
    public static final int SEARCH = 3;
    //购买类型的数据
    public static final int BUY = 4;

    public static List<Integer> validTypes() {
        return Lists.newArrayList(VIEW, CLICK, SEARCH, BUY);
    }
}
