package com.cyh.mymybatis.ibatis.transaction;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author cyh
 * @Time 2021/2/22
 */
public class JDBCTransaction implements Transaction{
    private Connection connection;
    private DataSource dataSource;

}
