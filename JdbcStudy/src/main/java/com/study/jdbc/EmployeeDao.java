package com.study.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
    public List<Employee> findByDept(int deptId, int limit) throws SQLException {
        String sql = "SELECT employee_id, first_name, last_name, salary, department_id "
                + "FROM employees WHERE department_id = ? ORDER BY salary DESC LIMIT ?";
        List<Employee> list = new ArrayList<>();
        try(Connection conn = JdbcUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, deptId);
            ps.setInt(2, limit);

            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    list.add(row(rs));
                }
            }
        }
        return list;
    }

    public Employee findById(int id) throws SQLException {
        String sql = "SELECT employee_id, first_name, last_name, salary, department_id "
                + "FROM employees WHERE employee_id = ?";
        try(Connection conn = JdbcUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return row(rs);
                }
            }
        }
        return null;
    }

    public int insert(Employee e) throws SQLException {
        String sql = "INSERT INTO employees (first_name, last_name, salary, department_id) "
                + "VALUES (?, ?, ?, ?);";

        try(Connection conn = JdbcUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getFirstName());
            ps.setString(2, e.getLastName());
            ps.setDouble(3, e.getSalary());
            ps.setInt(4, e.getDepartmentId());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    return rs.getInt(1);
                }
            }
            throw new SQLException("插入成功但没拿到主键");
        }
    }

    public int update(Employee e) throws SQLException {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, salary = ?, department_id = ? "
                + "WHERE employee_id = ?;";

        try(Connection conn = JdbcUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getFirstName());
            ps.setString(2, e.getLastName());
            ps.setDouble(3, e.getSalary());
            ps.setInt(4, e.getDepartmentId());
            ps.setInt(5, e.getId());
            return ps.executeUpdate();

        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE employee_id = ? ;";

        try(Connection conn = JdbcUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    private Employee row(ResultSet rs) throws SQLException {
        return new Employee(rs.getInt(1), rs.getString(2),
                rs.getString(3), rs.getDouble(4), rs.getInt(5));
    }

    public void transferSalary(int fromId, int toId, double amount) throws SQLException {
        String sql1 = "UPDATE employees SET salary = salary - ? WHERE employee_id = ? ;";
        String sql2 = "UPDATE employees SET salary = salary + ? WHERE employee_id = ? ;";

        try(Connection conn = JdbcUtil.getConnection()) {
             conn.setAutoCommit(false);

             try {
                 try( PreparedStatement ps = conn.prepareStatement(sql1)) {
                     ps.setDouble(1, amount);
                     ps.setInt(2, fromId);

                     int rows = ps.executeUpdate();
                     if(rows != 1) throw new SQLException("id:" + fromId + "转账失败");
                 }

                 try(PreparedStatement ps = conn.prepareStatement(sql2)) {
                     ps.setDouble(1, amount);
                     ps.setInt(2, toId);

                     int rows = ps.executeUpdate();
                     if(rows != 1) throw new SQLException("id:" + toId + "转账失败");
                     conn.commit();
                 }
             } catch (SQLException e) {
                 conn.rollback();
                 System.out.println("转帐失败");
                 throw e;
             } finally {
                conn.setAutoCommit(true);
             }
        }
    }
}
