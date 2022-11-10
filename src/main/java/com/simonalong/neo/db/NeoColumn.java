package com.simonalong.neo.db;

import static com.simonalong.neo.NeoConstant.LOG_PRE_NEO;

import java.math.BigDecimal;
import java.sql.*;

import com.simonalong.neo.Neo;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午8:30
 */
@Slf4j
@Data
@Accessors(chain = true)
public final class NeoColumn {

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
    /**
     * 列的元信息
     */
    private NeoInnerColumn innerColumn = new NeoInnerColumn();

    private NeoColumn(){}

    static NeoColumn from(NeoInnerColumn innerColumn) {
        NeoColumn neoColumn = new NeoColumn();
        neoColumn.setColumnName(innerColumn.getColumnName());
        neoColumn.setColumnLabel(innerColumn.columnName);
        neoColumn.innerColumn = innerColumn;
        neoColumn.size = innerColumn.getColumnSize();
        neoColumn.columnJDBCType = innerColumn.getColumnJDBCType();
        neoColumn.columnTypeName = innerColumn.getColumnName();
        neoColumn.isAutoIncrement = "YES".equals(innerColumn.getIsAutoIncrement());
        neoColumn.javaClass = classFromJavaType(innerColumn.getDataType());
        return neoColumn;
    }

