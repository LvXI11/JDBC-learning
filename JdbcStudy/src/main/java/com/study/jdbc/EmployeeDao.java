package com.study.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private Employee row(ResultSet rs) throws SQLException {
        return new Employee(rs.getInt(1), rs.getString(2),
                rs.getString(3), rs.getDouble(4), rs.getInt(5));
    }
}
