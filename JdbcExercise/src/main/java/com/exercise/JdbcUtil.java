package com.exercise;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtil {
    private static DataSource DATA_SOURCE;

    static {
        Properties props = new Properties();
        try(InputStream in = JdbcUtil.class.getClassLoader().getResourceAsStream("druid.properties")) {
            if(in == null) {
                throw new RuntimeException("classpath 根目录下找不到 druid.properties");
            }
            props.load(in);
            DATA_SOURCE = DruidDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            throw new RuntimeException("初始化连接池失败", e);
        }
    }

    private JdbcUtil() {}

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}
