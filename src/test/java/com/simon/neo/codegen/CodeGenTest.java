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
        EntityCodeGen codeGen = new EntityCodeGen()
            // 设置DB信息
            .setDb("neo_test", "neo@Test123", "jdbc:mysql://127.0.0.1:3306/tina?useUnicode=true&characterEncoding=UTF-8&useSSL=false")
            // 设置项目路径
            .setProjectPath("/Users/zhouzhenyong/project/private/Neo")
            // 设置实体生成的包路径
            .setEntityPath("com.simon.neo.entity")
            // 设置表前缀过滤
            .setPreFix("t_")
            // 设置要排除的表
            //.setExcludes("xx_test")
            // 设置只要的表
            .setIncludes("t_config_item")
            // 设置属性中数据库列名字向属性名字的转换，这里设置下划线，比如：data_user_base -> dataUserBase
            .setFieldNamingChg(NamingChg.UNDERLINE);

        // 代码生成
        codeGen.generate();
    }
}
