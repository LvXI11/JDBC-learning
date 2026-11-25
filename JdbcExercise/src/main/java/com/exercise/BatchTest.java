package com.exercise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BatchTest {
    public static void batchInsert() throws SQLException {
        String sql1 = "DELETE FROM exercise.batch_emp";
        String sql2 = "INSERT INTO exercise.batch_emp (name, salary) "
                + "VALUES (?, ?);";
        String sql3 = "SELECT COUNT(*) FROM exercise.batch_emp;";

        try(Connection conn = JdbcUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try(PreparedStatement ps = conn.prepareStatement(sql1)) {
                    ps.executeUpdate();
                }

                try(PreparedStatement ps = conn.prepareStatement(sql2)) {
                    long start = System.currentTimeMillis();
                    for (int i = 0; i < 10000; i++) {
                        ps.setString(1, "emp" + i);
                        ps.setDouble(2, 1000 + i);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    long end = System.currentTimeMillis();
                    System.out.println("花费时间:" + (end - start));
                    conn.commit();
                }

                try(PreparedStatement ps = conn.prepareStatement(sql3)) {
                    try(ResultSet rs = ps.executeQuery()) {
                        if(rs.next()) {
                            System.out.println("成功插入" + rs.getInt(1) + "行");
                        }
                    }
                }
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public static void oneByOneInsert() throws SQLException {
        String sql1 = "DELETE FROM exercise.batch_emp";
        String sql2 = "INSERT INTO exercise.batch_emp (name, salary) "
                + "VALUES (?, ?);";
        String sql3 = "SELECT COUNT(*) FROM exercise.batch_emp;";

        try(Connection conn = JdbcUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try(PreparedStatement ps = conn.prepareStatement(sql1)) {
                    ps.executeUpdate();
                }

                try(PreparedStatement ps = conn.prepareStatement(sql2)) {
                    long start = System.currentTimeMillis();
                    for (int i = 0; i < 10000; i++) {
                        ps.setString(1, "emp" + i);
                        ps.setDouble(2, 1000 + i);
                        ps.executeUpdate();
                    }
                    long end = System.currentTimeMillis();
                    System.out.println("花费时间:" + (end - start));
                }
                try(PreparedStatement ps = conn.prepareStatement(sql3)) {
                    try(ResultSet rs = ps.executeQuery()) {
                        if(rs.next()) {
                            System.out.println("成功插入" + rs.getInt(1) + "行");
                        }
                    }
                }
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        BatchTest.batchInsert();
        BatchTest.oneByOneInsert();
    }
}
