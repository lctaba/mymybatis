package com.cyh.mymybatis.ibatis.sqlsession;

import com.cyh.mymybatis.ibatis.executor.Executor;

/**
 * @Author cyh
 * @Date 2021/2/28 16:45
 */
public class DefaultSqlSession implements SqlSession{
    private Configuration configuration;
    private Executor executor;
    private boolean autoCommit;

    public DefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
        this.configuration = configuration;
        this.executor = executor;
        this.autoCommit = autoCommit;
    }
}
