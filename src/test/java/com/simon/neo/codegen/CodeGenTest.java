package com.simon.neo.codegen;

import com.simon.neo.StringNaming;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/24 下午11:13
 */
public class CodeGenTest {

    @Test
    public void test1(){
        EntityCodeGen codeGen = new EntityCodeGen();

        // 设置数据库url
        codeGen.setUrl("jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        // 设置用户名
        codeGen.setUserName("xxx");
        // 设置密码
        codeGen.setPassword("xxx");

        // 设置项目路径
        codeGen.setProjectPath("/xxxxx/Neo");

        // 设置实体的包路径
        codeGen.setEntityPath("xxxx.entity");

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
