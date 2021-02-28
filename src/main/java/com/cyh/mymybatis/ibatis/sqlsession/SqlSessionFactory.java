package com.cyh.mymybatis.ibatis.sqlsession;

import com.cyh.mymybatis.ibatis.executor.ExecutorType;
import com.cyh.mymybatis.ibatis.transaction.TransactionIsolationLevel;

import java.io.Closeable;
import java.sql.Connection;

public interface SqlSessionFactory {
    SqlSession openSession();
    SqlSession openSession(boolean autoCommit);
    SqlSession openSession(Connection connection);
    SqlSession openSession(TransactionIsolationLevel level);
    SqlSession openSession(ExecutorType executorType);
    SqlSession openSession(ExecutorType executorType, boolean autoCommit);
    SqlSession openSession(ExecutorType executorType, TransactionIsolationLevel level);
    SqlSession openSession(ExecutorType executorType, Connection connection);

}
