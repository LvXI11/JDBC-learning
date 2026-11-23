package com.study.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/exercise"
            + "?sslMode=DISABLED&allowPublicKeyRetrieval=true"
            + "&serverTimezone=Asia/Shanghai&characterEncoding=utf8"
            + "&rewriteBatchedStatements=false";
    private static final String USER = "root";
    private static final String PWD = "qwer";

    public JdbcUtil() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PWD);
    }
}
