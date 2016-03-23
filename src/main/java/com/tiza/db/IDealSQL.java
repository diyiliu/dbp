package com.tiza.db;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Description: IDealSQL
 * Author: DIYILIU
 * Update: 2016-03-23 9:12
 */
public abstract class IDealSQL extends Thread{

    public void batch(JdbcTemplate jdbcTemplate, List<String> sqlList){

        String[] sqlArray = (String[]) sqlList.toArray();

        jdbcTemplate.batchUpdate(sqlArray);
    }
}