    Boolean isPrimaryAndAutoInc() {
        return isAutoIncrement && isPrimaryKey;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public int hashCode(){
        return columnName.hashCode();
    }

    @Data
    @SuppressWarnings("all")
    @Accessors(chain = true)
    public static class NeoInnerColumn {

        private static final String TABLE_CAT = "TABLE_CAT";
        private static final String TABLE_SCHEM = "TABLE_SCHEM";
        private static final String TABLE_NAME = "TABLE_NAME";
        private static final String COLUMN_NAME = "COLUMN_NAME";
        private static final String DATA_TYPE = "DATA_TYPE";
        private static final String TYPE_NAME = "TYPE_NAME";
        private static final String COLUMN_SIZE = "COLUMN_SIZE";
        /**
         * 未使用
         */
        private static final String BUFFER_LENGTH = "BUFFER_LENGTH";
        private static final String DECIMAL_DIGITS = "DECIMAL_DIGITS";
        private static final String NUM_PREC_RADIX = "NUM_PREC_RADIX";
        private static final String NULLABLE = "NULLABLE";
        private static final String REMARKS = "REMARKS";
        private static final String COLUMN_DEF = "COLUMN_DEF";
        /**
         * 未使用
         */
        private static final String SQL_DATA_TYPE = "SQL_DATA_TYPE";
        /**
         * 未使用
         */
        private static final String SQL_DATETIME_SUB = "SQL_DATETIME_SUB";
        private static final String CHAR_OCTET_LENGTH = "CHAR_OCTET_LENGTH";
        private static final String ORDINAL_POSITION = "ORDINAL_POSITION";
        private static final String IS_NULLABLE = "IS_NULLABLE";
        /**
         * 该属性sqlite不支持
         */
        private static final String SCOPE_CATALOG = "SCOPE_CATALOG";
        private static final String SCOPE_SCHEMA = "SCOPE_SCHEMA";
        private static final String SCOPE_TABLE = "SCOPE_TABLE";
        private static final String SOURCE_DATA_TYPE = "SOURCE_DATA_TYPE";
        private static final String IS_AUTOINCREMENT = "IS_AUTOINCREMENT";
        private static final String IS_GENERATEDCOLUMN = "IS_GENERATEDCOLUMN";

        /**
         * 表目录（可以为null）
         */
        private String catalog;
        /**
         * 表模式（可以为null）
         */
        private String schema;
        /**
         * 表名COLUMN_NAME
         */
        private String tableName;
        /**
         * 列名
         */
        private String columnName;
        /**
         * int => 来自java.sql.Types的SQL类型
         */
        private Integer dataType;
        private JDBCType columnJDBCType;
        /**
         * String =>数据源依赖类型名称，对于UDT，类型名称是完全限定的
         */
        private String typeName;
        /**
         * int =>列大小。
         */
        private Integer columnSize;
        /**
         * int =>小数位数。对于DECIMAL_DIGITS不适用的数据类型，将返回Null。
         */
        private Integer decimalDigits;
        /**
         * int => Radix（通常为10或2）
         */
        private Integer numPrecRadix;
        /**
         * int =>允许NULL。
         *      columnNoNulls  - 可能不允许NULL值
         *      columnNullable  - 肯定允许NULL值
         *      columnNullableUnknown  -  nullability unknown
         */
        private Integer nullable;
        /**
         * String =>注释描述列（可以为null）
         */
        private String remarks;
        /**
         * String =>列的默认值，当值为时，应将其解释为字符串用单引号括起来（可以为null）
         */
        private String columnDef;
        /**
         * int => for char types列中的最大字节数
         */
        private Integer charOctetLength;
        /**
         * int =>表中列的索引（从1开始）
         */
        private Integer ordinalPosition;
        /**
         * String=> ISO规则用于确定列的可为空性。
         *      YES ---如果列可以包含NULL
         *      NO ---如果列不能包含NULL
         *      空字符串---如果列的可空性未知
         */
        private String isNullable;
        /**
         * String =>作为引用属性范围的表的目录（null如果DATA_TYPE不是REF），该属性sqlite不支持
         */
        private String scopeCatalog;
        /**
         * String =>表的架构，它是引用属性的作用域（如果DATA_TYPE不是REF，则为null）
         */
        private String scopeSchema;
        /**
         * String =>表名称，这是引用属性的作用域（如果是DATA_TYPE不是REF）
         */
        private String scopeTable;
        /**
         * short =>不同类型的源类型或用户生成的Ref类型，来自java.sql.Types的SQL类型（如果DATA_TYPE不是DISTINCT或用户生成的REF，则为null）
         */
        private Short sourceDataType;
        /**
         * String =>表示此列是否自动递增
         *      YES ---如果列是自动递增
         *      NO ---如果列未自动递增
         *      空字符串---如果无法确定列是否自动递增
         */
        private String isAutoIncrement;
        /**
         * String =>指示是否是一个生成的列
         *      YES---如果这是一个基因额定列
         *      NO ---如果这不是生成的列
         *      空字符串---如果无法确定这是否是生成的列
         */
        private String isGeneratedColumn;

        private NeoInnerColumn(){}

        static NeoInnerColumn parse(Neo neo, ResultSet rs){
            NeoInnerColumn innerColumn = null;
            try {
                innerColumn =  new NeoInnerColumn();
                innerColumn.setCatalog(rs.getString(TABLE_CAT));
                innerColumn.setSchema(rs.getString(TABLE_SCHEM));
                innerColumn.setColumnName(rs.getString(COLUMN_NAME));
                innerColumn.setDataType(rs.getInt(DATA_TYPE));
                innerColumn.setColumnJDBCType(JDBCType.valueOf(rs.getInt(DATA_TYPE)));
                innerColumn.setTypeName(rs.getString(TYPE_NAME));
                innerColumn.setColumnSize(rs.getInt(COLUMN_SIZE));
                innerColumn.setDecimalDigits(rs.getInt(DECIMAL_DIGITS));
                innerColumn.setNumPrecRadix(rs.getInt(NUM_PREC_RADIX));
                innerColumn.setNullable(rs.getInt(NULLABLE));
                innerColumn.setRemarks(rs.getString(REMARKS));
                innerColumn.setColumnDef(rs.getString(COLUMN_DEF));
                innerColumn.setCharOctetLength(rs.getInt(CHAR_OCTET_LENGTH));
                innerColumn.setOrdinalPosition(rs.getInt(ORDINAL_POSITION));
                innerColumn.setIsNullable(rs.getString(IS_NULLABLE));
                innerColumn.setScopeSchema(rs.getString(SCOPE_SCHEMA));
                innerColumn.setScopeTable(rs.getString(SCOPE_TABLE));
                innerColumn.setSourceDataType(rs.getShort(SOURCE_DATA_TYPE));
                innerColumn.setIsAutoIncrement(rs.getString(IS_AUTOINCREMENT));

                // pg 不支持属性
                if (!neo.getDbType().equals(DbType.PGSQL)) {
                    innerColumn.setIsGeneratedColumn(rs.getString(IS_GENERATEDCOLUMN));

                    // sqlite 不支持属性
                    if (!neo.getDbType().equals(DbType.SQLITE)) {
                        innerColumn.setScopeCatalog(rs.getString(SCOPE_CATALOG));
                    }
                }

            } catch (SQLException e) {
                log.warn(LOG_PRE_NEO, e);
            }
            return innerColumn;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NeoColumn){
            return columnName.equals(NeoColumn.class.cast(obj).getColumnName());
        }
        return false;
    }

    public static Class<?> classFromJavaType(int type) {
        switch (type) {
            case Types.BOOLEAN:
            case Types.BIT:
                return Boolean.class;

            case Types.TINYINT:
                return Byte.class;

            case Types.SMALLINT:
                return Short.class;

            case Types.INTEGER:
                return Integer.class;

            case Types.BIGINT:
                return Long.class;

            case Types.DOUBLE:
            case Types.FLOAT:
                return Double.class;

            case Types.REAL:
                return Float.class;

            case Types.TIMESTAMP:
                return Timestamp.class;

            case Types.DATE:
                return Date.class;

            case Types.VARCHAR:
            case Types.NVARCHAR:
            case Types.CHAR:
            case Types.NCHAR:
            case Types.LONGVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.CLOB:
            case Types.NCLOB:
                return String.class;

            case Types.DECIMAL:
            case Types.NUMERIC:
                return BigDecimal.class;

            case Types.VARBINARY:
            case Types.BINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB:
            case Types.JAVA_OBJECT:
                return byte[].class;

            case Types.NULL:
                return null;

            case Types.TIME:
                return Time.class;

            default:
                // DISTINCT
                // STRUCT
                // ARRAY
                // REF
                // DATALINK
                // ROWID
                // SQLXML
                // REF_CURSOR
                // TIME_WITH_TIMEZONE
                // TIMESTAMP_WITH_TIMEZONE
                break;
        }
        return null;
    }
}
