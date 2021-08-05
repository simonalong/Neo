package com.simonalong.neo;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 命名规则转换器
 *
 * @author zhouzhenyong
 * @since 2019/3/18 下午10:03
 */
@SuppressWarnings("unused")
public final class StringConverter {

    /**
     * 大驼峰到小驼峰：首字母变成小写
     * {@code DateBaseUser -> dataBaseUser}
     *
     * @param word 源字符
     * @return 目标字符
     */
    public static String bigCamelToSmallCamel(String word){
        return (null == word) ? null : word.substring(0, 1).toLowerCase(Locale.ENGLISH) + word.substring(1);
    }

    /**
     * 小驼峰到大驼峰：首字母变成小写
     * {@code dataBaseUser -> DateBaseUser}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String bigCamel(String word){
        return (null == word) ? null : word.substring(0, 1).toUpperCase(Locale.ENGLISH) + word.substring(1);
    }

    /**
     * 匹配的单词变为大写
     * @param regex 正则表达式，主要用于匹配某些字符变为大写
     * @param word 待匹配字段
     * @return 目标字符
     */
    private static String toUpper(String regex, String word){
        Matcher m = Pattern.compile(regex).matcher(word);
        StringBuffer buffer = new StringBuffer();
        while (m.find()){
            m.appendReplacement(buffer, m.group().toUpperCase(Locale.ENGLISH));
        }
        m.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * 下划线到小驼峰：下划线后面小写变大写，下划线去掉
     * {@code
     * data_base_user   -> dataBaseUser
     * _data_base_user  -> dataBaseUser
     * _data_base_user_ -> dataBaseUser
     * data_base_user_  -> dataBaseUser
     * }
     * <p>
     *
     * @param word 源字符
     * @return 目标字符
     */
    public static String underLineToSmallCamel(String word){
        if (null == word) {
            return null;
        }
        return bigCamelToSmallCamel(toUpper("(?<=_)[a-z]", word).replaceAll("_", ""));
    }

    /**
     * 小驼峰到下划线：非边缘单词开头大写变前下划线和后面大写：{@code dataBaseUser -> data_base_user}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String underLine(String word){
        if (null == word) {
            return null;
        }
        String regex = "\\B[A-Z]";
        Matcher m = Pattern.compile(regex).matcher(word);
        StringBuffer buffer = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(buffer, "_" + m.group().toLowerCase(Locale.ENGLISH));
        }
        m.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * 小驼峰到后下划线：{@code dataBaseUser -> data_base_user_}
     * @param word 源字符
     * @return 目标字符
     */
    public static String postUnder(String word){
        if (null == word) {
            return null;
        }
        return underLine(word) + "_";
    }

    /**
     * 小驼峰到前下划线：{@code dataBaseUser -> _data_base_user}
     * @param word 源字符
     * @return 目标字符
     */
    public static String preUnder(String word){
        if (null == word) {
            return null;
        }
        return "_" + underLine(word);
    }

    /**
     * 小驼峰到前后缀下划线：{@code dataBaseUser -> _data_base_user_}
     * @param word 源字符
     * @return 目标字符
     */
    public static String prePostUnder(String word){
        if (null == word) {
            return null;
        }
        return "_" + underLine(word) + "_";
    }

    /**
     * 下划线到大驼峰：下划线后面小写变大写，下划线去掉
     * {@code
     * data_base_user   -> DataBaseUser
     * _data_base_user  -> DataBaseUser
     * _data_base_user_ -> DataBaseUser
     * data_base_user_  -> DataBaseUser
     * }
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String underLineToBigCamel(String word){
        if (null == word) {
            return null;
        }
        return bigCamel(toUpper("(?<=_)[a-z]", word).replaceAll("_", ""));
    }


    /**
     * 大驼峰到下划线：{@code DataBaseUser -> data_base_user}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String bigCamelToUnderLine(String word){
        return underLine(bigCamelToSmallCamel(word));
    }

    /**
     * 大驼峰到前缀下划线：{@code DataBaseUser -> _data_base_user}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String bigCamelToPreUnder(String word){
        return preUnder(bigCamelToSmallCamel(word));
    }

    /**
     * 大驼峰到后缀下划线：{@code DataBaseUser -> data_base_user_}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String bigCamelToPostUnder(String word){
        return postUnder(bigCamelToSmallCamel(word));
    }

    /**
     * 大驼峰到前后缀下划线：{@code DataBaseUser -> _data_base_user_}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String bigCamelToPrePostUnder(String word){
        return prePostUnder(bigCamelToSmallCamel(word));
    }

    /**
     * 中划线到小驼峰：{@code data-db-user -> dataBaseUser}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String middleLineToSmallCamel(String word){
        if (null == word) {
            return null;
        }
        String regex = "(?<=-)[a-z]";
        return toUpper(regex, word).replaceAll("-", "");
    }

    /**
     * 小驼峰到中划线：{@code dataBaseUser -> data-db-user}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String middleLine(String word){
        if (null == word) {
            return null;
        }
        String regex = "\\B[A-Z]";
        Matcher m = Pattern.compile(regex).matcher(word);
        StringBuffer buffer = new StringBuffer();
        while (m.find()){
            m.appendReplacement(buffer, "-"+m.group().toLowerCase(Locale.ENGLISH));
        }
        m.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * 大写下划线到小驼峰：{@code DATA_BASE_USER -> dataBaseUser}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String upperUnderToSmallCamel(String word){
        if (null == word) {
            return null;
        }
        return underLineToSmallCamel(word.toLowerCase(Locale.ENGLISH));
    }

    /**
     * 小驼峰到大写下划线：{@code dataBaseUser -> DATA_BASE_USER}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String upperUnder(String word){
        if (null == word) {
            return null;
        }
        return word.replaceAll("\\B[A-Z]", "_$0").toUpperCase(Locale.ENGLISH);
    }

    /**
     * 大写中划线到小驼峰：{@code DATA-BASE-USER -> dataBaseUser}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String upperUnderMiddleToSmallCamel(String word){
        if (null == word) {
            return null;
        }
        return middleLineToSmallCamel(word.toLowerCase(Locale.ENGLISH));
    }

    /**
     * 小驼峰到大写中划线：{@code dataBaseUser -> DATA-BASE-USER}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String upperUnderMiddle(String word){
        if (null == word) {
            return null;
        }
        return word.replaceAll("\\B[A-Z]", "-$0").toUpperCase(Locale.ENGLISH);
    }

    /**
     * 中划线到大驼峰：{@code data-db-user -> DataBaseUser}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String middleLineToBigCamel(String word){
        return bigCamel(middleLineToSmallCamel(word));
    }

    /**
     * 大驼峰到中划线：{@code DataBaseUser -> data-db-user}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String bigCamelToMiddleLine(String word){
        return middleLine(bigCamelToSmallCamel(word));
    }

    /**
     * 大写下划线到大驼峰：{@code DATA_BASE_USER -> DataBaseUser}
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String upperUnderToBigCamel(String word){
        return bigCamel(upperUnderToSmallCamel(word));
    }

    /**
     * 大驼峰到大写下划线：{@code DataBaseUser -> DATA_BASE_USER }
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String bigCamelToUpperUnder(String word){
        return upperUnder(bigCamelToSmallCamel(word));
    }

    /**
     * 大写中划线到大驼峰：{@code DATA-BASE-USER -> DataBaseUser }
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String upperMiddleToBigCamel(String word){
        return bigCamel(upperUnderMiddleToSmallCamel(word));
    }

    /**
     * 大驼峰到小写中划线：{@code DataBaseUser -> DATA-BASE-USER }
     * <p>
     * @param word 源字符
     * @return 目标字符
     */
    public static String bigCamelToUpperMiddle(String word){
        return upperUnderMiddle(bigCamelToSmallCamel(word));
    }

    /**
     * 前缀字符下划线去掉到小驼峰：{@code pre_data_base_user -> dataBaseUser}
     * <p>
     * @param word 源字符
     * @param preFix 前缀
     * @return 目标字符
     */
    public static String preFixUnderToSmallCamel(String word, String preFix){
        if(word.startsWith(preFix)){
            return underLineToSmallCamel(word.substring(preFix.length()));
        }
        return underLineToSmallCamel(word);
    }

    /**
     * 小驼峰到添加前缀字符下划线：{@code dataBaseUser -> pre_data_base_user}
     * <p>
     * @param word 源字符
     * @param preFix 字符前缀
     * @return 目标字符
     */
    public static String preFixUnderLine(String word, String preFix){
        return preFix + underLine(word);
    }
}
