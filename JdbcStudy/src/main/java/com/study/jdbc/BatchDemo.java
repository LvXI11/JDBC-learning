package com.study.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchDemo {

    public static void main(String[] args) throws SQLException {
        String sql = "INSERT INTO batch_emp (name, salary) VALUES (?, ?) ;";
//
//        try(Connection conn = JdbcUtil.getConnection();
//            PreparedStatement ps = conn.prepareStatement(sql)) {
//            conn.setAutoCommit(false);
//
//            long start = System.currentTimeMillis();
//            for(int i = 1;i <= 100000; i++) {
//                ps.setString(1, "emp" + i);
//                ps.setDouble(2, 1000 + i);
//                ps.addBatch();
//            }
//            int[] array = ps.executeBatch();
//            conn.commit();
//            long end = System.currentTimeMillis();
//
//            System.out.println("花费时间:" + (end - start));
//            System.out.println("数组长度:" + array.length);
//            for(int i = 0;i < 5; i++) {
//                System.out.println(array[i]);
//            }
//        }
//          URL = false
//        花费时间:5766
//        数组长度:100000
//        1
//        1
//        1
//        1
//        1

//        URL = true
//        花费时间:1046
//        数组长度:100000
//                -2
//                -2
//                -2
//                -2
//                -2

        try(Connection conn = JdbcUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            long start = System.currentTimeMillis();
            for(int i = 1;i <= 100000; i++) {
                ps.setString(1, "emp" + i);
                ps.setDouble(2, 1000 + i);
               ps.executeUpdate();
            }
            conn.commit();
            long end = System.currentTimeMillis();

            System.out.println("花费时间:" + (end - start));
        }
//        花费时间:5531 URL = false
        //        花费时间:5660     URL = ture
    }
}
