package com.simonalong.neo.codegen.generator;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.StringConverter;
import com.simonalong.neo.codegen.config.DbGeneratorConfig;
import com.simonalong.neo.codegen.FieldInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/5/15 2:14 PM
 */
public class DbEntityCodeGenerator extends AbstractEntityCodeGenerator {

    @Override
    public void generate() {
        Neo neo = getDb();
        NeoMap dataMap = NeoMap.of("entityPath", configContext.getEntityPath());

        List<String> tableNameList = getShowTableList(neo);

        if (tableNameList.size() > 0) {
            tableNameList.forEach(t -> {
                generateImport(neo, t, dataMap);
                dataMap.put("tableName", t);
                dataMap.put("importInnerEnum", 0);
                String tableName = StringConverter.underLineToBigCamel(t.substring(configContext.getPreFix().length()));
                dataMap.put("TableName", tableName);
                dataMap.put("tableRemark", neo.getTable(t).getTableMata().getRemarks());
                dataMap.put("tableNamePost", configContext.getEntityPostFix());
                List<NeoMap> list = getFieldInfoList(neo, t);
                dataMap.put("fieldList", list);

                writeFile(dataMap, getEntityPath(tableName), "entity.ftl");
            });
        }
    }

    private Neo getDb() {
        DbGeneratorConfig dbGeneratorConfig = (DbGeneratorConfig) configContext;
        return Neo.connect(dbGeneratorConfig.getUrl(), dbGeneratorConfig.getUserName(), dbGeneratorConfig.getPassword()).initDb();
    }

    private List<NeoMap> getFieldInfoList(Neo neo, String tableName) {
        return neo.getColumnList(tableName)
            .stream()
            .map(c -> NeoMap.from(new FieldInfo().setFieldType(c.getJavaClass().getSimpleName())
                .setFieldRemark(c.getInnerColumn().getRemarks())
                .setFieldName(configContext.getFieldNamingChg().otherToSmallCamel(c.getColumnName()))
                .setColumnName(c.getColumnName()), NeoMap.NamingChg.DEFAULT))
            .collect(Collectors.toList());
    }

    /**
     * 获取要展示的表名，如果设置了要显示的，则显示指定显示的，否则显示所有的，如果有排除的表名，则排除一些表名
     */
    private List<String> getShowTableList(Neo neo) {
        List<String> tableList = ((DbGeneratorConfig) configContext).getIncludeTables();
        if (tableList.isEmpty()) {
            tableList = neo.getAllTableNameList();
        }

        tableList.removeAll(((DbGeneratorConfig) configContext).getExcludeTables());
        return tableList;
    }
}
