package com.cyh.mymybatis.ibatis.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Author cyh
 * @Date 2021/2/24 21:33
 */
public class BoundSql {
    private String sql;
    List<Object> params;

    public BoundSql(String sql, List<Object> params) {
        this.sql = sql;
        this.params = params;
    }

    @Override
    public String toString() {
        return "BoundSql{" +
                "sql='" + sql + '\'' +
                ", params=" + params +
                '}';
    }

    /**
     * 通过sql语句和参数列表构造PreparedStatement
     * @param connection 数据库连接
     * @return 可执行的PreparedStatement
     */
    public PreparedStatement getPreparedStatement(Connection connection){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(this.sql);
            int n = params.size();
            for (int i=0; i<n; i++){
                preparedStatement.setObject(i+1,params.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }
}
