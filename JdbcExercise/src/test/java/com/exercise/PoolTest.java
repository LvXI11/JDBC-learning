package com.exercise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class PoolTest {

    @Test
    @DisplayName("连接来着Druid池，且三条代理对象不同")
    void pool_reuse() throws SQLException {
        for(int i = 1; i <= 3; i++) {
            try(Connection conn = JdbcUtil.getConnection()) {
                System.out.println(i + " 代理对象 hash = " + System.identityHashCode(conn));
                System.out.println(i + " 连接实现类  = " + conn.getClass().getName());
                System.out.println(i + " 连接 toString = " + conn);
            }
        }
    }

    @Test
    @DisplayName("池预建连接验证")
    void pool_processlist() throws Exception {
        for (int i = 1; i <= 3; i++) {
            try (Connection conn = JdbcUtil.getConnection()) {
                System.out.println(i + " -> " + conn.getClass().getName());
            }
        }
        System.out.println("现在去 DBeaver 跑 SHOW PROCESSLIST，15 秒后结束...");
        Thread.sleep(15000);
    }
}
