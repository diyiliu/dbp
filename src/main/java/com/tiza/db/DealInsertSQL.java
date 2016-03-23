package com.tiza.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Description: DealInsertSQL
 * Author: DIYILIU
 * Update: 2016-03-23 9:13
 */

@Service
public class DealInsertSQL extends IDealSQL{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static ConcurrentLinkedQueue<String> insertPool = new ConcurrentLinkedQueue<>();

    private final static int BATCH_SIZE = 20;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run() {
        logger.info("插入SQL线程启动...");

        List<String> sqlList = new ArrayList<>(BATCH_SIZE);

        for (;;){
            while (!insertPool.isEmpty()){

                String sql = insertPool.poll();
                sqlList.add(sql);

                if (sqlList.size() == BATCH_SIZE){
                    batch(jdbcTemplate, sqlList);
                    sqlList.clear();
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void putSQL(String sql){

        insertPool.add(sql);
    }
}
