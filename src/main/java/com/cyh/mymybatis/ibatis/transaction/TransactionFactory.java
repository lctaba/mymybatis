package com.cyh.mymybatis.ibatis.transaction;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author cyh
 * @Time 2021/2/17
 */
public interface TransactionFactory {
    Transaction newTransaction(Connection connection);
    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel, boolean autocommit);
}
