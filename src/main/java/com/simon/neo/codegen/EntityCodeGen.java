package com.simon.neo.codegen;

import com.simon.neo.Neo;
import com.simon.neo.NeoMap;
import com.simon.neo.NeoTable.Table;
import com.simon.neo.StringNaming;
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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Setter;

/**
 * @author zhouzhenyong
 * @since 2019/3/23 下午10:48
 */
@Setter
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
    private Function<String, String> fieldNamingChg = t->t;
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
     * template的配置
     */
    private Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);

    public EntityCodeGen(){
        configuration.setTemplateLoader(new ClassTemplateLoader(EntityCodeGen.class, "/template"));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setCacheStorage(NullCacheStorage.INSTANCE);
    }

    /**
     * 设置那些只输出的表
     * @param tables 数据库中的表名
     */
    public void setIncludes(String ...tables){
        includeTables.addAll(Arrays.asList(tables));
    }

    /**
     * 设置排除的表
     * @param tables 数据库中的表名
     */
    public void setExcludes(String ...tables){
        excludeTables.addAll(Arrays.asList(tables));
    }

    public void generate(){
        Neo neo = Neo.connect(url, userName, password);
        NeoMap dataMap = NeoMap.of("entityPath", entityPath);

        List<String> tableNameList = getShowTableList(neo);

        if(null != tableNameList && tableNameList.size() > 0){
            tableNameList.forEach(t->{
                String tableName = StringNaming.underLineToBigCamel(t.substring(preFix.length()));
                dataMap.put("tableName", tableName);
                dataMap.put("tableRemark", neo.getTable(t).getTableMata().getRemarks());
                dataMap.put("tableNamePost", entityPostFix);
                dataMap.put("fieldList", getFieldInfoList(neo, t));

                writeFile(dataMap,
                    projectPath + "/src/main/java/" + filePath(entityPath) + "/" + tableName
                        + entityPostFix + ".java", "entity.ftl");
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

    private List<FieldInfo> getFieldInfoList(Neo neo, String tableName) {
        return neo.getColumnList(tableName).stream()
            .map(c -> new FieldInfo()
                .setFieldType(c.getJavaClass().getSimpleName())
                .setFieldRemark(c.getColumnMeta().getRemarks())
                .setFieldName(fieldNamingChg.apply(c.getColumnName())))
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

    public static void main(String... args){
        EntityCodeGen codeGen = new EntityCodeGen();

        // 设置数据库url
        codeGen.setUrl("jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        // 设置用户名
        codeGen.setUserName("neo_test");
        // 设置密码
        codeGen.setPassword("neo@Test123");

        // 设置项目路径
        codeGen.setProjectPath("/Users/zhouzhenyong/project/private/Neo");

        // 设置实体的包路径
        codeGen.setEntityPath("com.simon.neo.entity");

        // 设置表前缀过滤
        codeGen.setPreFix("xx_");
        // 设置要排除的表
        codeGen.setExcludes("xx_test");
        // 设置只要的表
//         codeGen.setIncludes("xx_test2");

        // 设置属性中数据库列名字向属性名字的转换，这个跟NamingChg中的是相反的，可以不设置，默认不转换
        codeGen.setFieldNamingChg(StringNaming::underLineToSmallCamel);

        // 代码生成
        codeGen.generate();

        System.out.println("finish");
    }
}
