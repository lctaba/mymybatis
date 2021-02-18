package com.cyh.mymybatis.ibatis.sqlsession;

import com.cyh.mymybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

/**
 * @Author cyh
 * @Time 2021/2/17
 */
public class Environment {
    //环境名
    private String id;
    //数据源信息
    private DataSource dataSource;
    //事务管理工厂信息
    private TransactionFactory transactionFactory;

    public Environment(){}

    public Environment(Environment.Builder builder){
        this.id = builder.id;
        this.dataSource = builder.dataSource;
        this.transactionFactory = builder.transactionFactory;
    }

    public static Environment.Builder builder(){
        return new Environment.Builder();
    }

    public static class Builder{
        private String id;
        private DataSource dataSource;
        private TransactionFactory transactionFactory;

        public Environment build(){
            return new Environment(this);
        }

        public Environment.Builder id(String id){
            this.id = id;
            return this;
        }

        public Environment.Builder transactionFactory(TransactionFactory transactionFactory){
            this.transactionFactory = transactionFactory;
            return this;
        }

        public Environment.Builder dataSource(DataSource dataSource){
            this.dataSource = dataSource;
            return this;
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "id='" + id + '\'' +
                    ", dataSource=" + dataSource +
                    ", transactionFactory=" + transactionFactory +
                    '}';
        }
    }
}
