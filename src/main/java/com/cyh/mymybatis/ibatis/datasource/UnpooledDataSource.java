package com.cyh.mymybatis.ibatis.datasource;

import com.cyh.mymybatis.ibatis.sqlsession.Environment;
import com.cyh.mymybatis.ibatis.transaction.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class UnpooledDataSource implements DataSource {
    private String driver;
    private String url;
    private String username;
    private String pwd;
    private Properties properties;
    private static Map<String, Driver> registeredDrivers = new ConcurrentHashMap<>();
    private ClassLoader driverClassLoader;


    public UnpooledDataSource(String driver, String url, String username, String pwd, ClassLoader driverClassLoader) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.pwd = pwd;
        this.driverClassLoader = driverClassLoader;
    }

    public UnpooledDataSource(String driver, String url, Properties properties, ClassLoader driverClassLoader) {
        this.driver = driver;
        this.url = url;
        this.properties = properties;
        this.driverClassLoader = driverClassLoader;
    }

    public UnpooledDataSource(String driver, String url, Properties properties) {
        this.driver = driver;
        this.url = url;
        this.properties = properties;
    }

    public UnpooledDataSource() {
    }

    public UnpooledDataSource(String driver,String url,String username,String pwd){
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.pwd = pwd;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.doGetConnection(this.username, this.pwd);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return this.doGetConnection(username,pwd);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(this.getClass().getName() + " is not a wrapper.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        DriverManager.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger("global");
    }

    private Connection doGetConnection(String username, String pwd) throws SQLException {
        Properties properties = new Properties();
        if (this.properties != null){
            properties.putAll(this.properties);
        }

        if(username != null){
            properties.setProperty("user",username);
        }

        if(pwd != null){
            properties.setProperty("password",pwd);
        }

        return this.doGetConnection(properties);
    }

    private Connection doGetConnection(Properties properties) throws SQLException {
        this.initializeDriver();
        return DriverManager.getConnection(this.url,properties);
    }

    private synchronized void initializeDriver(){
        if(!registeredDrivers.containsKey(this.driver)){
            try {
                Class driverType;
                if(this.driverClassLoader != null){
                    driverType = Class.forName(this.driver,true,this.driverClassLoader);
                }else {
                    driverType = Class.forName(this.driver,true,null);
                }
                Driver driver = (Driver)driverType.newInstance();
                DriverManager.registerDriver(new UnpooledDataSource.DriverProxy(driver));
                registeredDrivers.put(this.driver,driver);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    static {
        Enumeration<Driver> drivers = DriverManager.getDrivers();

        while(drivers.hasMoreElements()) {
            Driver driver = (Driver)drivers.nextElement();
            registeredDrivers.put(driver.getClass().getName(), driver);
        }

    }

    private static class DriverProxy implements Driver {
        private Driver driver;

        DriverProxy(Driver d) {
            this.driver = d;
        }

        public boolean acceptsURL(String u) throws SQLException {
            return this.driver.acceptsURL(u);
        }

        public Connection connect(String u, Properties p) throws SQLException {
            return this.driver.connect(u, p);
        }

        public int getMajorVersion() {
            return this.driver.getMajorVersion();
        }

        public int getMinorVersion() {
            return this.driver.getMinorVersion();
        }

        public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
            return this.driver.getPropertyInfo(u, p);
        }

        public boolean jdbcCompliant() {
            return this.driver.jdbcCompliant();
        }

        public Logger getParentLogger() {
            return Logger.getLogger("global");
        }
    }
}
