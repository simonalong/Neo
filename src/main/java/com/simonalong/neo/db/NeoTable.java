package com.simonalong.neo.db;

import com.simonalong.neo.Neo;
import com.simonalong.neo.core.AbstractBaseTable;
import com.simonalong.neo.core.DbSync;
import com.simonalong.neo.db.TableIndex.Index;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:46
 */
@Slf4j
public class NeoTable extends AbstractBaseTable {

    /**
     * 实际获取数据的对象
     */
    @Getter
    @Setter
    Neo neo;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 索引
     */
    private TableIndex index = new TableIndex();
    /**
     * 列信息
     */
    @Getter
    @Setter
    private Set<NeoColumn> columnList = new HashSet<>();
    @Getter
    private Table tableMata;

    public NeoTable(Neo neo, Table tableMata){
        this.neo = neo;
        this.tableMata = tableMata;
        this.tableName = tableMata.getTableName();
    }

    public NeoTable(Neo neo, String tableName, Set<NeoColumn> columnList){
        this.neo = neo;
        this.tableName = tableName;
        this.columnList = columnList;
    }

    @Override
    public DbSync getDb() {
        return this.neo;
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

//    /**
//     * 默认的join采用的是innerJoin
//     *
//     * @param rightTableName 右表表名
//     * @return 做关联的关联器
//     */
//    public NeoJoiner join(String rightTableName){
//        return neo.innerJoin(tableName, rightTableName);
//    }
//
//    /**
//     * 左关联，只保留左表的信息
//     *
//     * @param rightTableName 右表表名
//     * @return 做关联的关联器
//     */
//    public NeoJoiner leftJoin(String rightTableName){
//        return neo.leftJoin(tableName, rightTableName);
//    }
//
//    /**
//     * 左关联，只保留左表的信息
//     *
//     * @param rightTableName 右表表名
//     * @return 做关联的关联器
//     */
//    public NeoJoiner rightJoin(String rightTableName){
//        return neo.rightJoin(tableName, rightTableName);
//    }
//
//    /**
//     * 左关联，只保留左表的信息
//     *
//     * @param rightTableName 右表表名
//     * @return 做关联的关联器
//     */
//    public NeoJoiner innerJoin(String rightTableName){
//        return neo.innerJoin(tableName, rightTableName);
//    }
//
//    /**
//     * 左关联，只保留左表的信息
//     *
//     * @param rightTableName 右表表名
//     * @return 做关联的关联器
//     */
//    public NeoJoiner outerJoin(String rightTableName){
//        return neo.outerJoin(tableName, rightTableName);
//    }
//
//    /**
//     * 左关联，只保留左表的信息
//     *
//     * @param rightTableName 右表表名
//     * @return 做关联的关联器
//     */
//    public NeoJoiner leftJoinExceptInner(String rightTableName){
//        return neo.leftJoinExceptInner(tableName, rightTableName);
//    }
//
//    /**
//     * 左关联，只保留左表的信息
//     *
//     * @param rightTableName 右表表名
//     * @return 做关联的关联器
//     */
//    public NeoJoiner rightJoinExceptInner(String rightTableName){
//        return neo.rightJoinExceptInner(tableName, rightTableName);
//    }
//
//    /**
//     * 左关联，只保留左表的信息
//     *
//     * @param rightTableName 右表表名
//     * @return 做关联的关联器
//     */
//    public NeoJoiner outerJoinExceptInner(String rightTableName){
//        return neo.outerJoinExceptInner(tableName, rightTableName);
//    }


    public void initIndex(ResultSet resultSet){
        index.add(resultSet);
    }

    public List<String> getIndexNameList(){
        return index.getIndexNameList();
    }

    public List<Index> getIndexList(){
        return index.getIndexList();
    }

    public List<String> getColumnNameList(){
        return getColumnList().stream().map(NeoColumn::getColumnName).collect(Collectors.toList());
    }

    /**
     * 获取创建sql的语句
     * {@code
     * create table xxx{
     *     id xxxx;
     * } comment ='xxxx';
     * }
     * <p>
     * @return 表创建的sql语句
     */
    public String getTableCreate(){
        return (String) (neo.execute("show create table `" + tableName + "`").get(0).get(0).get("Create Table"));
    }

    /**
     * 获取表中的自增的主键名字
     * <p>
     * @return 主键且自增的列的名字
     */
    String getPrimaryKeyAutoIncName() {
        return columnList.stream().filter(NeoColumn::isPrimaryAndAutoInc).map(NeoColumn::getColumnName).findFirst()
            .orElse(null);
    }

    /**
     * 获取表中的主键名字
     * <p>
     * @return 主键的列名
     */
    public String getPrimary() {
        return columnList.stream().filter(NeoColumn::getIsPrimaryKey).map(NeoColumn::getColumnName).findFirst()
            .orElse(null);
    }

    void setPrimary(String columnName) {
        for (NeoColumn column : columnList) {
            if(column.getColumnName().equals(columnName)){
                column.setIsPrimaryKey(true);
            }
        }
    }

    NeoTable setTableMeta(Table tableMeta){
        this.tableMata = tableMeta;
        this.tableName = tableMeta.getTableName();
        return this;
    }

    @Override
    public int hashCode(){
        return tableName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NeoTable){
            NeoTable objTable = NeoTable.class.cast(obj);
            return tableName.equals(objTable.getTableName());
        }
        return false;
    }

