package com.exercise;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EmployeeQrDao {
    private final QueryRunner qr = new QueryRunner(JdbcUtil.getDataSource());

    private static final String COLS =
             "employee_id AS id, first_name AS firstName, last_name AS lastName,"
             + "salary, department_id AS departmentId";

    public Employee findById(int id) throws SQLException {
         String sql = "SELECT " + COLS + " FROM employees WHERE employee_id = ?;";
         return qr.query(sql, new BeanHandler<>(Employee.class), id);
    }

    public List<Employee> findByDept(int deptId, int limit) throws SQLException {
        String sql = "SELECT " + COLS + " FROM employees "
                + "WHERE department_id = ? ORDER BY salary DESC LIMIT ? ;";
        return qr.query(sql, new BeanListHandler<>(Employee.class), deptId, limit);
    }

    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM employees";
        Number n = qr.query(sql, new ScalarHandler<>());
        return n.longValue();
    }

    public int updateSalary(int id, double salary) throws SQLException {
        String sql = "UPDATE employees SET salary = ? WHERE employee_id = ? ;";
        return qr.update(sql, salary, id);
    }

    public void transferSalary(int fromId, int toId, double amount) throws SQLException {
        QueryRunner plain = new QueryRunner();
        try(Connection conn = JdbcUtil.getConnection()) {
            conn.setAutoCommit(false);

            try{
                int r1 = plain.update(conn,
                        "UPDATE employees SET salary = salary - ? WHERE employee_id = ?;",
                        amount, fromId);
                if(r1 != 1) throw new SQLException("转出失败：" + fromId);

                int r2 = plain.update(conn,
                        "UPDATE employees SET salary = salary + ? WHERE employee_id = ?;",
                        amount, toId);
                if(r2 != 1) throw new SQLException("转入失败：" + toId);

                conn.commit();
            }catch (SQLException e) {
                conn.rollback();
                throw e;
            }finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public int[] batchUpdateSalary(Object[][] params) throws SQLException {
        return qr.batch("UPDATE employees SET salary = ? WHERE employee_id = ?;", params);
    }
}
