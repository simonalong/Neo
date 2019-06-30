package com.github.simonalong.neo.sql;

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
    TX_R_U(Connection.TRANSACTION_READ_COMMITTED),
    /**
     * 一个常量，表示可以进行脏读，不可重复读和幻像读
     */
    TX_R_C(Connection.TRANSACTION_READ_COMMITTED),
    /**
     * 一个常量，表示禁止脏读和不可重复读;可以发生幻像读取
     */
    TX_R_R(Connection.TRANSACTION_REPEATABLE_READ),
    /**
     * 一个常量，表示防止脏读，不可重复读和幻像读
     */
    TX_SE(Connection.TRANSACTION_SERIALIZABLE);

    private int level;
}