    @Data
    @Accessors(chain = true)
    public static class Table{

        private static final String TABLE_CAT = "TABLE_CAT";
        private static final String TABLE_SCHEM = "TABLE_SCHEM";
        private static final String TABLE_NAME = "TABLE_NAME";
        private static final String TABLE_TYPE = "TABLE_TYPE";
        private static final String REMARKS = "REMARKS";
        private static final String TYPE_CAT = "TYPE_CAT";
        private static final String TYPE_SCHEM = "TYPE_SCHEM";
        private static final String TYPE_NAME = "TYPE_NAME";
        private static final String SELF_REFERENCING_COL_NAME = "SELF_REFERENCING_COL_NAME";
        private static final String REF_GENERATION = "REF_GENERATION";

        /**
         * String => db catalog（可以为null）
         */
        private String catalog;
        /**
         * String => db schema（可以为null）
         */
        private String schema;
        /**
         * String => db name
         */
        private String tableName;
        /**
         * String => db type。典型的类型是“TABLE”，“VIEW”，“SYSTEM TABLE”，“GLOBAL TEMPORARY”，“LOCAL TEMPORARY”，“ALIAS”，“SYNONYM”。
         */
        private String tableType;
        /**
         * String =>对表的解释性注释
         */
        private String remarks;
        /**
         * String =>类型目录（可以为null）
         */
        private String typeCatalog;
        /**
         * String =>类型模式（可以为null）
         */
        private String typeSchema;
        /**
         * String => type name（可以为null）
         */
        private String typeName;
        /**
         * String => name of指定的表的“identifier”列（可以为null）
         */
        private String selfReferencingColName;
        /**
         * String =>指定如何创建SELF_REFERENCING_COL_NAME中的值。值为“SYSTEM”，“USER”，“DERIVED”。（可能为null）
         */
        private String refGeneration;

        private Table(){}

        public static Table parse(ResultSet rs){
            try {
                return new Table()
                    .setCatalog(rs.getString(TABLE_CAT))
                    .setSchema(rs.getString(TABLE_SCHEM))
                    .setTableName(rs.getString(TABLE_NAME))
                    .setTableType(rs.getString(TABLE_TYPE))
                    .setRemarks(rs.getString(REMARKS))
                    .setTypeCatalog(rs.getString(TYPE_CAT))
                    .setTypeSchema(rs.getString(TYPE_SCHEM))
                    .setTypeName(rs.getString(TYPE_NAME))
                    .setSelfReferencingColName(rs.getString(SELF_REFERENCING_COL_NAME))
                    .setRefGeneration(rs.getString(REF_GENERATION));
            } catch (SQLException e) {
                log.error("parse error", e);
            }
            return new Table();
        }
    }
}
