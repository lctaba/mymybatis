package com.cyh.mymybatis;

import com.cyh.mymybatis.ibatis.mapper.BoundSql;
import com.cyh.mymybatis.ibatis.mapper.MappedStatement;
import com.cyh.mymybatis.ibatis.parser.DynamicSqlParser;
import com.cyh.mymybatis.test.TestDo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
//        StringBuilder builder = new StringBuilder();
//        String s = "aabbccdd";
//        builder.append(s,0,7);
//        System.out.println(builder.toString());
//        String s = "select * from cyh where id = #{id} and name = #{name} and age = \\#{1} and level = 'level' and type = #{type}" ;
//        DynamicSqlParser dynamicSqlParser = new DynamicSqlParser("#{","}");
//        //        System.out.println(dynamicSqlParser.parse(s));
//        MappedStatement ms= new MappedStatement();
//        Field[] fields = ms.getClass().getDeclaredFields();
//        for(Field f : fields){
//            System.out.println(f.getName());
//        }
//    }

/*        TestDo testDo = new TestDo(0,"cyh",21);
        MappedStatement ms = new MappedStatement();
        ms.setParamType(testDo.getClass());
        ms.setParamName(new ArrayList<>());
        ms.getParamName().add("id");
        ms.getParamName().add("name");
        ms.getParamName().add("age");
        BoundSql boundSql = ms.getBoundSql(testDo);
        System.out.println(boundSql);*/
        TestDo testDo = new TestDo(0,"cyh",21);
        List<TestDo> testDos = new ArrayList<>();
        testDos.add(testDo);
        System.out.println(testDos.getClass().getClassLoader());
    }
}
