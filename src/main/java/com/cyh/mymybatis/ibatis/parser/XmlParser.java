package com.cyh.mymybatis.ibatis.parser;

import com.cyh.mymybatis.ibatis.Log.LogType;
import com.cyh.mymybatis.ibatis.datasource.PooledDataSource;
import com.cyh.mymybatis.ibatis.datasource.UnpooledDataSource;
import com.cyh.mymybatis.ibatis.executor.ExecutorType;
import com.cyh.mymybatis.ibatis.mapper.MappedStatement;
import com.cyh.mymybatis.ibatis.sqlsession.Configuration;
import com.cyh.mymybatis.ibatis.sqlsession.Environment;
import com.cyh.mymybatis.ibatis.transaction.JDBCTransactionFactory;
import com.cyh.mymybatis.ibatis.transaction.ManagedTransactionFactory;
import com.cyh.mymybatis.ibatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sun.security.util.Resources;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.*;

public class XmlParser {
    private Configuration configuration;

    public Configuration getConfiguration(InputStream in){
        this.parseConfig(in);
        return this.configuration;
    }

    public void parseConfig(InputStream in){

        try{
            this.configuration = new Configuration();
            SAXReader reader = new SAXReader();
            Document doc = reader.read(in);
            Element root = doc.getRootElement();
            parseConfigSettings(root.element("settings"));
            parseEnvironments(root.element("environments"));
            parseMappers(root.element("mappers"));
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
                    this.configuration.setFirstLevelCacheEnabled(Boolean.parseBoolean(e.attributeValue("value")));
                    break;
                case "secondLevelCacheEnabled":
                    this.configuration.setSecondLevelCacheEnabled(Boolean.parseBoolean(e.attributeValue("value")));
                    break;
                case "lazyLoadingEnabled":
                    this.configuration.setLazyLoadingEnabled(Boolean.parseBoolean(e.attributeValue("value")));
                    break;
                case "multipleResultSetEnabled":
                    this.configuration.setMultipleResultSetEnabled(Boolean.parseBoolean(e.attributeValue("value")));
                    break;
                case "executorType":
                    this.configuration.setExecutorType(ExecutorType.valueOf(e.attributeValue("value")));
                    break;
                case "timeout":
                    this.configuration.setTimeout(Integer.parseInt(e.attributeValue("value")));
                    break;
                case "logType":
                    this.configuration.setLogType(LogType.valueOf(e.attributeValue("value")));
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
        this.configuration.setEnvironment(Environment.builder()
                .id(environment.attributeValue("id"))
                .dataSource(dataSource)
                .transactionFactory(transactionFactory)
                .build());
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
        TransactionFactory transactionFactory = null;
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
        DataSource dataSource = null;
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

    /**
     * 解析配置文件中mapper节点
     * @param mappers mapper节点
     */
    public void parseMappers(Element mappers){
        List<Element> mapperList= mappers.elements();
        for (Element mapper : mapperList){
            parseMapper(mapper.attributeValue("resources"));
        }
    }

    /**
     * 读取并解析配置文件mapper节点中的mapper.xml
     * @param xmlPath mapper.xml的路径
     */
    public void parseMapper(String xmlPath) {
        InputStream in = Resources.class.getResourceAsStream(xmlPath);
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(in);
            Element root = doc.getRootElement();
            List<Element> sqlLists = root.elements();
            MappedStatement ms = new MappedStatement();
            String id = new String();
            for (Element sql : sqlLists){
                id = sql.attributeValue("id");
                parseSql(sql,ms);
            }
            this.configuration.getMappedStatements().put(id,ms);
        }
        catch (DocumentException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将mapper.xml文件中的sql语句封装到MappedStatement中，并添加到Configuration的Map中去
     * @param sql 一个sql语句节点
     * @param ms MappedStatement
     * @throws ClassNotFoundException 不存在类异常
     */
    public void parseSql(Element sql, MappedStatement ms) throws ClassNotFoundException {

        Element paramTypes = sql.element("paramType");
        String paramTypeString = paramTypes.attributeValue("value");
        ms.setResultType(Class.forName(paramTypeString));


        Element resultType = sql.element("resultType");
        String resultTypeString = resultType.attributeValue("value");
        ms.setResultType(Class.forName(resultTypeString));

        Element sqlCommand = sql.element("sql");
        String sqlString = sqlCommand.attributeValue("value");
        ms.setRawSql(sqlString);
        DynamicSqlParser dynamicSqlParser= new DynamicSqlParser("#{","}");
        dynamicSqlParser.parse(ms);
    }

    /**
     * 还需要解析MapperRegistry，还没写
     */
}
