package com.cyh.mymybatis.ibatis.datasource;

import java.util.Properties;

public class PooledDataSource extends UnpooledDataSource{
    protected int poolMaximumActiveConnections = 10;
    protected int poolMaximumIdleConnections = 5;
    protected int poolMaximumCheckoutTime = 20000;
    protected int poolTimeToWait = 20000;

    public PooledDataSource(String driver,String url,String username,String pwd){
        super(driver, url, username, pwd);
    }

    public PooledDataSource(String driver, String url, String username, String pwd, ClassLoader driverClassLoader) {
        super(driver, url, username, pwd, driverClassLoader);
    }

    public PooledDataSource(String driver, String url, Properties properties, ClassLoader driverClassLoader) {
        super(driver, url, properties, driverClassLoader);
    }

    public PooledDataSource() {
    }

    public PooledDataSource(String driver, String url, Properties properties) {
        super(driver, url, properties);
    }


}
