package com.cyh.mymybatis.ibatis.datasource;

public class PooledDataSource extends UnpooledDataSource{
    public PooledDataSource(String driver,String url,String username,String pwd){
        super(driver, url, username, pwd);
    }
}
