package com.tiza.db;

import com.tiza.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: IDealSQL
 * Author: DIYILIU
 * Update: 2016-03-23 9:12
 */
public abstract class IDealSQL extends Thread {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected JdbcTemplate jdbcTemplate;

    public void batch(List<String> sqlList) {
        logger.info("批处理：{}" + JacksonUtil.toJson(sqlList));
        String[] sqlArray = sqlList.toArray(new String[sqlList.size()]);

        try {
            jdbcTemplate.batchUpdate(sqlArray);
        } catch (BadSqlGrammarException e) {
            logger.error("SQL错误！[{}], 描述[{}]", e.getSql(), e.getSQLException().toString());
        }
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
