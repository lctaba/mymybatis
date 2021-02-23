package com.cyh.mymybatis;

import com.cyh.mymybatis.ibatis.parser.DynamicSqlParser;

public class Test {
    public static void main(String[] args) {
//        StringBuilder builder = new StringBuilder();
//        String s = "aabbccdd";
//        builder.append(s,0,7);
//        System.out.println(builder.toString());
        String s = "select * from cyh where id = #{id} and name = #{name} and age = \\#{1} and level = 'level' and type = #{type}" ;
        DynamicSqlParser dynamicSqlParser = new DynamicSqlParser("#{","}");
        System.out.println(dynamicSqlParser.parse(s));
    }
}
