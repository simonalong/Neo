package com.simonalong.neo.codegen.generator;

import com.mysql.cj.MysqlType;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.StringConverter;
import com.simonalong.neo.codegen.FieldInfo;
import com.simonalong.neo.codegen.config.SqlGeneratorConfig;
import com.simonalong.neo.exception.NeoException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/5/15 2:16 PM
 */
public class CreateSqlEntityCodeGenerator extends AbstractEntityCodeGenerator {

    @Override
    public void generate() {
        NeoMap configMap = loadConfig();
        writeFile(configMap, getEntityPath(configMap.getString("TableName")), "entity.ftl");
    }

    private NeoMap loadConfig() {
        NeoMap dataMap = NeoMap.of("entityPath", configContext.getEntityPath());
        dataMap.put("tableNamePost", configContext.getEntityPostFix());
        try {
            CreateTable createTable = (CreateTable)CCJSqlParserUtil.parse(((SqlGeneratorConfig)configContext).getCreateSql());
            generateImport(createTable, dataMap);

            String tableNameOriginal = getNameFilterDb(createTable.getTable().getName());
            dataMap.put("tableName", tableNameOriginal);

            String tableName = StringConverter.underLineToBigCamel(getNameFilterDb(tableNameOriginal.substring(configContext.getPreFix().length())));
            dataMap.put("TableName", tableName);
            dataMap.put("tableRemark", getTableNameCn(createTable));
            dataMap.put("fieldList", getFieldInfoList(createTable));
        } catch (JSQLParserException e) {
            throw new NeoException("parse create table sql fail: ", e);
        }
        return dataMap;
    }

    private String getTableNameCn(CreateTable createTable) {
        List<?> optionList = createTable.getTableOptionsStrings();
        if (null == optionList || optionList.isEmpty()) {
            return "";
        }
        List<String> options = optionList.stream().map(String::valueOf).collect(Collectors.toList());
        if (options.isEmpty() || !options.contains("COMMENT")) {
            return "";
        }

        return options.get(options.indexOf("COMMENT") + 2).replaceAll("'", "");
    }

    private List<NeoMap> getFieldInfoList(CreateTable createTable) {
        return createTable.getColumnDefinitions().stream().map(c -> {
            try {
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldType(Class.forName(MysqlType.getByName(c.getColDataType().getDataType()).getClassName()).getSimpleName());
                fieldInfo.setFieldRemark(getNameFilterDb(getColumnNameCn(c)));
                fieldInfo.setFieldName(getNameFilterDb(configContext.getFieldNamingChg().otherToSmallCamel(c.getColumnName())));
                fieldInfo.setColumnName(getNameFilterDb(c.getColumnName()));
                return NeoMap.from(fieldInfo, NeoMap.NamingChg.DEFAULT);
            } catch (ClassNotFoundException e) {
                throw new NeoException(e);
            }
        }).collect(Collectors.toList());
    }

    private String getColumnNameCn(ColumnDefinition columnDefinition) {
        List<String> columnSpecStrings = columnDefinition.getColumnSpecStrings();
        if (null == columnSpecStrings || columnSpecStrings.isEmpty()) {
            return null;
        }
        int index = columnSpecStrings.indexOf("COMMENT");
        if (-1 != index) {
            return columnSpecStrings.get(index + 1);
        }
        return null;
    }
}
