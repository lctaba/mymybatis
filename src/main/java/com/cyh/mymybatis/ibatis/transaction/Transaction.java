package com.cyh.mymybatis.ibatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author cyh
 * @Time 2021/2/22
 */
public interface Transaction {
    Connection getConnection() throws SQLException;
    void commit() throws SQLException;
    void rollback() throws SQLException;
    void close() throws SQLException;
    Integer getTimeout() throws SQLException;
}
