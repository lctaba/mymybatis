package com.cyh.mymybatis.ibatis.parser;

import com.cyh.mymybatis.ibatis.mapper.MappedStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author cyh
 * @Time 2021/2/23
 */
public class DynamicSqlParser {

    private String startToken;
    private String endToken;

    public DynamicSqlParser(String startToken, String endToken) {
        this.startToken = startToken;
        this.endToken = endToken;
    }

    /**
     * 解析原始动态sql，将其转为可执行sql
     * @param ms 传入带有原始sql的MappedStatement，并将转换后的只执行sql存入
     */
    public void parse(MappedStatement ms){
        StringBuilder builder = new StringBuilder();
        String rawSql = ms.getRawSql();
        List<String> paramName = new ArrayList<>();
        int offset = 0;
        int start = rawSql.indexOf(startToken);

        while (start != -1){
            if (rawSql.charAt(start-1) == '\\'){
                builder.append(rawSql,offset,start+startToken.length());
                offset = start+startToken.length();
            }else {
                builder.append(rawSql,offset,start);
                offset = start+startToken.length();
                builder.append("?");
                int end = rawSql.indexOf(endToken,offset);
                while (end != -1){
                    if(rawSql.charAt(end-1) == '\\'){
                        offset = end+endToken.length();
                    }else {
                        paramName.add(rawSql.substring(start+startToken.length(),end));
                        offset = end+endToken.length();
                        break;
                    }
                    end = rawSql.indexOf(endToken,offset);
                }
            }
            start = rawSql.indexOf(startToken,offset);
        }
        builder.append(rawSql,offset,rawSql.length());
        ms.setParamName(paramName);
    }
}

