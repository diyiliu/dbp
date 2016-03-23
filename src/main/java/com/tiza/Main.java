package com.tiza;

import com.tiza.db.DealInsertSQL;
import com.tiza.db.DealUpdateSQL;
import com.tiza.util.SpringUtil;

/**
 * Description: Main
 * Author: DIYILIU
 * Update: 2016-03-22 16:26
 */

public class Main {

    public static void main(String[] args) {

        SpringUtil.init();

        startDealSQL();
    }

    public static void startDealSQL(){

        new DealInsertSQL().start();
        new DealUpdateSQL().start();
    }
}
