package com.simonalong.neo.codegen.generator;

import com.mysql.cj.MysqlType;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.codegen.config.GeneratorConfig;
import com.simonalong.neo.db.NeoColumn;
import com.simonalong.neo.exception.NeoException;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.simonalong.neo.NeoConstant.LOG_PRE;

/**
 * @author shizi
 * @since 2020/5/15 2:05 PM
 */
@Slf4j
abstract class AbstractEntityCodeGenerator implements Generator{

    GeneratorConfig configContext;
    /**
     * template的配置
     */
    private Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);

    AbstractEntityCodeGenerator(){
        configuration.setTemplateLoader(new ClassTemplateLoader(AbstractEntityCodeGenerator.class, "/template"));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setCacheStorage(NullCacheStorage.INSTANCE);
    }

    public void setConfig(GeneratorConfig generatorConfig) {
        configContext = generatorConfig;
    }

    /**
     * 根据属性的Java类型映射，进行类型的判断和生成
     */
    @SuppressWarnings("all")
    protected void generateImport(Neo neo, String tableName, NeoMap neoMap){
        List<NeoColumn> columnList = neo.getColumnList(tableName);

        // 先将标示清理掉
        neoMap.put("importDate", 0);
        neoMap.put("importTime", 0);
        neoMap.put("importTimestamp", 0);
        neoMap.put("importBigDecimal", 0);
        neoMap.put("importBigInteger", 0);
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

                if (java.math.BigInteger.class.isAssignableFrom(fieldClass)) {
                    neoMap.put("importBigInteger", 1);
                }
            });
        }
    }

    /**
     * 根据属性的Java类型映射，进行类型的判断和生成
     */
    @SuppressWarnings("all")
    void generateImport(CreateTable createTable, NeoMap neoMap){
        List<ColumnDefinition> columnList = createTable.getColumnDefinitions();
        // 先将标示清理掉
        neoMap.put("importDate", 0);
        neoMap.put("importTime", 0);
        neoMap.put("importTimestamp", 0);
        neoMap.put("importBigDecimal", 0);
        neoMap.put("importBigInteger", 0);
        if(null != columnList && !columnList.isEmpty()){
            columnList.forEach(c->{
                Class fieldClass = null;
                try {
                    fieldClass = Class.forName(MysqlType.getByName(c.getColDataType().getDataType()).getClassName());
                } catch (ClassNotFoundException e) {
                    throw new NeoException(e);
                }
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

                if (java.math.BigInteger.class.isAssignableFrom(fieldClass)) {
                    neoMap.put("importBigInteger", 1);
                }
            });
        }
    }

    protected String getEntityPath(String tableName) {
        return configContext.getProjectPath() + "/src/main/java/" + filePath(configContext.getEntityPath()) + "/" + tableName + configContext.getEntityPostFix() + ".java";
    }

    @SuppressWarnings("all")
    void writeFile(Map<String, Object> dataMap, String filePath, String templateName){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(getFile(filePath)));
            Template template = getTemplate(templateName);
            if(null != template){
                template.process(dataMap, bufferedWriter);
            }
        } catch (TemplateException | IOException e) {
            log.error(LOG_PRE + "writeFile error", e);
        }
    }

    private Template getTemplate(String templatePath){
        try {
            return configuration.getTemplate(templatePath);
        } catch (IOException e) {
            log.error(LOG_PRE + "getTemplate error", e);

        }
        return null;
    }

    /**
     * `xxx` 到 xxx
     */
    String getNameFilterDb(String name){
        if (null == name) {
            return null;
        }
        return name.replaceAll("`", "").replaceAll("'", "");
    }

    /**
     * 包路径到文件路径：com.a.b -> com/a/b
     * @param packagePath 包路径
     */
    String filePath(String packagePath){
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
}

