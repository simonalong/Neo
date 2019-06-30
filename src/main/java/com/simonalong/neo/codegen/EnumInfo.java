package com.simonalong.neo.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/3/28 下午11:04
 */
final class EnumInfo {

    private String tableName;
    @Getter
    private Map<String, EnumInner> enumInnerMap;
    private EnumInfo(){}

    static EnumInfo build(String tableName, String tableCreateSql){
        EnumInfo info = new EnumInfo();
        info.tableName = tableName;
        info.enumInnerMap = info.buildEnumMap(tableCreateSql);
        return info;
    }

    /**
     * 通过sql的创建语句
     * @param tableCreateSql 比如：create table xxx(....) ...
     * @return key为枚举的名字，value为枚举对应的信息
     */
    private Map<String, EnumInner> buildEnumMap(String tableCreateSql){
        List<String> statusList = Arrays.asList(tableCreateSql.split(",\n"));
        if(statusList.isEmpty()){
            return new HashMap<>();
        }

        Map<String, EnumInner> result = new HashMap<>();
        statusList.stream().filter(c->c.toLowerCase().contains("enum"))
            .forEach(c-> result.putIfAbsent(getEnumName(c), getEnumInner(getComment(c), getEnums(c))));
        return result;
    }

    /**
     * 获取枚举名字
     * @param tableLine 建表的一行数据枚举数据
     */
    private String getEnumName(String tableLine){
        String regex = "(.*)(?= enum)";
        Matcher matcher = Pattern.compile(regex).matcher(tableLine);
        if (matcher.find()) {
            String enumName = matcher.group(1).trim();
            return enumName.substring(1, enumName.length() - 1);
        }
        return null;
    }

    /**
     * 将枚举的注释部分进行解析，目前只支持中文和英文的字符
     * @param str 比如：性别用户的性别:MALE=男性;FEMALE=女性;UNKNOWN=未知 或者 性别用户的性别：MALE=男性；FEMALE=女性；UNKNOWN=未知
     */
    @SuppressWarnings("all")
    private EnumInner getEnumInner(String str, Set<String> enumNameSet){
        EnumInner inner = new EnumInner();
        Set<EnumMeta> metaSet = enumNameSet.stream().map(c->new EnumMeta().setEnumData(c)).collect(Collectors.toSet());
        if(null == str){
            return inner.setMetaSet(metaSet);
        }

        String regex = "^(.*)(?::|：)(.*)";
        Matcher matcher = Pattern.compile(regex).matcher(str);
        if(matcher.find()){
            inner.setRemark(matcher.group(1));
            Set<EnumMeta> metas = getMeta(matcher.group(2));
            if(!metas.isEmpty()){
                inner.setMetaSet(metas);
            }else{
                inner.setMetaSet(metaSet);
            }
        }else{
            inner.setRemark(str);
            inner.setMetaSet(metaSet);
        }
        return inner;
    }

    /**
     * 获取创建表中的一行处理的注释部分，也就是每个表字段的注释部分
     * @param tableLine 比如：`gander` enum('Y','N') COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '性别：Y=男；N=女'
     * @return 返回：性别：Y=男；N=女
     */
    private String getComment(String tableLine){
        String regex = "^.*(?<= COMMENT ')(.*)'$";
        Matcher matcher = Pattern.compile(regex).matcher(tableLine);
        if(matcher.find()){
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 获取枚举的数据列表
     * @param tableLine 建表的一行数据枚举数据
     */
    private Set<String> getEnums(String tableLine){
        String regex = "(?<= enum)\\((.*)\\)";
        Matcher matcher = Pattern.compile(regex).matcher(tableLine);
        if (matcher.find()) {
            return Arrays.stream(matcher.group(1).split(",")).map(c -> c.substring(1, c.length() - 1))
                .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    /**
     * @param text 比如：MALE=男性;FEMALE=女性;UNKNOWN=未知
     */
    @SuppressWarnings("all")
    private Set<EnumMeta> getMeta(String text){
        List<String> resultList = new ArrayList<>();
        if(text.contains(";")){
            resultList = Arrays.asList(text.split(";"));
        }else if(text.contains("；")){
            resultList = Arrays.asList(text.split("；"));
        }

        String enumDescRegex = "(.*)=(.*)";
        Pattern p = Pattern.compile(enumDescRegex);
        return resultList.stream().map(c->{
            Matcher matcher = p.matcher(c);
            if(matcher.find()){
                return new EnumMeta(matcher.group(1), matcher.group(2));
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    public class EnumInner{

        /**
         * 枚举的注释
         */
        private String remark;
        private Set<EnumMeta> metaSet;

        public String enumMetaKey() {
            if (null != metaSet && !metaSet.isEmpty()) {
                return metaSet.stream().map(c->c.enumData+c.desc).reduce((a, b)->a+":"+b).orElse(null);
            }
            return "";
        }

        @Override
        public String toString(){
            return remark + metaSet.toString();
        }
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    public class EnumMeta{

        /**
         * 枚举数据，比如Y
         */
        private String enumData;
        /**
         * 每个枚举类型的解析，比如：正常
         */
        private String desc;

        EnumMeta(String enumData, String desc){
            this.enumData = enumData;
            this.desc = desc;
        }

        @Override
        public String toString(){
            return enumData + desc;
        }
    }
}
