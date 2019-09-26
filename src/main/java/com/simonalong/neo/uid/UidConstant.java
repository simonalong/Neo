package com.simonalong.neo.uid;

/**
 * @author zhouzhenyong
 * @since 2019-09-26 16:10
 */
public interface UidConstant {

    /**
     * 步长
     */
    Integer DEFAULT_STEP = 5;
    /**
     * 刷新二级buf的比率
     */
    Integer REFRESH_RATIO = 2;
    /**
     * 全局id生成器的表名
     */
    String UUID_TABLE = "neo_id_generator";
    /**
     * 全局id生成器表的唯一id
     */
    Integer TABLE_ID = 1;
}
