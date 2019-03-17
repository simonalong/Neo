package com.simon.neo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:46
 */
public class NeoTable {

    /**
     * 表名
     */
    @Getter
    private String tableName;
    /**
     * 表详解
     */
    private String tableDesc;
    /**
     * 主键
     */
    private TablePrimaryKey primary;
    /**
     * 外键
     */
    private List<TableForeignKey> foreignColumnList;
    /**
     * 索引
     */
    private List<TableIndex> tableIndexList;
    /**
     * 列信息
     */
    private Set<NeoColumn> columnList = new HashSet<>();

    public NeoTable(String tableName, Set<NeoColumn> columnList){
        this.tableName = tableName;
        this.columnList = columnList;
    }
    /**
     * 获取表中的自增的主键名字
     */
    String getPrimaryKeyAutoIncName() {
        return columnList.stream().filter(NeoColumn::isPrimaryAndAutoInc).map(NeoColumn::getColumnName).findFirst()
            .orElse(null);
    }

    public void setPrimary(String columnName) {
        for (NeoColumn column : columnList) {
            if(column.getColumnName().equals(columnName)){
                column.setIsPrimaryKey(true);
            }
        }
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
}
