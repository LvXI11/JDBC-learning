package com.study.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcDemo {
    public static void main(String[] args) throws Exception {
        try(Connection conn = JdbcUtil.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM employees")) {
            rs.next();
            System.out.println("员工总数 : " + rs.getInt(1));
        }

        System.out.println("---连两次---");
        try(Connection c1 = JdbcUtil.getConnection();
            Connection c2 = JdbcUtil.getConnection()) {
            System.out.println("c1 = " + System.identityHashCode(c1));
            System.out.println("c2 = " + System.identityHashCode(c2));
            System.out.println("是同一条连接？ " + (c1 == c2));
        }

        EmployeeDao dao = new EmployeeDao();
        System.out.println("--- findByDept(50,5) ---");
        for(Employee e : dao.findByDept(50, 5)) {
            System.out.println(e);
        }

        System.out.println("-- findById(100) ---");
        Employee one = dao.findById(100);
        System.out.println(one == null ? "没找到" : one);

        System.out.println("--- findById(999) ---");
        Employee none = dao.findById(999);
        System.out.println(none == null ? "没找到（返回null）" : none);
    }
}
