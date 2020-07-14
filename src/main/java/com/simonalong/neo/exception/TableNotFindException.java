package com.simonalong.neo.exception;

/**
 * @author shizi
 * @since 2020/7/14 10:40 AM
 */
public class TableNotFindException extends NeoException {

    public TableNotFindException(String table) {
        super("表 " + table + " 没有找到");
    }
}
