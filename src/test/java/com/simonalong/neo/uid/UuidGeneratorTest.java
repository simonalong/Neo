package com.simonalong.neo.uid;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/5/1 下午10:51
 */
public class UuidGeneratorTest extends UuidBaseTest {

    public UuidGeneratorTest() throws SQLException {}

    private ExecutorService executorService = Executors.newFixedThreadPool(1000, new ThreadFactory() {
        private AtomicInteger count = new AtomicInteger(0);

        @Override
        @SuppressWarnings("all")
        public Thread newThread(Runnable r) {
            return new Thread(r, "test_" + count.getAndIncrement());
        }
    });

    @Test
    public void generateTest1() {
        UuidGenerator generator = UuidGenerator.getInstance(neo);
        generator.addNamespaces("test1", "test2");
        show(parseUid(generator.getUUid("test1")));
        show(parseUid(generator.getUUid("test1")));
        show(parseUid(generator.getUUid("test2")));
        show(parseUid(generator.getUUid("test2")));
        show(parseUid(generator.getUUid("test2")));
        show(parseUid(generator.getUUid("test2")));
        show(parseUid(generator.getUUid("test2")));
        show(parseUid(generator.getUUid("test2")));
    }

    /**
     * 基本测试
     */
    @Test
    public void test1() {
        UuidGenerator generator = UuidGenerator.getInstance(neo);
        generator.addNamespaces("biz0");
        show(parseUid(generator.getUUid("biz0")));
        show(parseUid(generator.getUUid("biz0")));
        show(parseUid(generator.getUUid("biz0")));
    }

    /**
     * 持续的低qps的压测，则这个QPS还是比较低，完全满足业务需求
     */
    @Test
    @SneakyThrows
    public void testQps1() {
        UuidGenerator generator = UuidGenerator.getInstance(neo);
        generator.addNamespaces("biz0");
        int count = 10;
        int callNum = 10;
        int concurrentNum = 100;

        for (int i = 0; i < count; i++) {
            generateFun(generator, "biz0", callNum, concurrentNum);
            Thread.sleep(1000);
        }

        //biz=biz0, qps = 3.225806451612903单位（w/s）
        //biz=biz0, qps = 52.608695652173914单位（w/s）
        //biz=biz0, qps = 147.17948717948718单位（w/s）
        //biz=biz0, qps = 190.0单位（w/s）
        //biz=biz0, qps = 734.3859649122807单位（w/s）
        //biz=biz0, qps = 636.5942028985507单位（w/s）
        //biz=biz0, qps = 689.045643153527单位（w/s）
        //biz=biz0, qps = 70.50133852518861单位（w/s）
        //biz=biz0, qps = 57.41919864423193单位（w/s）
    }

    /**
     * 低qps一段时间后到高QPS，可以支撑更高，但是一旦持续的高并发，则后面会慢慢降下来
     */
    @Test
    @SneakyThrows
    public void testQps2() {
        UuidGenerator generator = UuidGenerator.getInstance(neo);
        generator.addNamespaces("biz0");
        int count = 10;
        int callNum = 10;
        int concurrentNum = 100;

        for (int i = 0; i < count; i++) {
            generateFun(generator, "biz0", callNum + i * i * 100, concurrentNum + i * 10 * i);
            Thread.sleep(1000);
        }

        //biz=biz0, qps = 3.225806451612903单位（w/s）
        //biz=biz0, qps = 52.608695652173914单位（w/s）
        //biz=biz0, qps = 147.17948717948718单位（w/s）
        //biz=biz0, qps = 190.0单位（w/s）
        //biz=biz0, qps = 734.3859649122807单位（w/s）
        //biz=biz0, qps = 636.5942028985507单位（w/s）
        //biz=biz0, qps = 689.045643153527单位（w/s）
        //biz=biz0, qps = 70.50133852518861单位（w/s）
        //biz=biz0, qps = 57.41919864423193单位（w/s）
    }

