package com.cyh.mymybatis.ibatis.mapper;

import com.cyh.mymybatis.ibatis.cache.Cache;

/**
 * @Author cyh
 * @Time 2021/2/16
 */
public class MappedStatement {
    private String sql;
    private Class<?> paramType;
    private Class<?> resultType;
    private Cache cache;

    public Class<?> getParamType() {
        return paramType;
    }

    public void setParamType(Class<?> paramType) {
        this.paramType = paramType;
    }


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }



    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
