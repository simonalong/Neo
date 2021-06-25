package com.simonalong.neo.neo;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.entity.DemoEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.*;

/**
 * 批次执行
 * @author zhouzhenyong
 * @since 2019/3/25 下午20:04
 */
public class NeoBatchTest extends NeoBaseTest {

    public NeoBatchTest()  {}

    @BeforeClass
    public static void beforeClass() {
        neo.truncateTable(TABLE_NAME);
    }

    @Before
    public void beforeTest() {
        neo.truncateTable(TABLE_NAME);
    }

    @AfterClass
    public static void afterClass() {
        neo.truncateTable(TABLE_NAME);
    }

    @Test
    public void batchInsertTest1(){
        List<NeoMap> maps = Arrays.asList(
          NeoMap.of("group", "group_batch", "name", "name_batch_1"),
          NeoMap.of("group", "group_batch", "name", "name_batch_2"),
          NeoMap.of("group", "group_batch", "name", "name_batch_3"),
          NeoMap.of("group", "group_batch", "name", "name_batch_4"),
          NeoMap.of("group", "group_batch", "name", "name_batch_5")
        );
        neo.batchInsert(TABLE_NAME, maps);

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_1"));
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_2"));
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_3"));
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_4"));
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_5"));

        List<NeoMap> valueList = neo.list(TABLE_NAME, NeoMap.of("group", "group_batch")).stream().map(e->e.assignExcept("id")).collect(Collectors.toList());
        Assert.assertEquals(dataList, valueList);
    }

    @Test
    public void batchInsertTest2() {
        List<DemoEntity> entities = Arrays.asList(
            new DemoEntity().setGroup("group_batch").setName("name_batch_1"),
            new DemoEntity().setGroup("group_batch").setName("name_batch_2"),
            new DemoEntity().setGroup("group_batch").setName("name_batch_3"),
            new DemoEntity().setGroup("group_batch").setName("name_batch_4"),
            new DemoEntity().setGroup("group_batch").setName("name_batch_5")
        );

        neo.batchInsertEntity(TABLE_NAME, entities);

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_1", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_2", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_3", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_4", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_5", "sl", 0));
        List<NeoMap> valueList = neo.list(TABLE_NAME, NeoMap.of("group", "group_batch")).stream().map(e->e.assignExcept("id")).collect(Collectors.toList());
        Assert.assertEquals(dataList.toString(), valueList.toString());
    }

    @Test
    public void batchUpdateTest1(){
        List<NeoMap> maps = Arrays.asList(
            NeoMap.of("group", "group_batch1", "name", "name_batch_1"),
            NeoMap.of("group", "group_batch2", "name", "name_batch_2"),
            NeoMap.of("group", "group_batch3", "name", "name_batch_3"),
            NeoMap.of("group", "group_batch4", "name", "name_batch_4"),
            NeoMap.of("group", "group_batch5", "name", "name_batch_5")
        );
        neo.batchInsert(TABLE_NAME, maps);

        maps = Arrays.asList(
            NeoMap.of("group", "group_batch1", "name", "name_batch_1_chg"),
            NeoMap.of("group", "group_batch2", "name", "name_batch_2_chg"),
            NeoMap.of("group", "group_batch3", "name", "name_batch_3_chg"),
            NeoMap.of("group", "group_batch4", "name", "name_batch_4_chg"),
            NeoMap.of("group", "group_batch5", "name", "name_batch_5_chg")
        );
        neo.batchUpdate(TABLE_NAME, maps, Columns.of("group"));

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_batch1", "name", "name_batch_1_chg"));
        dataList.add(NeoMap.of("group", "group_batch2", "name", "name_batch_2_chg"));
        dataList.add(NeoMap.of("group", "group_batch3", "name", "name_batch_3_chg"));
        dataList.add(NeoMap.of("group", "group_batch4", "name", "name_batch_4_chg"));
        dataList.add(NeoMap.of("group", "group_batch5", "name", "name_batch_5_chg"));
        List<NeoMap> valueList = neo.list(TABLE_NAME, NeoMap.of()).stream().map(e->e.assignExcept("id")).collect(Collectors.toList());
        Assert.assertEquals(dataList, valueList);
    }

