package com.cyh.mymybatis.ibatis.transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author cyh
 * @Time 2021/2/22
 */
public class JDBCTransaction implements Transaction{
    private Connection connection;
    private DataSource dataSource;
    private TransactionIsolationLevel level;
    boolean autoCommit;

    public JDBCTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        this.dataSource = dataSource;
        this.level = level;
        this.autoCommit = autoCommit;
    }

    public JDBCTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if(this.connection == null){
            this.openConnection();
        }
        return this.connection;
    }

    @Override
    public void commit() throws SQLException {
        if(this.connection != null && !this.connection.getAutoCommit()){
            this.connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if(this.connection != null && !this.connection.getAutoCommit()){
            this.connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        if (this.connection != null){
            resetAutoCommit();
            this.connection.close();
        }
    }

    @Override
    public Integer getTimeout() throws SQLException {
        return null;
    }

    protected void openConnection() throws SQLException {
        this.connection = this.dataSource.getConnection();
        if(this.level != null){
            this.connection.setTransactionIsolation(this.level.getLevel());
        }
        this.connection.setAutoCommit(this.autoCommit);
    }

    protected void resetAutoCommit() throws SQLException {
        if(!this.connection.getAutoCommit()){
            this.connection.setAutoCommit(true);
        }
    }
}
