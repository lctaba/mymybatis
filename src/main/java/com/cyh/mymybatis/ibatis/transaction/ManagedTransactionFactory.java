package com.cyh.mymybatis.ibatis.transaction;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author cyh
 * @Time 2021/2/18
 */
public class ManagedTransactionFactory implements TransactionFactory{
    private boolean closeTransaction = true;

    @Override
    public Transaction newTransaction(Connection connection) {
        return new ManagedTransaction(connection,this.closeTransaction);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel, boolean autocommit) {
        return new ManagedTransaction(dataSource,transactionIsolationLevel,this.closeTransaction);
    }
}
