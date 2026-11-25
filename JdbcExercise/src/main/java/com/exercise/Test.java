package com.exercise;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        EmployeeDao dao = new EmployeeDao();

        System.out.println("--- 按工号查 ---");
        System.out.println(dao.findById(100));
        System.out.println(dao.findById(999));

        System.out.println("--- 按部门查 ---");
        System.out.println(dao.findByDept(50, 5));
        System.out.println(dao.findByDept(99999, 5));

        System.out.println("--- insert、update、delete ---");

        Employee e = new Employee(0,"Demo", "Insert", 5000.00, 50);
        System.out.println("---insert---");
        int id = dao.insert(e);
        e.setId(id);
        System.out.println("id = " + id);
        System.out.println(dao.findById(id));
        System.out.println("---update---");
        System.out.println("影响行数 = " + dao.update(e));
        System.out.println(dao.findById(id));
        System.out.println("---delete---");
        System.out.println("影响行数 = " + dao.delete(id));
        System.out.println(dao.findById(id));

    }
}