    /**
     * 如果没有指定默认的条件，则会自动根据表中的主键进行过滤
     */
    @Test
    public void batchUpdateTest2() {
        List<NeoMap> maps = Arrays.asList(
            NeoMap.of("group", "group_batch1", "name", "name_batch"),
            NeoMap.of("group", "group_batch2", "name", "name_batch"),
            NeoMap.of("group", "group_batch3", "name", "name_batch"),
            NeoMap.of("group", "group_batch4", "name", "name_batch"),
            NeoMap.of("group", "group_batch5", "name", "name_batch")
        );
        neo.batchInsert(TABLE_NAME, maps);

        maps = neo.list(TABLE_NAME, NeoMap.of()).stream().peek(e->{
            e.put("name", e.getString("name") + "_" + e.getInteger("id") + "_chg");
        }).collect(Collectors.toList());
        neo.batchUpdate(TABLE_NAME, maps);

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_batch1", "name", "name_batch_1_chg"));
        dataList.add(NeoMap.of("group", "group_batch2", "name", "name_batch_2_chg"));
        dataList.add(NeoMap.of("group", "group_batch3", "name", "name_batch_3_chg"));
        dataList.add(NeoMap.of("group", "group_batch4", "name", "name_batch_4_chg"));
        dataList.add(NeoMap.of("group", "group_batch5", "name", "name_batch_5_chg"));
        List<NeoMap> valueList = neo.list(TABLE_NAME, NeoMap.of()).stream().map(e->e.assignExcept("id")).collect(Collectors.toList());
        Assert.assertEquals(dataList, valueList);
    }

    @Test
    public void batchUpdateTest3_1() {
        List<DemoEntity> entities = Arrays.asList(
            new DemoEntity().setGroup("group_batch1").setName("name_batch").setUserName("user1"),
            new DemoEntity().setGroup("group_batch2").setName("name_batch").setUserName("user2"),
            new DemoEntity().setGroup("group_batch3").setName("name_batch").setUserName("user3"),
            new DemoEntity().setGroup("group_batch4").setName("name_batch").setUserName("user4"),
            new DemoEntity().setGroup("group_batch5").setName("name_batch").setUserName("user5")
        );
        neo.batchInsertEntity(TABLE_NAME, entities);

        entities = neo.list(DemoEntity.class, TABLE_NAME, NeoMap.of()).stream().map(e-> e.setName("name_batch_" + e.getId() + "_chg")).collect(Collectors.toList());
        neo.batchUpdateEntity(TABLE_NAME, entities, Columns.of("user_name"));

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_batch1", "name", "name_batch_1_chg", "user_name", "user1", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch2", "name", "name_batch_2_chg", "user_name", "user2", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch3", "name", "name_batch_3_chg", "user_name", "user3", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch4", "name", "name_batch_4_chg", "user_name", "user4", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch5", "name", "name_batch_5_chg", "user_name", "user5", "sl", 0));
        List<NeoMap> valueList = neo.list(TABLE_NAME, NeoMap.of()).stream().map(e->e.assignExcept("id")).collect(Collectors.toList());
        Assert.assertEquals(dataList.toString(), valueList.toString());
    }

    /**
     * 不指定搜索字段，则默认用主键
     */
    @Test
    public void batchUpdateTest4(){
        List<DemoEntity> entities = Arrays.asList(
            new DemoEntity().setGroup("group_batch1").setName("name_batch"),
            new DemoEntity().setGroup("group_batch2").setName("name_batch"),
            new DemoEntity().setGroup("group_batch3").setName("name_batch"),
            new DemoEntity().setGroup("group_batch4").setName("name_batch"),
            new DemoEntity().setGroup("group_batch5").setName("name_batch")
        );
        neo.batchInsertEntity(TABLE_NAME, entities);

        entities = neo.list(DemoEntity.class, TABLE_NAME, NeoMap.of()).stream().map(e-> e.setName("name_batch_" + e.getId() + "_chg")).collect(Collectors.toList());
        neo.batchUpdateEntity(TABLE_NAME, entities);

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_batch1", "name", "name_batch_1_chg", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch2", "name", "name_batch_2_chg", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch3", "name", "name_batch_3_chg", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch4", "name", "name_batch_4_chg", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch5", "name", "name_batch_5_chg", "sl", 0));
        List<NeoMap> valueList = neo.list(TABLE_NAME, NeoMap.of()).stream().map(e->e.assignExcept("id")).collect(Collectors.toList());
        Assert.assertEquals(dataList.toString(), valueList.toString());
    }

