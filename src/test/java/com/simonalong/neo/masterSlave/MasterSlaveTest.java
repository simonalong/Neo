package com.simonalong.neo.masterSlave;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.replication.MasterSlaveNeo;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * @author shizi
 * @since 2020/5/28 1:01 AM
 */
public class MasterSlaveTest extends BaseTest {

    /**
     * 主从的基本验证
     */
    @Test
    public void test1(){
        String tableName = "neo_table1";

        String url = "jdbc:mysql://127.0.0.1:3307/demo1";
        String username = "root";
        String password = "";
        Neo master = Neo.connect(url, username, password);

        String url1 = "jdbc:mysql://127.0.0.1:3308/demo1";
        String username1 = "root";
        String password1 = "";
        Neo slave1 = Neo.connect(url1, username1, password1);

        MasterSlaveNeo msNeo = new MasterSlaveNeo();
        msNeo.addMasterDb(master, "master");
        msNeo.addSlaveDb(slave1, "slave1");

        msNeo.insert(tableName, NeoMap.of("name", "name1"));
        Assert.assertEquals(msNeo.one(tableName, NeoMap.of("name", "name1")), slave1.one(tableName, NeoMap.of("name", "name1")));
    }

    /**
     * 主从中，从库不可用情况下的验证
     */
    @Test
    @SneakyThrows
    public void test2(){
        String tableName = "neo_table1";

        String url = "jdbc:mysql://127.0.0.1:3307/demo1";
        String username = "root";
        String password = "";
        Neo master = Neo.connect(url, username, password);

        String url1 = "jdbc:mysql://127.0.0.1:3308/demo1";
        String username1 = "root";
        String password1 = "";
        Neo slave1 = Neo.connect(url1, username1, password1);

        MasterSlaveNeo msNeo = new MasterSlaveNeo();
        msNeo.addMasterDb(master, "master");
        msNeo.addSlaveDb(slave1, "slave1");
        Random random = new Random();

        while(true) {
            Integer index = random.nextInt(5);
            msNeo.insert(tableName, NeoMap.of("name", "name" + index));
            show(master.one(tableName, NeoMap.of("name", "name" + index)));
            show(msNeo.one(tableName, NeoMap.of("name", "name" + index)));
            show(slave1.one(tableName, NeoMap.of("name", "name" + index)));

            Thread.sleep(10 * 1000);
        }
    }

    /**
     * 主从中，从库不可用情况下的验证
     */
    @Test
    @SneakyThrows
    public void test3(){
        String tableName = "neo_table1";

        String url = "jdbc:mysql://127.0.0.1:3307/demo1";
        String username = "root";
        String password = "";
        Neo master = Neo.connect(url, username, password);
//
        String url1 = "jdbc:mysql://127.0.0.1:3308/demo1";
        String username1 = "root";
        String password1 = "";
        Neo slave1 = Neo.connect(url1, username1, password1);
//
//        MasterSlaveNeo msNeo = new MasterSlaveNeo();
//        msNeo.addMasterDb(master, "master");
////        msNeo.addSlaveDb(slave1, "slave1");
//        Random random = new Random();
//
//        while(true) {
//            Integer index = random.nextInt(5);
//            msNeo.insert(tableName, NeoMap.of("name", "name" + index));
//            show(master.one(tableName, NeoMap.of("name", "name" + index)));
//            show(msNeo.one(tableName, NeoMap.of("name", "name" + index)));
//            show(slave1.one(tableName, NeoMap.of("name", "name" + index)));
//
//            Thread.sleep(10 * 1000);
//        }
        show(master.one(tableName, NeoMap.of("name", "name0")));
    }
}
