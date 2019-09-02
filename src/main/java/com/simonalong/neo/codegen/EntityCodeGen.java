package com.simonalong.neo.codegen;

import com.simonalong.neo.Neo;
import com.simonalong.neo.codegen.EnumInfo.EnumInner;
import com.simonalong.neo.table.NeoColumn;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.StringConverter;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 数据库的DO实体映射生成器
 * @author zhouzhenyong
 * @since 2019/3/23 下午10:48
 */
@Setter
@Accessors(chain = true)
public class EntityCodeGen {

    /**
     * url
     */
    private String url;
    /**
     * 库用户名
     */
    private String userName;
    /**
     * 库的用户密码
     */
    private String password;
    /**
     * 项目路径
     */
    private String projectPath;
    /**
     * 表名前缀
     */
    private String preFix = "";
    /**
     * 属性名字的字符转换，默认为不转换
     */
    private NamingChg fieldNamingChg = NamingChg.DEFAULT;
    /**
     * 待生成的表
     */
    private List<String> includeTables = new ArrayList<>();
    /**
     * 不需要生成的表
     */
    private List<String> excludeTables = new ArrayList<>();
    /**
     * entity实体的包路径
     */
    private String entityPath;
    /**
     * 实体名字的后缀，默认为DO
     */
    private String entityPostFix = "DO";
    /**
     * 枚举类型的map
     */
    private Map<String, EnumInner> enumMap = new HashMap<>();
    /**
     * template的配置
     */
    private Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);

    public EntityCodeGen(){
        configuration.setTemplateLoader(new ClassTemplateLoader(EntityCodeGen.class, "/template"));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setCacheStorage(NullCacheStorage.INSTANCE);
    }

    public EntityCodeGen setDb(String userName, String password, String url){
        this.userName = userName;
        this.password = password;
        this.url = url;
        return this;
    }

    /**
     * 设置那些只输出的表
     * @param tables 数据库中的表名
     * @return 实体生成器
     */
    public EntityCodeGen setIncludes(String ...tables){
        includeTables.addAll(Arrays.asList(tables));
        return this;
    }

    /**
     * 设置排除的表
     * @param tables 数据库中的表名
     * @return 实体生成器
     */
    public EntityCodeGen setExcludes(String ...tables){
        excludeTables.addAll(Arrays.asList(tables));
        return this;
    }

    public void generate(){
        Neo neo = Neo.connect(url, userName, password);
        NeoMap dataMap = NeoMap.of("entityPath", entityPath);

        List<String> tableNameList = getShowTableList(neo);

        if (null != tableNameList && tableNameList.size() > 0) {
            tableNameList.forEach(t->{
                generateImport(neo, t, dataMap);
                String tableName = StringConverter.underLineToBigCamel(t.substring(preFix.length()));
                dataMap.put("tableName", tableName);
                dataMap.put("tableRemark", neo.getTable(t).getTableMata().getRemarks());
                dataMap.put("tableNamePost", entityPostFix);
                List<NeoMap> list = getFieldInfoList(neo, t);
                dataMap.put("fieldList", list);

                // 生成对应的枚举数据
                generateEnum(neo, t, dataMap);

                writeFile(dataMap,
                    projectPath + "/src/main/java/" + filePath(entityPath) + "/" + tableName
                        + entityPostFix + ".java", "entity.ftl");
            });
        }
    }

    /**
     * 设置外部和内部的枚举类型，如果有共同的类名和枚举类型，则作为公共的外部共同枚举，如果有同名的，但是内部属性不同，则作为内部枚举
     */
    private void generateEnum(Neo neo, String tableName, NeoMap dataMap){
        EnumInfo enumInfo = EnumInfo.build(tableName, neo.getTableCreate(tableName));

        // 先清理设置
        dataMap.put("enumType", null);
        dataMap.put("enumRemark", null);
        dataMap.put("enumList", null);
        dataMap.put("importInnerEnum", 0);
        List<NeoMap> innerEnumList = new ArrayList<>();
        enumInfo.getEnumInnerMap().forEach((key, value) -> {
            String bigCamelKey = NamingChg.BIGCAMEL.smallCamelToOther(NamingChg.UNDERLINE.otherToSmallCamel(key)) + "Enum";
            NeoMap enumDataMap = NeoMap.of("enumType", bigCamelKey, "enumRemark", value.getRemark(), "enumList", value.getMetaSet());
            if(!enumMap.containsKey(key)){
                enumMap.put(key, value);
                dataMap.putAll(enumDataMap);
                writeFile(dataMap,
                        projectPath + "/src/main/java/" + filePath(entityPath) + "/" + bigCamelKey
                            + ".java", "enum.ftl");
            }else{
                // 只有跟已经有的枚举类型不同，才进行在类内部生成
                if (!enumMap.get(key).equals(value)){
                    dataMap.put("importInnerEnum", 1);
                    innerEnumList.add(enumDataMap);
                }
            }
        });

        dataMap.put("innerEnumList", innerEnumList);
    }

    /**
     * 根据属性的Java类型映射，进行类型的判断和生成
     */
    @SuppressWarnings("all")
    private void generateImport(Neo neo, String tableName, NeoMap neoMap){
        List<NeoColumn> columnList = neo.getColumnList(tableName);

        // 先将标示清理掉
        neoMap.put("importDate", 0);
        neoMap.put("importTime", 0);
        neoMap.put("importTimestamp", 0);
        neoMap.put("importBigDecimal", 0);
        if(null != columnList && !columnList.isEmpty()){
            columnList.forEach(c->{
                Class fieldClass = c.getJavaClass();
                if (java.sql.Date.class.isAssignableFrom(fieldClass)) {
                    neoMap.put("importDate", 1);
                }

                if (java.sql.Time.class.isAssignableFrom(fieldClass)) {
                   neoMap.put("importTime", 1);
                }

                if (java.sql.Timestamp.class.isAssignableFrom(fieldClass)) {
                    neoMap.put("importTimestamp", 1);
                }

                if (java.math.BigDecimal.class.isAssignableFrom(fieldClass)) {
                    neoMap.put("importBigDecimal", 1);
                }

                // 枚举类型
                if ("ENUM".equals(c.getColumnTypeName())){
                    neoMap.put("enumFlag", 1);
                }
            });
        }
    }

    @SuppressWarnings("all")
    private void writeFile(Map<String, Object> dataMap, String filePath, String templateName){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(getFile(filePath)));
            Template template = getTemplate(templateName);
            if(null != template){
                template.process(dataMap, bufferedWriter);
            }
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 包路径到文件路径：com.a.b -> com/a/b
     * @param packagePath 包路径
     */
    private String filePath(String packagePath){
        return packagePath.replace(".", "/");
    }

    @SuppressWarnings("all")
    private File getFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        file.setReadable(true);
        file.setWritable(true);
        return file;
    }

    private Template getTemplate(String templatePath){
        try {
            return configuration.getTemplate(templatePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<NeoMap> getFieldInfoList(Neo neo, String tableName) {
        return neo.getColumnList(tableName).stream()
            .map(c -> NeoMap.from(new FieldInfo()
                .setFieldType(c.getJavaClass().getSimpleName())
                .setFieldRemark(c.getInnerColumn().getRemarks())
                .setFieldName(fieldNamingChg.otherToSmallCamel(c.getColumnName()))))
            .collect(Collectors.toList());
    }

    /**
     * 获取要展示的表名，如果设置了要显示的，则显示指定显示的，否则显示所有的，如果有排除的表名，则排除一些表名
     */
    private List<String> getShowTableList(Neo neo){
        List<String> tableList = includeTables;
        if(tableList.isEmpty()) {
            tableList = neo.getAllTableNameList();
        }

        tableList.removeAll(excludeTables);
        return tableList;
    }
}
