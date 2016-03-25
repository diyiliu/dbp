package com.tiza.db;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Description: DealInsertSQL
 * Author: DIYILIU
 * Update: 2016-03-23 9:13
 */

@Service
public class DealInsertSQL extends IDealSQL {

    private static ConcurrentLinkedQueue<String> insertPool = new ConcurrentLinkedQueue<>();

    private final static int BATCH_SIZE = 20;
    private final static String SQL_TYPE = "INSERT";

    @Override
    public void run() {
        //logger.info("插入SQL线程启动...");

        for (; ; ) {
            deal(insertPool, BATCH_SIZE, SQL_TYPE);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void putSQL(String sql) {

        insertPool.add(sql);
    }
}
