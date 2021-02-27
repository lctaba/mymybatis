package com.cyh.mymybatis.ibatis.transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author cyh
 * @Time 2021/2/22
 */
public class ManagedTransaction implements Transaction{
    private Connection connection;
    private DataSource dataSource;
    private TransactionIsolationLevel level;
    private final boolean closeConnection;

    public ManagedTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean closeConnection) {
        this.dataSource = dataSource;
        this.level = level;
        this.closeConnection = closeConnection;
    }

    public ManagedTransaction(Connection connection, boolean closeConnection) {
        this.connection = connection;
        this.closeConnection = closeConnection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if(this.connection == null){
            openConnection();
        }
        return this.connection;
    }

    @Override
    public void commit() throws SQLException {

    }

    @Override
    public void rollback() throws SQLException {

    }

    @Override
    public void close() throws SQLException {
        if (this.closeConnection && this.connection != null) {
            this.connection.close();
        }
    }

    @Override
    public Integer getTimeout() throws SQLException {
        return null;
    }

    protected void openConnection() throws SQLException {
        this.connection = this.dataSource.getConnection();
        if (this.level != null) {
            this.connection.setTransactionIsolation(this.level.getLevel());
        }
    }
}
