package com.simon.neo;

import java.sql.JDBCType;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午8:30
 */
@Data
@Accessors(chain = true)
public class NeoColumn {

    /**
     * 数据库中的列名
     */
    private String columnName;
    /**
     * 列的别名
     */
    private String columnLabel;
    /**
     * 尺寸大小
     */
    private Integer size;
    /**
     * 列的转换的java的类型
     */
    private Class<?> javaClass;
    /**
     * 列的JDBC类型
     * @see java.sql.JDBCType
     */
    private JDBCType columnJDBCType;
    /**
     * 列的数据库中定义的类型
     */
    private String columnTypeName;
    /**
     * 列是否是可以自增的
     */
    private Boolean isAutoIncrement = false;
    /**
     * 列是否是主键
     */
    private Boolean isPrimaryKey = false;
    /**
     * 列是否是外键
     */
    private Boolean isForeignKey = false;

    public Boolean isPrimaryAndAutoInc(){
        return isAutoIncrement && isPrimaryKey;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public int hashCode(){
        return columnName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NeoColumn){
            return columnName.equals(NeoColumn.class.cast(obj).getColumnName());
        }
        return false;
    }
}