    /**
     * 持续的高QPS，则只能达到最高的理论值（51.2w/s）
     */
    @Test
    @SneakyThrows
    public void testQps3() {
        UuidGenerator generator = UuidGenerator.getInstance(neo);
        generator.addNamespaces("biz0");
        int count = 10;
        int callNum = 1000;
        int concurrentNum = 10000;

        for (int i = 0; i < count; i++) {
            generateFun(generator, "biz0", callNum, concurrentNum);
            Thread.sleep(1000);
        }

        //biz=biz0, qps = 51.28994204236549单位（w/s）
        //biz=biz0, qps = 53.960716598316424单位（w/s）
        //biz=biz0, qps = 53.96654074473826单位（w/s）
        //biz=biz0, qps = 53.96654074473826单位（w/s）
        //biz=biz0, qps = 53.963628514381305单位（w/s）
        //biz=biz0, qps = 53.98110661268556单位（w/s）
        //biz=biz0, qps = 53.963628514381305单位（w/s）
        //biz=biz0, qps = 53.978192810104716单位（w/s）
        //biz=biz0, qps = 53.97527932207049单位（w/s）
        //biz=biz0, qps = 53.96654074473826单位（w/s）
    }

    /**
     * 多业务的压测：低qps一段时间后到高QPS，可以支撑更高
     */
    @Test
    @SneakyThrows
    public void testQps4() {
        UuidGenerator generator = UuidGenerator.getInstance(neo);
        generator.addNamespaces("biz0");
        int count = 10;
        int callNum = 10;
        int concurrentNum = 100;

        for (int i = 0; i < count; i++) {
            generateFun(generator, "biz0", callNum + i * i * 100, concurrentNum + i * 10 * i);
            generateFun(generator, "biz1", callNum + i * i * 100, concurrentNum + i * 10 * i);
            Thread.sleep(1000);
        }

        //biz=biz0, qps = 7.6923076923076925单位（w/s）
        //biz=biz0, qps = 121.0单位（w/s）
        //biz=biz0, qps = 179.375单位（w/s）
        //biz=biz0, qps = 523.939393939394单位（w/s）
        //biz=biz0, qps = 686.2295081967213单位（w/s）
        //biz=biz0, qps = 1021.5116279069767单位（w/s）
        //biz=biz0, qps = 1297.34375单位（w/s）
        //biz=biz0, qps = 68.85904444972665单位（w/s）
        //biz=biz0, qps = 58.99017535132446单位（w/s）
        //biz=biz0, qps = 56.42278287461774单位（w/s）

        //biz=biz1, qps = 14.285714285714286单位（w/s）
        //biz=biz1, qps = 110.0单位（w/s）
        //biz=biz1, qps = 574.0单位（w/s）
        //biz=biz1, qps = 864.5单位（w/s）
        //biz=biz1, qps = 890.6382978723404单位（w/s）
        //biz=biz1, qps = 1220.138888888889单位（w/s）
        //biz=biz1, qps = 1277.3846153846155单位（w/s）
        //biz=biz1, qps = 1334.9769585253457单位（w/s）
        //biz=biz1, qps = 1437.3939393939395单位（w/s）
        //biz=biz1, qps = 1543.9539748953976单位（w/s）
    }

    /**
     * 每次调用的统计，统计其中的QPS
     *
     * @param generator     id生成器
     * @param biz           业务名
     * @param callNum       调用多少次
     * @param concurrentNum 每次的并发量
     */
    @SneakyThrows
    private void generateFun(UuidGenerator generator, String biz, Integer callNum, Integer concurrentNum) {
        long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(callNum * concurrentNum);
        for (int index = 0; index < concurrentNum; index++) {
            executorService.execute(() -> {
                for (int j = 0; j < callNum; j++) {
                    generator.getUUid(biz);
                    latch.countDown();
                }
            });
        }

        latch.await();
        long duration = System.currentTimeMillis() - start;
        show("biz=" + biz + ", qps = " + (callNum * concurrentNum) / (duration * 10.0) + "单位（w/s）");
    }
}
