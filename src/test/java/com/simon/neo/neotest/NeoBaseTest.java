package com.simon.neo.neotest;

import com.simon.neo.Neo;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author zhouzhenyong
 * @since 2019/3/15 下午6:34
 */
public class NeoBaseTest {

    public  static final String URL = "jdbc:mysql://118.31.38.50:3306/tina?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    public static final String USER = "like";
    public static final String PASSWORD = "Like@123";

    public static final String TABLE_NAME = "tina_test";

    public Neo neo = Neo.connect(URL, USER, PASSWORD);

    public NeoBaseTest() throws SQLException {
    }

    public void show(Object object) {
        if(null == object){
            System.out.println("obj is null ");
            return;
        }
        Optional.ofNullable(object).ifPresent(objects1 -> System.out.println(objects1.toString()));
    }
}
