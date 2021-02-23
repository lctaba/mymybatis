package com.cyh.mymybatis.test;

/**
 * @Author cyh
 * @Time 2021/2/22
 */
public interface TestDao {
    TestDo findById(int id, TestDo testDo);
}
