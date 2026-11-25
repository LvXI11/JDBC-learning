package com.exercise;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;

public class EmployeeDaoTest {
    private final EmployeeDao dao = new EmployeeDao();

    @Test
    @DisplayName("findById 查得到真实员工")
    void findById_exists() throws SQLException {
        Employee e = dao.findById(100);
        assertNotNull(e);
        assertEquals(100, e.getId());
        assertEquals(24000.00, e.getSalary(), 0.001);
    }

    @Test
    @DisplayName("findById 查不到真实员工")
    void findById_notFound() throws SQLException {
        assertNull(dao.findById(999));
    }

    @Test
    @DisplayName("转账成功：两边一减一加，最后转回来复原")
    void transfer_commit() throws SQLException {
        double from0 = dao.findById(205).getSalary();   // 12000
        double to0   = dao.findById(206).getSalary();   // 8300

        dao.transferSalary(205, 206, 200.00);

        assertEquals(from0 - 200, dao.findById(205).getSalary(), 0.001);
        assertEquals(to0   + 200, dao.findById(206).getSalary(), 0.001);

        // 复原，保证这个测试跑 100 次结果都一样
        dao.transferSalary(206, 205, 200.00);
        assertEquals(from0, dao.findById(205).getSalary(), 0.001);
        assertEquals(to0,   dao.findById(206).getSalary(), 0.001);
    }

    @Test
    @DisplayName("转账失败：抛异常，且转出方工资不变（回滚生效）")
    void transfer_rollback() throws SQLException {
        double before = dao.findById(205).getSalary();

        assertThrows(SQLException.class,
                () -> dao.transferSalary(205, 9999, 200.00));

        assertEquals(before, dao.findById(205).getSalary(), 0.001);
    }
}