    /**
     * 字段不固定
     */
    @Test
    public void batchUpdateTest7(){
        List<NeoMap> maps = Arrays.asList(
            NeoMap.of("group", "group_batch1", "name", "name_batch"),
            NeoMap.of("group", "group_batch2", "name", "name_batch"),
            NeoMap.of("group", "group_batch3", "name", "name_batch"),
            NeoMap.of("group", "group_batch4", "name", "name_batch"),
            NeoMap.of("group", "group_batch5", "name", "name_batch")
        );
        neo.batchInsert(TABLE_NAME, maps);

        maps = neo.list(TABLE_NAME, NeoMap.of()).stream().peek(e->{
            e.put("name", e.getString("name") + "_" + e.getInteger("id") + "_chg");

            if(e.getInteger("id") > 3) {
                e.put("age", e.getInteger("id"));
            }
        }).collect(Collectors.toList());
        neo.batchUpdate(TABLE_NAME, maps);

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_batch1", "name", "name_batch_1_chg"));
        dataList.add(NeoMap.of("group", "group_batch2", "name", "name_batch_2_chg"));
        dataList.add(NeoMap.of("group", "group_batch3", "name", "name_batch_3_chg"));
        dataList.add(NeoMap.of("group", "group_batch4", "name", "name_batch_4_chg", "age", 4));
        dataList.add(NeoMap.of("group", "group_batch5", "name", "name_batch_5_chg", "age", 5));
        List<NeoMap> valueList = neo.list(TABLE_NAME, NeoMap.of()).stream().map(e->e.assignExcept("id")).collect(Collectors.toList());
        Assert.assertEquals(dataList, valueList);
    }

    /**
     * 字段不固定，也就是实体中有数据为空
     */
    @Test
    public void batchUpdateTest7_1(){
        List<DemoEntity> entities = Arrays.asList(
            new DemoEntity().setGroup("group_batch1").setName("name_batch"),
            new DemoEntity().setGroup("group_batch2").setName("name_batch"),
            new DemoEntity().setGroup("group_batch3").setName("name_batch"),
            new DemoEntity().setGroup("group_batch4").setName("name_batch"),
            new DemoEntity().setGroup("group_batch5").setName("name_batch")
        );
        neo.batchInsertEntity(TABLE_NAME, entities);

        entities = neo.list(DemoEntity.class, TABLE_NAME, NeoMap.of()).stream().peek(e-> {
            e.setName("name_batch_" + e.getId() + "_chg");
            if(e.getId() > 3) {
                e.setAge(e.getId().intValue());
            }
        }).collect(Collectors.toList());
        neo.batchUpdateEntity(TABLE_NAME, entities);

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_batch1", "name", "name_batch_1_chg", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch2", "name", "name_batch_2_chg", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch3", "name", "name_batch_3_chg", "sl", 0));
        dataList.add(NeoMap.of("group", "group_batch4", "name", "name_batch_4_chg", "sl", 0, "age", 4));
        dataList.add(NeoMap.of("group", "group_batch5", "name", "name_batch_5_chg", "sl", 0, "age", 5));
        List<NeoMap> valueList = neo.list(TABLE_NAME, NeoMap.of()).stream().map(e->e.assignExcept("id")).collect(Collectors.toList());
        Assert.assertEquals(dataList.toString(), valueList.toString());
    }
}
