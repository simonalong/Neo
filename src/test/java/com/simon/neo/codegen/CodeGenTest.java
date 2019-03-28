package com.simon.neo.codegen;

import com.simon.neo.NeoMap.NamingChg;
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

        // 设置属性中数据库列名字向属性名字的转换，这里设置下划线，比如：data_user_base -> dataUserBase
        codeGen.setFieldNamingChg(NamingChg.UNDERLINE);

        // 代码生成
        codeGen.generate();

        System.out.println("finish");
    }
}
