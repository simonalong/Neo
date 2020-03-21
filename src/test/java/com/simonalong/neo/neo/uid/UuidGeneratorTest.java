//package com.simonalong.neo.neo.uid;
//
//import com.simonalong.neo.Neo;
//import com.simonalong.neo.NeoMap;
//import com.simonalong.neo.NeoBaseTest;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//
//import static com.simonalong.neo.uid.UidConstant.UUID_TABLE;
//
///**
// * @author zhouzhenyong
// * @since 2019-09-26 17:56
// */
//@Slf4j
//public class UuidGeneratorTest extends NeoBaseTest {
//
//    public UuidGeneratorTest() throws SQLException {
//    }
//
//    @Test
//    public void insert() {
//        neo.insert("uuid_data", NeoMap.of("uuid", 1));
//    }
//
//    @Test
//    public void getUuidTest() {
//        neo.openUidGenerator();
//        show(neo.getUuid());
//        show(neo.getUuid());
//        show(neo.getUuid());
//        show(neo.getUuid());
//        show(neo.getUuid());
//    }
//
//    @Test
//    @SneakyThrows
//    public void testInsert() {
//        neo.openUidGenerator();
//        List<Thread> runnables = new ArrayList<>();
//        Thread.sleep(1000);
//        for (int i = 0; i < 10; i++) {
//            runnables.add(new Thread(new InnerThread(neo), "a" + i));
//        }
//
//        runnables.forEach(Thread::start);
//
//        Thread.sleep(10 * 60 * 1000);
//    }
//
//    class InnerThread implements Runnable {
//
//        Random random = new Random();
//        Neo neo;
//
//        public InnerThread(Neo neo) {
//            this.neo = neo;
//        }
//
//        @SneakyThrows
//        @Override
//        public void run() {
//            int index = 0;
//            try {
//                for (; index < 30000000; index++) {
//                    Long result = neo.getUuid();
//                    show("thread=" + Thread.currentThread().getName() + ", result=" + result + ", index = " + index);
//                    neo.insert("uuid_data", NeoMap.of("uuid", result));
//                    Thread.sleep(random.nextInt(10));
//                }
//            } catch (Exception e) {
//                log.error("InnerThread 异常, ", e);
//            }
//        }
//    }
//}
