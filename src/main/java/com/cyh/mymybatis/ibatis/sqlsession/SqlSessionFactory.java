package com.cyh.mymybatis.ibatis.sqlsession;

import java.io.Closeable;

public interface SqlSessionFactory {
    SqlSession openSession();
}
