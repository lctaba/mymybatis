package com.cyh.mymybatis.ibatis.sqlsession;

import com.cyh.mymybatis.ibatis.executor.Executor;
import com.cyh.mymybatis.ibatis.executor.ExecutorType;
import com.cyh.mymybatis.ibatis.transaction.ManagedTransactionFactory;
import com.cyh.mymybatis.ibatis.transaction.Transaction;
import com.cyh.mymybatis.ibatis.transaction.TransactionFactory;
import com.cyh.mymybatis.ibatis.transaction.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DefaultSqlSessionFactory implements SqlSessionFactory{
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 在sqlSession创建的过程中传入connection参数时，通过Environment类获取事务类型，
     * 并创建相应的事务工厂。通过事务工厂创建事务对象并存储在Executor中。在调用方法时通过事务获取连接
     * 在sqlSession创建的过程中传入Environment参数，就是通过Environment类获取事务类型，
     * 并创建相应的事务工厂。通过事务工厂创建事务对象并存储在Executor中。在调用方法时通过事务获取连接
     * 还有一个事务等级的类，不知道什么意思
     */
    @Override
    public SqlSession openSession() {
        return doOpenSession(this.configuration.getExecutorType(),TransactionIsolationLevel.NONE,true);
    }

    @Override
    public SqlSession openSession(boolean autoCommit) {
        return doOpenSession(this.configuration.getExecutorType(),TransactionIsolationLevel.NONE,autoCommit);
    }

    @Override
    public SqlSession openSession(Connection connection) {
        return doOpenSession(this.configuration.getExecutorType(),connection);
    }

    @Override
    public SqlSession openSession(TransactionIsolationLevel level) {
        return doOpenSession(this.configuration.getExecutorType(),level,true);
    }

    @Override
    public SqlSession openSession(ExecutorType executorType) {
        return doOpenSession(executorType,TransactionIsolationLevel.NONE,true);
    }

    @Override
    public SqlSession openSession(ExecutorType executorType, boolean autoCommit) {
        return doOpenSession(executorType,TransactionIsolationLevel.NONE,autoCommit);
    }

    @Override
    public SqlSession openSession(ExecutorType executorType, TransactionIsolationLevel level) {
        return doOpenSession(executorType,level,true);
    }

    @Override
    public SqlSession openSession(ExecutorType executorType, Connection connection) {
        return doOpenSession(executorType,connection);
    }

    public SqlSession doOpenSession(ExecutorType executorType, TransactionIsolationLevel level, Boolean autoCommit){
        Environment environment = this.configuration.getEnvironment();
        DefaultSqlSession sqlSession = null;
        try {
            TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
            DataSource dataSource = environment.getDataSource();
            Transaction transaction = transactionFactory.newTransaction(dataSource,level,autoCommit);
            Executor executor = this.configuration.newExecutor(executorType,transaction);
            sqlSession = new DefaultSqlSession(this.configuration,executor,autoCommit);
        }catch (Exception e){
            e.printStackTrace();
        }
        return sqlSession;
    }

    public SqlSession doOpenSession(ExecutorType executorType, Connection connection){
        Environment environment = this.configuration.getEnvironment();
        DefaultSqlSession sqlSession = null;
        try {
            boolean autoCommit;
            try {
                autoCommit = connection.getAutoCommit();
            }catch (SQLException e){
                autoCommit = true;
            }
            TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
            DataSource dataSource = environment.getDataSource();
            Transaction transaction = transactionFactory.newTransaction(connection);
            Executor executor = this.configuration.newExecutor(executorType,transaction);
            sqlSession = new DefaultSqlSession(this.configuration,executor,autoCommit);
        }catch (Exception e){
            e.printStackTrace();
        }
        return sqlSession;
    }

    private TransactionFactory getTransactionFactoryFromEnvironment(Environment environment) {
        TransactionFactory transactionFactory = environment.getTransactionFactory();
        return transactionFactory == null ? new ManagedTransactionFactory() : transactionFactory;
    }
}
