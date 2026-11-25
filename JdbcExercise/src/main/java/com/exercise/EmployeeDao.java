package com.exercise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

    public Employee findById(int id) throws SQLException {
        String sql = "SELECT employee_id, first_name, last_name, salary, department_id "
                + "FROM employees WHERE employee_id = ? ;";

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

    public List<Employee> findByDept(int deptId, int limit) throws SQLException {
        String sql = "SELECT employee_id, first_name, last_name, salary, department_id "
                + "FROM employees WHERE department_id = ? ORDER BY salary DESC LIMIT ? ;";
        List<Employee> list = new ArrayList<>();

        try(Connection conn = JdbcUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
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

    private Employee row(ResultSet rs) throws SQLException {
        return new Employee(rs.getInt(1), rs.getString(2),
        rs.getString(3), rs.getDouble(4), rs.getInt(5));
    }

    //返回新主键
    public int insert(Employee e) throws SQLException {
        String sql = "INSERT INTO employees (first_name, last_name, salary, department_id)"
                + "VALUES (?, ?, ?, ?) ;";
        int id = -1;

        try(Connection conn = JdbcUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getFirstName());
            ps.setString(2, e.getLastName());
            ps.setDouble(3, e.getSalary());
            ps.setInt(4, e.getDepartmentId());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                   id = rs.getInt(1);
                }
            }
        }
        return id;
    }

    //返回影响行数
    public int update(Employee e) throws SQLException {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, salary = ?, department_id = ? "
            + "WHERE employee_id = ? ;";

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

    //返回影响行数
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE employee_id = ?;";

        try(Connection conn = JdbcUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            return ps.executeUpdate();
        }
    }

    public void transferSalary(int fromId, int toId, double amount) throws SQLException {
        String sql1 = "UPDATE employees SET salary = salary - ? WHERE employee_id = ?;";
        String sql2 = "UPDATE employees SET salary = salary + ? WHERE employee_id = ?;";

        try(Connection conn = JdbcUtil.getConnection()) {

            conn.setAutoCommit(false);
            try {
                try(PreparedStatement ps = conn.prepareStatement(sql1)) {
                    ps.setDouble(1, amount);
                    ps.setInt(2, fromId);
                    if (ps.executeUpdate() != 1) throw new SQLException();
                }

                try(PreparedStatement ps = conn.prepareStatement(sql2)) {
                    ps.setDouble(1, amount);
                    ps.setInt(2, toId);
                    if(ps.executeUpdate() != 1) throw new SQLException();
                }
                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new SQLException("转账失败", e);
            }finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
