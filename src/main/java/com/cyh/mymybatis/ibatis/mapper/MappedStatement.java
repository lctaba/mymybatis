package com.cyh.mymybatis.ibatis.mapper;

import com.cyh.mymybatis.ibatis.cache.Cache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author cyh
 * @Time 2021/2/16
 */
public class MappedStatement {
    private String rawSql;
    private String sql;
    private Class<?> paramType;
    private Class<?> resultType;
    private Cache cache;
    private List<String> paramName;

    public String getSql() {
        return sql;
    }
    public void setSql(String sql) {
        this.sql = sql;
    }
    public List<String> getParamName() {
        return paramName;
    }
    public void setParamName(List<String> paramName) {
        this.paramName = paramName;
    }
    public Class<?> getParamType() {
        return paramType;
    }
    public void setParamType(Class<?> paramType) {
        this.paramType = paramType;
    }
    public String getRawSql() {
        return rawSql;
    }
    public void setRawSql(String sql) {
        this.rawSql = sql;
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

    /**
     * 获取绑定参数值的sql
     * @param param 传入的对象参数
     * @return 返回绑定参数值的sql
     */
    public BoundSql getBoundSql(Object param){
        if(param.getClass() != this.paramType){
            return null;
        }
        //不是用户自定义类型
        if(param.getClass().getClassLoader() == null){
            List<Object> params = new ArrayList<>();
            params.add(param);
            return new BoundSql(this.sql,params);
        }

        Field[] fields = param.getClass().getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        List<Object> fieldValues = new ArrayList<>();
        try {
            for(Field field:fields){
                fieldNames.add(field.getName());
                field.setAccessible(true);
                fieldValues.add(field.get(param));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        List<Object> params = new ArrayList<>();

        int n = paramName == null ? 0:paramName.size();
        for(int i=0; i<n; i++){
            if(fieldNames.contains(paramName.get(i))){
                params.add(fieldValues.get(i));
            }
        }
        BoundSql boundSql = new BoundSql(this.sql == null ? "":this.sql , params);
        return boundSql;
    }
}
