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

//        System.out.println();
//        System.out.println("--- 插入 ---");
//        Employee e = new Employee(999, "xiang", "lv", 12000, 50);
//        System.out.println("返回的主键: " + dao.insert(e));

//        Employee two = dao.findById(220);
//        System.out.println(two == null ? "没找到" : two);
//
//        System.out.println("--- 改 ---");
//        dao.update(new Employee(220, "hhh", "eee", 20000, 50));
//
//        Employee three = dao.findById(220);
//        System.out.println(three == null ? "没找到" : three);
//
        System.out.println("--- 删 ---");
        dao.delete(220);

        Employee four = dao.findById(220);
        System.out.println(four == null ? "没找到" : four);

//        System.out.println("--- 插入两个员工并转账1000 ---");
//        dao.transferSalary(dao.insert(new Employee(108,"hhhh","eeee", 5000, 60)),
//                dao.insert(new Employee(109,"aa","yy", 6000, 60)), 1000);

//        dao.transferSalary(222, 888888, 500);
//        dao.findById(222);
//        dao.findById(223);

          dao.delete(222);
          dao.delete(223);
          dao.delete(224);
          dao.delete(225);
          dao.delete(226);
          dao.delete(227);
    }
}
