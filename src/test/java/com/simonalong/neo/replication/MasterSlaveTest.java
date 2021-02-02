package com.simonalong.neo.replication;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
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
     * 一主一从
     */
    @Test
    public void test1() {
        String tableName = "neo_table1";

        String url = "jdbc:mysql://127.0.0.1:3307/demo1";
        String username = "root";
        String password = "";
        Neo master = Neo.connect(url, username, password);

        String url1 = "jdbc:mysql://127.0.0.1:3308/demo1";
        String username1 = "root";
        String password1 = "";
        Neo slave1 = Neo.connect(url1, username1, password1);

        Random random = new Random();
        Integer data = random.nextInt(50) + 100;

        MasterSlaveNeo msNeo = new MasterSlaveNeo();
        msNeo.addMasterDb("master", master);
        msNeo.addSlaveDb("slave1", slave1);

        NeoMap searchMap = NeoMap.of("name", "name" + data);

        msNeo.insert(tableName, searchMap);
        Assert.assertEquals(master.one(tableName, searchMap), slave1.one(tableName, searchMap));
        Assert.assertEquals(master.one(tableName, searchMap), msNeo.one(tableName, searchMap));
    }

    /**
     * 一主一从，从库异常的验证
     */
    @Test
    @SneakyThrows
    public void test2() {
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
        msNeo.addMasterDb("master", master);
        msNeo.addSlaveDb("slave1", slave1);
        Random random = new Random();

        while (true) {
            Integer index = random.nextInt(5);
            msNeo.insert(tableName, NeoMap.of("name", "name" + index));
            show(msNeo.one(tableName, NeoMap.of("name", "name" + index)));
            Thread.sleep(3 * 1000);
        }
    }

    /**
     * 一主多从，从1挂掉，切换到从2
     */
    @Test
    @SneakyThrows
    public void test3() {
        String tableName = "neo_table1";

        String url = "jdbc:mysql://127.0.0.1:3307/demo1";
        String username = "root";
        String password = "";
        Neo master = Neo.connect(url, username, password);

        String url1 = "jdbc:mysql://127.0.0.1:3308/demo1";
        String username1 = "root";
        String password1 = "";
        Neo slave1 = Neo.connect(url1, username1, password1);

        String url2 = "jdbc:mysql://127.0.0.1:3309/demo1";
        String username2 = "root";
        String password2 = "";
        Neo slave2 = Neo.connect(url2, username2, password2);

        MasterSlaveNeo msNeo = new MasterSlaveNeo();
        msNeo.addMasterDb("master", master);
        msNeo.addSlaveDb("slave1", slave1);
        msNeo.addSlaveDb("slave2", slave2);
        Random random = new Random();

        while (true) {
            Integer index = random.nextInt(50) + 100;
            msNeo.insert(tableName, NeoMap.of("name", "name" + index));
            show(msNeo.one(tableName, NeoMap.of("name", "name" + index)));
            Thread.sleep(5 * 1000);
        }
    }

    /**
     * 双主3从，从1挂掉，切换到从2
     */
    @Test
    @SneakyThrows
    public void test4() {
        String tableName = "neo_table1";

        String url1 = "jdbc:mysql://127.0.0.1:3307/demo1";
        String username1 = "root";
        String password1 = "";
        Neo master1 = Neo.connect(url1, username1, password1);

        String url2 = "jdbc:mysql://127.0.0.1:3407/demo1";
        String username2 = "root";
        String password2 = "";
        Neo master2 = Neo.connect(url2, username2, password2);

        String urlSlave1 = "jdbc:mysql://127.0.0.1:3308/demo1";
        String usernameSlave1 = "root";
        String passwordSlave1 = "";
        Neo slave1 = Neo.connect(urlSlave1, usernameSlave1, passwordSlave1);

        String urlSlave2 = "jdbc:mysql://127.0.0.1:3309/demo1";
        String usernameSlave2 = "root";
        String passwordSlave2 = "";
        Neo slave2 = Neo.connect(urlSlave2, usernameSlave2, passwordSlave2);

        String urlSlave3 = "jdbc:mysql://127.0.0.1:3408/demo1";
        String usernameSlave3 = "root";
        String passwordSlave3 = "";
        Neo slave3 = Neo.connect(urlSlave3, usernameSlave3, passwordSlave3);

        MasterSlaveNeo msNeo = new MasterSlaveNeo();
        msNeo.addMasterDb("master1", master1, true);
        msNeo.addMasterDb("master2", master2, false);
        msNeo.addSlaveDb("slave1", slave1);
        msNeo.addSlaveDb("slave2", slave2);
        msNeo.addSlaveDb("slave3", slave3);
        Random random = new Random();

        while (true) {
            Integer index = random.nextInt(50) + 100;
            msNeo.insert(tableName, NeoMap.of("name", "name" + index));
            show(msNeo.one(tableName, NeoMap.of("name", "name" + index)));
            Thread.sleep(3 * 1000);
        }
    }
}
