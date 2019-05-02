package com.simon.neo.uid;

import com.simon.neo.Columns;
import com.simon.neo.Neo;
import com.simon.neo.NeoMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Setter;

/**
 * 分布式全局id生成器
 * @author zhouzhenyong
 * @since 2019/5/1 下午10:22
 */
public class UidGenerator {

    @Setter
    private Neo neo;
    /**
     * 步长
     */
    @Setter
    private Integer stepSize;
    /**
     * 全局id生成器的表名
     */
    private static final String UUID_TABLE = "neo_id_generator";
    /**
     * 全局id生成器表的唯一id
     */
    private static final Integer TABLE_ID = 1;
    /**
     * id生成表是否已经初始化
     */
    private volatile Boolean tableInitFlag = false;
    /**
     * 全局id
     */
    private volatile AtomicLong uuidIndex = new AtomicLong();
    /**
     * 缓存1的全局起点
     */
    private volatile Long rangeStartOfBuf1;
    /**
     * 缓存2的全局起点
     */
    private volatile Long rangeStartOfBuf2;
    /**
     * 范围管理器
     */
    private RangeStartManager rangeManager;

    private static volatile UidGenerator instance;

    private UidGenerator(){}

    /**
     * 全局id生成器的构造函数
     *
     * @param neo 数据库对象
     * @param stepSize 步长
     * @param refreshRatio 刷新第二缓存的比率，用于在到达一定长度时候设置新的全局id起点
     * @return 全局id生成器对象
     */
    public static UidGenerator getInstance(Neo neo, Integer stepSize, Float refreshRatio){
        if(null == instance){
            synchronized (UidGenerator.class){
                if(null == instance){
                    instance = new UidGenerator();
                    instance.setNeo(neo);
                    instance.setStepSize(stepSize);
                    instance.init(neo, stepSize, refreshRatio);
                }
            }
        }
        return instance;
    }

    public Long getUid(){
        tableInitPreHandle();
        Long uid = uuidIndex.getAndIncrement();
        // 到达刷新buf的位置则进行刷新二级缓存
        if(rangeManager.readyRefresh(uid)){
            synchronized (UidGenerator.class){
                if(rangeManager.readyRefresh(uid)){
                    rangeManager.refreshRangeStart(allocStart());
                }
            }
        }

        // 刚好到达末尾，则切换起点，对于没有来得及切换，增长超过范围的，则重新分配
        Integer reachResult = rangeManager.reachBufEnd(uid);
        if (1 == reachResult) {
            uuidIndex.set(rangeManager.chgBufStart());
            return uuidIndex.getAndIncrement();
        } else if (2 == reachResult) {
            return uuidIndex.getAndIncrement();
        }
        return uid;
    }

    private void init(Neo neo, Integer stepSize, Float refreshRatio){
        rangeManager = new RangeStartManager(neo, stepSize, getRefreshBufSize(stepSize, refreshRatio));
        this.uuidIndex.set(rangeManager.initBufStart(allocStart()));
    }

    /**
     * 设置刷新尺寸
     * @param stepSize 步长
     * @param refreshRatio float类型的刷新比率
     */
    private Integer getRefreshBufSize(Integer stepSize, Float refreshRatio){
        return (int)(stepSize * refreshRatio);
    }

    /**
     * 数据库分配新的范围起点
     *
     * @return 返回数据库最新分配的值
     */
    private Long allocStart() {
        return neo.tx(() -> {
            Long value = neo.value(Long.class, UUID_TABLE, "uuid", NeoMap.of("id", TABLE_ID));
            neo.execute("update %s set `uuid` = `uuid` + ? where `id` = ?", UUID_TABLE, stepSize, TABLE_ID);
            return value;
        });
    }

    /**
     * 用于全局表的初始化，若全局表没有创建，则创建
     */
    private void tableInitPreHandle(){
        if (!tableInitFlag){
            synchronized (UidGenerator.class){
                if (!tableInitFlag) {
                    initTable();
                    tableInitFlag = true;
                }
            }
        }
    }

    private void initTable() {
        if (!neo.tableExist(UUID_TABLE)) {
            neo.execute(uidTableCreateSql());
        }
    }

