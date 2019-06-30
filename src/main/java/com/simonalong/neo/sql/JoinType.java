package com.simonalong.neo.sql;

import lombok.Getter;

/**
 * @author zhouzhenyong
 * @since 2019/4/21 上午10:54
 */
@Getter
public enum JoinType {

    /**
     * 左关联
     */
    LEFT_JOIN("left join"),
    /**
     * 右关联
     */
    RIGHT_JOIN("right join"),
    /**
     * 内部关联
     */
    INNER_JOIN("inner join"),
    /**
     * 外部关联
     */
    OUTER_JOIN("outer join"),
    /**
     * 左关联排除和外部相同的部分
     */
    LEFT_JOIN_EXCEPT_INNER("left join"),
    /**
     * 右关联排除和外部相同的部分
     */
    RIGHT_JOIN_EXCEPT_INNER("right join"),
    /**
     * 外关联排除和外部相同的部分
     */
    OUTER_JOIN_EXCEPT_INNER("outer join");

    private String sql;

    JoinType(String sql){
        this.sql = sql;
    }
}
