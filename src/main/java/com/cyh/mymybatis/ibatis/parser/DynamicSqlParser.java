package com.cyh.mymybatis.ibatis.parser;

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

    public String parse(String rawSql){
        StringBuilder builder = new StringBuilder();
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
        return builder.toString();
    }
}