    private String uidTableCreateSql() {
        return "create table `" + UUID_TABLE + "` (\n"
            + "  `id` int(11) not null,\n"
            + "  `uuid` bigint(20) not null default 0,\n"
            + "  primary key (`id`),\n"
            + "  unique key `id` (`id`)\n"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }













//
//    /*
//     * 这里步长设置为10000
//     */
//    private static final Integer STEP_SIZE = 10;
//    private static final Integer UUID_ID = 1;
//    private static final String UUID_TABLE = "id_generate";
//    /*
//     * uuid起始位置
//     */
//    private Long rangeStart;
//    private volatile AtomicLong uuid = new AtomicLong();
//    private ExecutorService singleThread = new ThreadPoolExecutor(1, 1,
//        0L, TimeUnit.MILLISECONDS,
//        new LinkedBlockingQueue<Runnable>());
//    //用于在异步获取数据的时候设置状态
//    private volatile boolean newDataTaking = false;
//    private ArrayBlockingQueue<Long> uuidQueue = new ArrayBlockingQueue<>(1);






//    @PostConstruct
//    public void init() {
//        allocNewEnd();
//        uuidQueue.poll();
//    }
//
//    private Connection getConnection() {
//        return like.getPool().getConnection();
//    }
//
//    /**
//     * 获取全局id
//     */
//    public Long getUUID() {
//        Long id = uuid.getAndIncrement();
//        //一旦检测到id已经靠近边沿，那么启动一个线程从数据库中获取
//        if (id - rangeStart + 1 >= STEP_SIZE) {
//            synchronized (this) {
//                if (!newDataTaking) {
//                    uuidQueue.clear();
//                    newDataTaking = true;
//                    allocNewEnd();
//                } else {
//                    return getNewEnd();
//                }
//            }
//        }
//        return id;
//    }
//
//    /**
//     * 使用单线程进行提供
//     */
//    private void allocNewEnd() {
//        singleThread.submit(() -> {
//            rangeStart = getAndAlloc() - STEP_SIZE + 1;
//            try {
//                uuidQueue.put(rangeStart);
//                uuid.set(rangeStart);
//                newDataTaking = false;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    /**
//     * 服务启动时候或者当前的uuid已经超过步长，则重新从数据库中获取最新的起始点
//     */
//    private Long getNewEnd() {
//        Long newEnd = null;
//        try {
//            newEnd = uuidQueue.take();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return newEnd;
//    }
//
//    /**
//     * 加行锁并获取对应的数据
//     */
//    private long getAndAlloc() {
//        long result = 0L;
//        Connection con = null;
//        boolean oldAutoCommit = false;
//        try {
//            con = getConnection();
//            oldAutoCommit = con.getAutoCommit();
//            con.setAutoCommit(false);
//
//            if (0 == updateNewEnd(con)) {
//                initDb(con);
//            }
//
//            result = selectCurrentEnd(con);
//            con.commit();
//        } catch (Exception e) {
//            try {
//                if (con != null) {
//                    con.rollback();
//                }
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }
//            throw new RuntimeException(e);
//        } finally {
//            if (con != null) {
//                try {
//                    con.setAutoCommit(oldAutoCommit);
//                    con.close();
//                } catch (SQLException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 更新当前的uuid的最大值
//     */
//    private int updateNewEnd(Connection connection) throws SQLException {
//        PreparedStatement pre = connection
//            .prepareStatement("update " + UUID_TABLE + " set uuid = uuid + ? where id = ?");
//        pre.setInt(1, STEP_SIZE);
//        pre.setInt(2, UUID_ID);
//
//        int result = pre.executeUpdate();
//        pre.close();
//        return result;
//    }
//
//    /**
//     * 选择当前的最新的uuid范围最大值
//     */
//    private Long selectCurrentEnd(Connection connection) throws SQLException {
//        PreparedStatement pre = connection.prepareStatement("select uuid from " + UUID_TABLE + " where id = ?");
//        pre.setInt(1, UUID_ID);
//
//        ResultSet qr = pre.executeQuery();
//        long result = 0;
//        if (qr.next()) {
//            result = qr.getLong("uuid");
//        }
//        pre.close();
//        qr.close();
//        return result;
//    }
//
//    /**
//     * 初始化数据库新增唯一的一条记录
//     */
//    private void initDb(Connection connection) throws SQLException {
//        PreparedStatement pre = connection.prepareStatement("insert into " + UUID_TABLE + " (id, uuid) values (?,?)");
//        pre.setInt(1, UUID_ID);
//        pre.setLong(2, STEP_SIZE);
//        pre.executeUpdate();
//        pre.close();
//    }
}
