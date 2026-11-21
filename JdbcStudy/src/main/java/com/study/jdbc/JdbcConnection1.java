package com.study.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcConnection1 {
    public static void main(String[] args) throws Exception{
        String url = "jdbc:mysql://127.0.0.1:3306/myemployees"
                + "?sslMode=DISABLED&allowPublicKeyRetrieval=true"
                + "&serverTimezone=Asia/Shanghai&characterEncoding=utf8";

        try(Connection conn = DriverManager.getConnection(url, "root", "qwer")) {
            System.out.println("连上了：" + conn);
            System.out.println("当前库：" + conn.getCatalog());
            System.out.println();

            System.out.println("isValid         : " + conn.isValid(2));
            System.out.println("isClosed        : " + conn.isClosed());

            DatabaseMetaData md = conn.getMetaData();
            System.out.println("url             : " + md.getURL());
            System.out.println("user            : " + md.getUserName());
            System.out.println("product         : " + md.getDatabaseProductName()
                    + " / " + md.getDatabaseProductVersion());
            System.out.println("driver          : " + md.getDriverName()
                    + " / " + md.getDriverVersion());
            System.out.println();

            System.out.println("---切库---");
            System.out.println("原 catalog : " + conn.getCatalog());
            conn.setCatalog("exercise");
            System.out.println("切后 catalog : " + conn.getCatalog());
            conn.setCatalog("myemployees");
            System.out.println("切回 catalog : " + conn.getCatalog());

            System.out.println("---事务开关---");
            System.out.println("autoCommit  : " + conn.getAutoCommit());
            conn.setAutoCommit(false);
            System.out.println("关掉后       : " + conn.getAutoCommit());
            conn.rollback();
            conn.setAutoCommit(true);
            System.out.println("还原后       : " + conn.getAutoCommit());

            System.out.println("---查数据---");
            String sql = "SELECT employee_id, first_name, last_name, salary "
                    + "FROM employees ORDER BY employee_id LIMIT 5";

            try(Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
                while(rs.next()) {
                    int id = rs.getInt(1);
                    String name = rs.getString(2) + " " + rs.getString(3);
                    double salary = rs.getDouble(4);
                    System.out.printf("%-5d | %-14s | %.2f%n", id, name, salary);
                }
            }
            System.out.println();
            System.out.println("---PreparedStatement---");
            String sql2 = "SELECT employee_id, last_name, salary FROM employees "
                    + "WHERE department_id = ? AND salary > ? ORDER BY salary DESC LIMIT 3";

            try(PreparedStatement ps = conn.prepareStatement(sql2)) {
                ps.setInt(1, 50);
                ps.setDouble(2, 2000);

                try(ResultSet rs2 = ps.executeQuery()) {
                    while(rs2.next()) {
                        System.out.printf("%-5d | %-12s | %.2f%n",
                                rs2.getInt(1), rs2.getString(2), rs2.getDouble(3));
                    }
                }
            }
            System.out.println();
//            System.out.println("---插入---");
//            String ins = "INSERT INTO employees(first_name, last_name, email, salary, department_id, hiredate)"
//                    + "VALUES(?, ?, ?, ?, ?, ?)";
//            try(PreparedStatement ps = conn.prepareStatement(ins, Statement.RETURN_GENERATED_KEYS)) {
//                ps.setString(1, "Jdbc");
//                ps.setString(2,"Tester");
//                ps.setString(3, "JDBCTEST");
//                ps.setDouble(4,6666.00);
//                ps.setInt(5, 50);
//                ps.setString(6, "2026-11-12 00:00:00");
//
//                int rows = ps.executeUpdate();
//                System.out.println("影响行数 : " + rows);
//
//                try(ResultSet keys = ps.getGeneratedKeys()) {
//                    if(keys.next()) {
//                        System.out.println("生成的主键 : " + keys.getInt(1));
//                    }
//                }
//            }
//            System.out.println("---UPDATE---");
//            String upd = "UPDATE employees SET salary = ?, email = ? WHERE employee_id = ?";
//            try(PreparedStatement ps = conn.prepareStatement(upd)) {
//                ps.setDouble(1, 8888.88);
//                ps.setString(2, "JDBCTEST-UPDATE");
//                ps.setInt(3, 207);
//                System.out.println("UPDATE 影响行数 : " + ps.executeUpdate());
//            }

//            System.out.println("---DELETE---");
//            String del = "DELETE FROM employees WHERE last_name = ?";
//            try(PreparedStatement ps = conn.prepareStatement(del)) {
//                ps.setString(1, "Tester");
//                System.out.println("DELETE 影响行数 : " + ps.executeUpdate());
//            }

            System.out.println("---事务：回滚---");
            System.out.println("开始前 count : " + count(conn));

            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO employees(first_name, last_name, email, salary, department_id)"
                    + "VALUES (?, ?, ?, ?, ?)")) {
                ps.setString(1, "Tx");
                ps.setString(2, "Rollback");
                ps.setString(3, "Tx001");
                ps.setDouble(4, 1111.11);
                ps.setInt(5, 50);
                System.out.println("插入影响行数 : " + ps.executeUpdate());
            }

            System.out.println("插入后 count : " + count(conn));
            conn.rollback();
            System.out.println("rollback 后 : " + count(conn));

            System.out.println("---事务：提交---");
            try(PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO employees(first_name, last_name, email, salary, department_id)"
                    + "VALUES(?, ?, ?, ?, ?)")) {
                ps.setString(1, "Tx");
                ps.setString(2, "Commit");
                ps.setString(3, "TX002");
                ps.setDouble(4, 2222.22);
                ps.setInt(5, 50);
                System.out.println("插入影响行数 : " + ps.executeUpdate());
            }

            System.out.println("commit 前 count : " + count(conn));
            conn.commit();
            System.out.println("commit 后 count : " + count(conn));

            conn.setAutoCommit(true);

            System.out.println("---清理---");
            try(PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM employees WHERE email IN (?, ?)")) {
                ps.setString(1, "TX001");
                ps.setString(2, "TX002");
                System.out.println("清理影响行数 : " + ps.executeUpdate());
            }

            System.out.println("---List<Employee>---");
            String sql3 = "SELECT employee_id, first_name, last_name, salary, department_id "
                    + "FROM employees WHERE department_id = ? ORDER BY salary DESC LIMIT 5";

            List<Employee> list = new ArrayList<>();
            try(PreparedStatement ps = conn.prepareStatement(sql3)) {
                ps.setInt(1, 50);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()) {
                        Employee e = new Employee(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getDouble(4),
                                rs.getInt(5));
                        list.add(e);
                    }
                }
            }
            System.out.println("拿到条数 : " + list.size());
            for(Employee e : list) {
                System.out.println(e);
            }
        }
    }

    private static int count(Connection conn) throws SQLException {
        try(Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM employees")) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
