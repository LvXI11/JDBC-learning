package com.exercise;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtil {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;


    static {
        Properties props = new Properties();
        try(InputStream in = JdbcUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if(in == null) {
                throw new RuntimeException("classpath 根目录下找不到 db.properties");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("读取 db.properties 失败", e);
        }

        URL = props.getProperty("url");
        USER = props.getProperty("user");
        PASSWORD = props.getProperty("password");
    }

    private JdbcUtil() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
