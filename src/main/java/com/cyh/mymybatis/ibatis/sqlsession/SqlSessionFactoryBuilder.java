package com.cyh.mymybatis.ibatis.sqlsession;

import com.cyh.mymybatis.ibatis.parser.XmlParser;

import java.io.InputStream;
import java.util.Properties;

public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(InputStream inputStream) {
        XmlParser xmlParser = new XmlParser();
        Configuration configuration = xmlParser.getConfiguration(inputStream);
        return new DefaultSqlSessionFactory(configuration);
    }
}
