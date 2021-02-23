package com.cyh.mymybatis.ibatis.executor;

import com.cyh.mymybatis.ibatis.mapper.MappedStatement;

import java.util.List;

/**
 * @Author cyh
 * @Time 2021/2/21
 */
public interface Executor {
    <E> List<E> query(MappedStatement mappedStatement, Object param);
    int update(MappedStatement mappedStatement, Object param);
    void close();
}
