package com.simonalong.neo.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/3/17 下午2:42
 */
@Data
public final class TableIndex {

    private static final String TABLE_CAT = "TABLE_CAT";
    private static final String TABLE_SCHEM = "TABLE_SCHEM";
    private static final String TABLE_NAME = "TABLE_NAME";
    private static final String NON_UNIQUE = "NON_UNIQUE";
    private static final String INDEX_QUALIFIER = "INDEX_QUALIFIER";
    private static final String INDEX_NAME = "INDEX_NAME";
    private static final String TYPE = "TYPE";
    private static final String ORDINAL_POSITION = "ORDINAL_POSITION";
    private static final String COLUMN_NAME = "COLUMN_NAME";
    private static final String ASC_OR_DESC = "ASC_OR_DESC";
    private static final String CARDINALITY = "CARDINALITY";
    private static final String PAGES = "PAGES";
    private static final String FILTER_CONDITION = "FILTER_CONDITION";

    /**
     * 索引名字和索引列表
     */
    private Map<String, List<Index>> indexMap = new ConcurrentHashMap<>();

    void add(ResultSet rs){
        try {
            String indexName = rs.getString(INDEX_NAME);
            indexMap.compute(indexName, (k,v)->{
                if (null == v) {
                    List<Index> indexList = new ArrayList<>();
                    indexList.add(Index.parse(rs));
                    return indexList;
                }else{
                    v.add(Index.parse(rs));
                    return v;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    List<String> getIndexNameList(){
        return new ArrayList<>(indexMap.keySet());
    }

    List<Index> getIndexList() {
        return new ArrayList<>(indexMap.values()).stream().flatMap(d -> Arrays.stream(d.toArray())).map(s -> (Index) s)
            .collect(Collectors.toList());
    }

    @Data
    @Accessors(chain = true)
    public static class Index{
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

        private Index(){}

        static Index parse(ResultSet rs){
            try {
                return new Index()
                    .setCatalog(rs.getString(TABLE_CAT))
                    .setSchema(rs.getString(TABLE_SCHEM))
                    .setTableName(rs.getString(TABLE_NAME))
                    .setNonUnique(rs.getBoolean(NON_UNIQUE))
                    .setIndexQualifier(rs.getString(INDEX_QUALIFIER))
                    .setIndexName(rs.getString(INDEX_NAME))
                    .setType(rs.getShort(TYPE))
                    .setOrdinalPosition(rs.getShort(ORDINAL_POSITION))
                    .setColumnName(rs.getString(COLUMN_NAME))
                    .setAscOrDesc(rs.getString(ASC_OR_DESC))
                    .setCardinality(rs.getLong(CARDINALITY))
                    .setPages(rs.getLong(PAGES))
                    .setFilterCondition(rs.getString(FILTER_CONDITION));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new Index();
        }
    }
}
