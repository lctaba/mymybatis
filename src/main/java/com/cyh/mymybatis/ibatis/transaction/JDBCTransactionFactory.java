package com.cyh.mymybatis.ibatis.transaction;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author cyh
 * @Time 2021/2/18
 */
public class JDBCTransactionFactory implements TransactionFactory{
    @Override
    public Transaction newTransaction(Connection connection) {
        return new JDBCTransaction(connection);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel, boolean autocommit) {
        return new JDBCTransaction(dataSource,transactionIsolationLevel,autocommit);
    }
}
