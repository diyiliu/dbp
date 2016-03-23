package com.tiza.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Description: DealUpdateSQL
 * Author: DIYILIU
 * Update: 2016-03-23 9:15
 */

@Service
public class DealUpdateSQL extends IDealSQL{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static ConcurrentLinkedQueue<String> updatePool = new ConcurrentLinkedQueue<>();

    private final static int BATCH_SIZE = 10;

    @Resource
    private JdbcTemplate jdbcTemplate;


    @Override
    public void run() {
        logger.info("更新SQL线程启动...");


    }

    public static void putSQL(String sql){

        updatePool.add(sql);
    }
}
