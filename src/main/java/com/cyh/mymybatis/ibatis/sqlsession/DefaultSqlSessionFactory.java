package com.cyh.mymybatis.ibatis.sqlsession;

import java.io.IOException;

public class DefaultSqlSessionFactory implements SqlSessionFactory{

    @Override
    public SqlSession openSession() {
        return null;
    }
    /**
     * 在sqlSession创建的过程中传入connection参数时，通过Environment类获取事务类型，
     * 并创建相应的事务工厂。通过事务工厂创建事务对象并存储在Executor中。在调用方法时通过事务获取连接
     * 在sqlSession创建的过程中传入Environment参数，就是通过Environment类获取事务类型，
     * 并创建相应的事务工厂。通过事务工厂创建事务对象并存储在Executor中。在调用方法时通过事务获取连接
     * 还有一个事务等级的类，不知道什么意思
     */
}
