package com.tiza.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Description: IDealSQL
 * Author: DIYILIU
 * Update: 2016-03-23 9:12
 */
public abstract class IDealSQL extends Thread {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected JdbcTemplate jdbcTemplate;

    public void batch(List<String> sqlList, String type) {
        String[] sqlArray = sqlList.toArray(new String[sqlList.size()]);
        Date t1 = new Date();
        Date t2 = null;
        try {
            jdbcTemplate.batchUpdate(sqlArray);
            t2 = new Date();
            logger.info("批处理： 类型[{}]，数量[{}]，耗时[{}毫秒]",
                    type, sqlArray.length, (t2.getTime() - t1.getTime()));
        } catch (BadSqlGrammarException e) {
            t2 = new Date();
            logger.error("SQL错误！类型[{}]，耗时[{}毫秒], SQL[{}]， 描述[{}]", type, (t2.getTime() - t1.getTime()), e.getSql(), e.getSQLException().getMessage());
        }
    }

    public void deal(ConcurrentLinkedQueue<String> sqlPool, int maxSize, String type) {
        List<String> sqlList = new ArrayList<>(maxSize);

        while (!sqlPool.isEmpty()) {

            String sql = sqlPool.poll();
            sqlList.add(sql);

            if (sqlList.size() == maxSize) {
                batch(sqlList, type);
                sqlList.clear();
            }
        }

        if (sqlList.size() > 0) {
            batch(sqlList, type);
            sqlList.clear();
        }
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
