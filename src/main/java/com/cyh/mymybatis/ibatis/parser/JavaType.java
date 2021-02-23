package com.cyh.mymybatis.ibatis.parser;

import com.cyh.mymybatis.ibatis.exception.TypeException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.*;

/**
 * @Author cyh
 * @Time 2021/2/23
 */
public class JavaType {
    private final Map<String, Class<?>> TYPE_ALIASES = new HashMap();

    public JavaType() {
        try {
            this.registerAlias("string", String.class);
            this.registerAlias("byte", Byte.class);
            this.registerAlias("long", Long.class);
            this.registerAlias("short", Short.class);
            this.registerAlias("int", Integer.class);
            this.registerAlias("integer", Integer.class);
            this.registerAlias("double", Double.class);
            this.registerAlias("float", Float.class);
            this.registerAlias("boolean", Boolean.class);
            this.registerAlias("byte[]", Byte[].class);
            this.registerAlias("long[]", Long[].class);
            this.registerAlias("short[]", Short[].class);
            this.registerAlias("int[]", Integer[].class);
            this.registerAlias("integer[]", Integer[].class);
            this.registerAlias("double[]", Double[].class);
            this.registerAlias("float[]", Float[].class);
            this.registerAlias("boolean[]", Boolean[].class);
            this.registerAlias("_byte", Byte.TYPE);
            this.registerAlias("_long", Long.TYPE);
            this.registerAlias("_short", Short.TYPE);
            this.registerAlias("_int", Integer.TYPE);
            this.registerAlias("_integer", Integer.TYPE);
            this.registerAlias("_double", Double.TYPE);
            this.registerAlias("_float", Float.TYPE);
            this.registerAlias("_boolean", Boolean.TYPE);
            this.registerAlias("_byte[]", byte[].class);
            this.registerAlias("_long[]", long[].class);
            this.registerAlias("_short[]", short[].class);
            this.registerAlias("_int[]", int[].class);
            this.registerAlias("_integer[]", int[].class);
            this.registerAlias("_double[]", double[].class);
            this.registerAlias("_float[]", float[].class);
            this.registerAlias("_boolean[]", boolean[].class);
            this.registerAlias("date", Date.class);
            this.registerAlias("decimal", BigDecimal.class);
            this.registerAlias("bigdecimal", BigDecimal.class);
            this.registerAlias("biginteger", BigInteger.class);
            this.registerAlias("object", Object.class);
            this.registerAlias("date[]", Date[].class);
            this.registerAlias("decimal[]", BigDecimal[].class);
            this.registerAlias("bigdecimal[]", BigDecimal[].class);
            this.registerAlias("biginteger[]", BigInteger[].class);
            this.registerAlias("object[]", Object[].class);
            this.registerAlias("map", Map.class);
            this.registerAlias("hashmap", HashMap.class);
            this.registerAlias("list", List.class);
            this.registerAlias("arraylist", ArrayList.class);
            this.registerAlias("collection", Collection.class);
            this.registerAlias("iterator", Iterator.class);
            this.registerAlias("ResultSet", ResultSet.class);
        }catch (TypeException e){
            e.printStackTrace();
        }
    }

    public void registerAlias(String alias, Class<?> value) throws TypeException {
        if (alias == null) {
            throw new TypeException("The parameter alias cannot be null");
        } else {
            String key = alias.toLowerCase(Locale.ENGLISH);
            if (this.TYPE_ALIASES.containsKey(key) && this.TYPE_ALIASES.get(key) != null && !((Class)this.TYPE_ALIASES.get(key)).equals(value)) {
                throw new TypeException("The alias '" + alias + "' is already mapped to the value '" + ((Class)this.TYPE_ALIASES.get(key)).getName() + "'.");
            } else {
                this.TYPE_ALIASES.put(key, value);
            }
        }
    }
}
