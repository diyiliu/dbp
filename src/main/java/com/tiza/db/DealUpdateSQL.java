package com.tiza.db;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Description: DealUpdateSQL
 * Author: DIYILIU
 * Update: 2016-03-23 9:15
 */

@Service
public class DealUpdateSQL extends IDealSQL {

    private static ConcurrentLinkedQueue<String> updatePool = new ConcurrentLinkedQueue<>();

    private final static int BATCH_SIZE = 10;
    private final static String SQL_TYPE = "UPDATE";

    @Override
    public void run() {
        //logger.info("更新SQL线程启动...");

        for (; ; ) {
            deal(updatePool, BATCH_SIZE, SQL_TYPE);
        }
    }

    public static void putSQL(String sql) {

        updatePool.add(sql);
    }
}
