package com.exercise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

public class EmployeeQrDaoTest {
    private final EmployeeQrDao qrDao = new EmployeeQrDao();

    @Test
    @DisplayName("QueryRunner + BeanHandler 查单条")
    void qr_findById() throws SQLException {
        Employee e = qrDao.findById(100);
        assertNotNull(e);
        assertEquals(100, e.getId());
        assertEquals(24000.00, e.getSalary(), 0.001);
    }

    @Test
    @DisplayName("查不到返回 null")
    void qr_findById_notFound() throws SQLException {
        assertNull(qrDao.findById(8888));
    }

    @Test
    @DisplayName("beanListHandler 查多条")
    void qr_findByDept() throws SQLException {
        List<Employee> list = qrDao.findByDept(50,5);
        assertEquals(5, list.size());
        assertEquals(50, list.get(0).getDepartmentId());
        assertTrue(list.get(0).getSalary() >= list.get(4).getSalary());
    }

    @Test
    @DisplayName("Querrunner 事务：失败要回滚")
    void qr_transfer_rollback() throws SQLException {
        double before = qrDao.findById(206).getSalary();
        assertThrows(SQLException.class, () -> qrDao.transferSalary(206, 999999, 100.00));
        assertEquals(before, qrDao.findById(206).getSalary(), 0.001);
    }

    @Test
    @DisplayName("ScalarHandler 查总数")
    void qr_count() throws SQLException {
        assertEquals(107L, qrDao.count());
    }

    @Test
    @DisplayName("update 影响行数 + 幂等 （改完改回来）")
    void qr_updateSalary() throws SQLException {
        Employee before = qrDao.findById(205);
        assertEquals(1, qrDao.updateSalary(205, 9999.00));
        assertEquals(9999.00, qrDao.findById(205).getSalary(), 0.001);
        assertEquals(1, qrDao.updateSalary(205, before.getSalary()));
        assertEquals(before.getSalary(), qrDao.findById(205).getSalary(), 0.001);
    }
}
