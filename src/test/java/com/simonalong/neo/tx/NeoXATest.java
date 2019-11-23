package com.simonalong.neo.tx;

import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.MysqlXAConnection;
import com.mysql.cj.jdbc.MysqlXid;
import com.simonalong.neo.BaseTest;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.xa.NeoXa;

import lombok.SneakyThrows;
import org.junit.Test;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

/**
 * @author zhouzhenyong
 * @since 2019/11/20 下午11:17
 */
public class NeoXATest extends BaseTest {

    public NeoXATest() {
    }

    @Test
    public void testXa() {
        Neo db1 = Neo.connect("jdbc:mysql://127.0.0.1:3306/neo", "root", "Root@123");
        Neo db2 = Neo.connect("jdbc:mysql://127.0.0.1:3306/neo2", "root", "Root@123");

        NeoXa xa = NeoXa.of("d1", db1, "d2", db2).setTmDb(db1);

        xa.run(() -> {
            Neo d1 = xa.get("d1");
            Neo d2 = xa.get("d2");
            d1.insert("neo_ax_t1", NeoMap.of("code", 6));
            d2.insert("neo_ax_t1", NeoMap.of("code", 5));
        });
    }

    @Test
    @SneakyThrows
    public void testXa0() {

        Neo db1 = Neo.connect("jdbc:mysql://127.0.0.1:3306/neo", "root", "Root@123");
        Neo db2 = Neo.connect("jdbc:mysql://127.0.0.1:3306/neo2", "root", "Root@123");

        XAConnection xaConn1 = new MysqlXAConnection(db1.getConnection().unwrap(JdbcConnection.class), true);
        XAResource rm1 = xaConn1.getXAResource();

        XAConnection xaConn2 = new MysqlXAConnection(db2.getConnection().unwrap(JdbcConnection.class), true);
        XAResource rm2 = xaConn2.getXAResource();

        Xid xid1 = new MysqlXid("g12345".getBytes(), "b1".getBytes(), 1);
        Xid xid2 = new MysqlXid("g12345".getBytes(), "b2".getBytes(), 1);

        rm1.start(xid1, XAResource.TMNOFLAGS);
        db1.insert("neo_ax_t1", NeoMap.of("code", 14));
        rm1.end(xid1, XAResource.TMSUCCESS);


        rm2.start(xid2, XAResource.TMNOFLAGS);
        db2.insert("neo_ax_t1", NeoMap.of("code", 11));
        rm2.end(xid2, XAResource.XA_RDONLY);

        // ===================两阶段提交================================
        // phase1：询问所有的RM 准备提交事务分支
        int rm1_prepare = rm1.prepare(xid1);
        int rm2_prepare = rm2.prepare(xid2);
        // phase2：提交所有事务分支
        boolean onePhase = false; //TM判断有2个事务分支，所以不能优化为一阶段提交
        if (rm1_prepare == XAResource.XA_OK && rm2_prepare == XAResource.XA_OK) {//所有事务分支都prepare成功，提交所有事务分支
            rm1.commit(xid1, onePhase);
            rm2.commit(xid2, onePhase);
        } else {//如果有事务分支没有成功，则回滚
            rm1.rollback(xid1);
            rm1.rollback(xid2);
        }

    }
}
