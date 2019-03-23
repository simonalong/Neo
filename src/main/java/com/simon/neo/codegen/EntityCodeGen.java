package com.simon.neo.codegen;

import com.simon.neo.Neo;
import com.simon.neo.NeoMap;
import com.simon.neo.NeoMap.NamingChg;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Setter;

/**
 * @author zhouzhenyong
 * @since 2019/3/23 下午10:48
 */
@Setter
public class EntityCodeGen {

    /**
     * 代码生成路径
     */
    private String codePath;
    /**
     * 应用名字
     */
    private String appName;
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
     * 应用的包路径
     */
    private String appPath;
    /**
     * 表名前缀
     */
    private String preFix;
    /**
     * 表名前缀
     */
    private NamingChg namingChg;
    /**
     * 待生成的表
     */
    private List<String> includeTables = new ArrayList<>();
    /**
     * 不需要生成的表
     */
    private List<String> excludeTables = new ArrayList<>();

    public void setIncludes(String ...tables){
        includeTables.addAll(Arrays.asList(tables));
    }

    public void setExcludes(String ...tables){
        excludeTables.addAll(Arrays.asList(tables));
    }

    public void generate(){
        Neo neo = Neo.connect(url, userName, password);
        NeoMap dataMap = NeoMap.of("appPath", appPath);

        List<String> tableNameList = getShowTableList(neo);

        if(null != tableNameList && tableNameList.size() > 0){
            tableNameList.forEach(t->{
                dataMap.put("tableName", t);

                String tableNameAfterPre = excludePreFix(t);
                dataMap.put("tablePathName", getTablePathName(tableNameAfterPre));
                dataMap.put("tableUrlName", getTableUrlName(tableNameAfterPre));
                dataMap.put("tablePathNameLower", getTablePathNameLower(tableNameAfterPre));

                List<FieldInfo> fieldInfoList =
                // controller
                writeFile(dataMap, codePath + "/controller/" + getTablePathName(tableNameAfterPre) + "Controller.java", "controller.ftl");
                // service
                writeFile(dataMap, codePath + "/service/" + getTablePathName(tableNameAfterPre) + "Service.java", "service.ftl");
                // dao
                writeFile(dataMap, codePath + "/dao/" + getTablePathName(tableNameAfterPre) + "Dao.java", "dao.ftl");
            });
        }
    }

    private List<FieldInfo> getFieldInfoList(Neo neo, String tableName) {
        return neo.getColumnList(tableName).stream().map(c -> {
            FieldInfo fieldInfo = new FieldInfo()
                .setFieldType(c.getJavaClass().getSimpleName())
                .setFieldName(c.getColumnName());
        }).collect(Collectors.toList());
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

        // 设置代码生成的路径
        codeGen.setCodePath("/Users/zhouzhenyong/work/likekj/like_server/like-admin/src/main/java/com/like/cloud/admin");

        // 设置数据库的名字，用于DB数据获取类命名
        codeGen.setUrl("jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        // 设置应用名字，用于对外的url路径
        codeGen.setUserName("neo_test");
        // 设置应用的打包路径
        codeGen.setPassword("neo@Test123");

        // 设置应用的打包路径
        codeGen.setAppPath("com.like.cloud.admin");

        // 设置表前缀过滤
        codeGen.setPreFix("lk_");
        // 设置要排除的表
        codeGen.setExcludes("lk_talk");
        // 设置命名转换
        codeGen.setNamingChg(NamingChg.UNDERLINE);
        // 代码生成
        codeGen.generate();
    }
}
