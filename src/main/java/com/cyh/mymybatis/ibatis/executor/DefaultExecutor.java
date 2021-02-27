package com.cyh.mymybatis.ibatis.executor;

import com.cyh.mymybatis.ibatis.cache.Cache;
import com.cyh.mymybatis.ibatis.mapper.BoundSql;
import com.cyh.mymybatis.ibatis.mapper.MappedStatement;
import com.cyh.mymybatis.ibatis.sqlsession.Configuration;
import com.cyh.mymybatis.ibatis.transaction.Transaction;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author cyh
 * @Time 2021/2/21
 */
public class DefaultExecutor implements Executor{
    private Transaction transaction;
    private Configuration configuration;
    private Cache cache;

    @Override
    public <E> List<E> query(MappedStatement mappedStatement, Object param) {
        List<E> resultSet = new ArrayList<E>();
        return resultSet;
    }

    @Override
    public int update(MappedStatement mappedStatement, Object param) {
        return 0;
    }

    @Override
    public void close() {

    }

    public <E> List<E> queryFromDatabase(MappedStatement mappedStatement, Object param){
        BoundSql boundSql = mappedStatement.getBoundSql(param);
        return null;
    }
}
