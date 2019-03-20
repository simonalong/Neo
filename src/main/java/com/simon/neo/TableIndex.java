package com.simon.neo;

import java.sql.ResultSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;

/**
 * @author zhouzhenyong
 * @since 2019/3/17 下午2:42
 */
@Data
public class TableIndex {

    private Map<String, Index> indexMap = new ConcurrentHashMap<>();

//    public static TableIndex of(ResultSet rs){
//
//    }



    class Index{
        /**
         * table catalog
         */
        private String catalog;
        /**
         * table schema
         */
        private String schema;
        /**
         * table name
         */
        private String tableName;
        /**
         * 索引值可以是非唯一的。当{@code type}为{@code tableIndexStatistic}时为false
         */
        private Boolean nonUnique;
        /**
         * index catalog（可以为null）;当TYPE是tableIndexStatistic时为null，可以为空
         */
        private String indexQualifier;
        /**
         * index name 索引名字，当{@code type}为{@code tableIndexStatistic}时为null
         */
        private String indexName;
        /**
         * 索引类型：
         *  tableIndexStatistic  - 这标识与表的索引描述一起返回的表统计信息
         *  tableIndexClustered  - 这是一个聚簇索引
         *  tableIndexHashed     - 这是一个散列索引
         *  tableIndexOther      - 这是其他一些样式的index
         */
        private short type;
        /**
         * 索引中的列序列号，当{@code type}为{@code tableIndexStatistic}时为零
         */
        private short ordinalPosition;
        /**
         * 列名，当{@code type}为{@code tableIndexStatistic}时为null
         */
        private String columnName;
        /**
         * 列排序顺序，“A”=>升序，“D”=>降序，如果不支持排序顺序，则可以为null，当{@code type}为{@code tableIndexStatistic}时为null
         */
        private String ascOrDesc;
        /**
         * 当{@code type}为{@code tableIndexStatistic}时，则为表中的行数;否则，它是索引中唯一值的数量。
         */
        private long cardinality;
        /**
         * 当{@code type}为{@code tableIndexStatistic}时，这是用于表的页数，否则它是用于当前索引的页数。
         */
        private long pages;
        /**
         * 过滤条件，如果有的话。（可能为null）
         */
        private String filterCondition;


    }
}
