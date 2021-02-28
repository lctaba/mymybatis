package com.cyh.mymybatis.ibatis.sqlsession;

import com.cyh.mymybatis.ibatis.Log.LogType;
import com.cyh.mymybatis.ibatis.datasource.PooledDataSource;
import com.cyh.mymybatis.ibatis.datasource.UnpooledDataSource;
import com.cyh.mymybatis.ibatis.executor.DefaultExecutor;
import com.cyh.mymybatis.ibatis.executor.Executor;
import com.cyh.mymybatis.ibatis.executor.ExecutorType;
import com.cyh.mymybatis.ibatis.mapper.MappedStatement;
import com.cyh.mymybatis.ibatis.mapper.MapperRegistry;
import com.cyh.mymybatis.ibatis.transaction.JDBCTransactionFactory;
import com.cyh.mymybatis.ibatis.transaction.ManagedTransactionFactory;
import com.cyh.mymybatis.ibatis.transaction.Transaction;
import com.cyh.mymybatis.ibatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Configuration {
    //二级缓存
    private boolean secondLevelCacheEnabled;
    //一级缓存
    private boolean firstLevelCacheEnabled;
    //延迟加载
    private boolean lazyLoadingEnabled;
    //同一个语句返回多种结果集
    private boolean multipleResultSetEnabled;
    //executor的类型
    private ExecutorType executorType;
    //超时时间
    private Integer timeout;
    //启用的日志类型
    private LogType logType;
    //用于给不同的Dao接口注册代理类
    private MapperRegistry mapperRegistry;
    //用于储存sql操作语句
    private Map<String,MappedStatement> mappedStatements;
    //用户储存数据库环境
    private Environment environment;

    public Configuration() {
        this.firstLevelCacheEnabled = true;
        this.secondLevelCacheEnabled = false;
        this.lazyLoadingEnabled = false;
        this.multipleResultSetEnabled = false;
        this.executorType = ExecutorType.Default;
        this.timeout = 1000;
        this.logType = LogType.Log4j;
        this.mapperRegistry = new MapperRegistry();
        this.mappedStatements = new HashMap<>();
    }

    public void setSecondLevelCacheEnabled(boolean secondLevelCacheEnabled) {
        this.secondLevelCacheEnabled = secondLevelCacheEnabled;
    }

    public void setFirstLevelCacheEnabled(boolean firstLevelCacheEnabled) {
        this.firstLevelCacheEnabled = firstLevelCacheEnabled;
    }

    public void setLazyLoadingEnabled(boolean lazyLoadingEnabled) {
        this.lazyLoadingEnabled = lazyLoadingEnabled;
    }

    public void setMultipleResultSetEnabled(boolean multipleResultSetEnabled) {
        this.multipleResultSetEnabled = multipleResultSetEnabled;
    }

    public void setExecutorType(ExecutorType executorType) {
        this.executorType = executorType;
    }

    public ExecutorType getExecutorType() { return executorType; }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public void setMapperRegistry(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    public Map<String, MappedStatement> getMappedStatements() {
        return mappedStatements;
    }

    public void setMappedStatements(Map<String, MappedStatement> mappedStatements) { this.mappedStatements = mappedStatements; }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Executor newExecutor(ExecutorType executorType, Transaction transaction){
        Executor executor = null;
        if(executorType == ExecutorType.Default){
            executor = new DefaultExecutor(transaction,this);
        }
        if(this.secondLevelCacheEnabled){

        }
        return executor;
    }



















    /**
     * 读取配置文件
     * @param file 传入的配置文件
     */
    public void parseConfig(File file){
        try{
            SAXReader reader = new SAXReader();
            Document doc = reader.read(file);
            Element root = doc.getRootElement();
            parseConfigSettings(root.element("settings"));
            parseEnvironments(root.element("environments"));
        } catch (DocumentException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析配置文件中的setting节点
     * @param settings settings节点元素
     */
    public void parseConfigSettings(Element settings){
        List<Element> elements = settings.elements();
        for(Element e : elements){
            String name = e.attributeValue("name");
            switch (name){
                case "firstLevelCacheEnabled":
                    this.firstLevelCacheEnabled = Boolean.parseBoolean(e.attributeValue("value"));
                    break;
                case "secondLevelCacheEnabled":
                    this.secondLevelCacheEnabled = Boolean.parseBoolean(e.attributeValue("value"));
                    break;
                case "lazyLoadingEnabled":
                    this.lazyLoadingEnabled = Boolean.parseBoolean(e.attributeValue("value"));
                    break;
                case "multipleResultSetEnabled":
                    this.multipleResultSetEnabled = Boolean.parseBoolean(e.attributeValue("value"));
                    break;
                case "executorType":
                    this.executorType = ExecutorType.valueOf(e.attributeValue("value"));
                    break;
                case "timeout":
                    this.timeout = Integer.parseInt(e.attributeValue("value"));
                    break;
                case "logType":
                    this.logType = LogType.valueOf(e.attributeValue("value"));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 解析配置文件中的environment节点
     * @param environments environment节点元素
     */
    public void parseEnvironments(Element environments) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Element environment = environments.element(environments.attributeValue("default"));
        TransactionFactory transactionFactory = parseTransaction(environment);
        DataSource dataSource = parseDataSource(environment);
        this.environment = Environment.builder()
                .id(environment.attributeValue("id"))
                .dataSource(dataSource)
                .transactionFactory(transactionFactory)
                .build();
    }

    /**
     * 解析事务管理配置
     * @param environment 传入environment节点
     * @return 返回一个事务管理工厂
     * @throws InstantiationException 异常
     * @throws IllegalAccessException 异常
     * @throws ClassNotFoundException 异常
     */
    public TransactionFactory parseTransaction(Element environment) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String transactionManager = environment.element("transactionManager").attributeValue("type");
        TransactionFactory transactionFactory;
        switch (transactionManager){
            case "JDBC":
                transactionFactory = new JDBCTransactionFactory();
                break;
            case "MANAGED":
                transactionFactory = new ManagedTransactionFactory();
                break;
            default:
                transactionFactory  =  (TransactionFactory)Class.forName(transactionManager).newInstance();
                break;
        }
        return transactionFactory;
    }

    /**
     * 解析配置文件中的dataSource节点
     * @param environment environment节点
     * @return
     */
    public DataSource parseDataSource(Element environment) {
        DataSource dataSource;
        Element dateSourceElement = environment.element("dataSource");
        Map<String,String> propertiesMap = new HashMap<>();
        List<Element> properties = dateSourceElement.elements();
        for(Element property : properties){
            propertiesMap.put(property.attributeValue("name"),property.attributeValue("value"));
        }
        String dataSourceType = dateSourceElement.attributeValue("type");
        switch (dataSourceType){
            case "POOLED":
                dataSource = new PooledDataSource(
                        propertiesMap.get("driver"),
                        propertiesMap.get("url"),
                        propertiesMap.get("username"),
                        propertiesMap.get("password"));
                break;
            case "UNPOOLED":
                dataSource = new UnpooledDataSource(
                        propertiesMap.get("driver"),
                        propertiesMap.get("url"),
                        propertiesMap.get("username"),
                        propertiesMap.get("password"));
                break;
            default:
                dataSource = null;
        }
        return dataSource;
    }


    /**
     * 解析mapper节点
     * 将所有的接口解析存入MapperRegistry中，形成一个接口类与代理类的映射。
     * 代理类MapperProxyFactory中存在一个Method到MapperMethod的映射。
     * 在getMapper时找不到Method到MapperMethod的映射时，就新加入一对映射。
     * 将所有sql语句解析存入MapperStatement中。由于每个mapper.xml中有方法名的id，将接口.方法名作为键，MapperStatement作为值储存在configuration中。
     */

}

