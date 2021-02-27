package com.cyh.mymybatis.ibatis.transaction;

/**
 * @Author cyh
 * @Date 2021/2/25 23:04
 */
public enum TransactionIsolationLevel {
    NONE(0),
    READ_COMMITTED(2),
    READ_UNCOMMITTED(1),
    REPEATABLE_READ(4),
    SERIALIZABLE(8);

    private final int level;

    private TransactionIsolationLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }
}
