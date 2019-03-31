package com.simon.neo.sql;

import java.sql.Connection;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事务隔离级别枚举类型
 *
 * @author zhouzhenyong
 * @since 2019/3/31 下午12:55
 */
@Getter
@AllArgsConstructor
public enum TxIsolationEnum {

    /**
     * 一个常量，指示防止脏读;可以发生不可重复的读取和幻像读取
     */
    TX_READ_UNCOMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    /**
     * 一个常量，表示可以进行脏读，不可重复读和幻像读
     */
    TX_READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    /**
     * 一个常量，表示禁止脏读和不可重复读;可以发生幻像读取
     */
    TX_REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    /**
     * 一个常量，表示防止脏读，不可重复读和幻像读
     */
    TX_SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    private int level;
}
