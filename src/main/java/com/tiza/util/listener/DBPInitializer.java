package com.tiza.util.listener;

import com.tiza.db.IDealSQL;
import com.tiza.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description: DBPInitializer
 * Author: DIYILIU
 * Update: 2016-03-23 14:29
 */
public class DBPInitializer implements ApplicationListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        logger.info("SQL处理初始化...");

        Map dealSQLMap =  SpringUtil.getBeansOfType(IDealSQL.class);

        for (Iterator iter = dealSQLMap.keySet().iterator(); iter.hasNext();){
            Object key = iter.next();
            IDealSQL dealSQL = (IDealSQL) dealSQLMap.get(key);
            dealSQL.start();
        }
    }
}
