package com.simonalong.neo.neo;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.entity.DemoEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.simonalong.neo.exception.NeoException;
import com.simonalong.neo.util.TimeRangeStrUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

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
            NeoMap.of("group", "group_batch", "name", "name_batch_1"),
            NeoMap.of("group", "group_batch", "name", "name_batch_2"),
            NeoMap.of("group", "group_batch", "name", "name_batch_3"),
            NeoMap.of("group", "group_batch", "name", "name_batch_4"),
            NeoMap.of("group", "group_batch", "name", "name_batch_5")
        );
        neo.batchInsert(TABLE_NAME, maps);

        maps = Arrays.asList(
            NeoMap.of("group", "group_batch", "name", "name_batch_1_chg"),
            NeoMap.of("group", "group_batch", "name", "name_batch_2_chg"),
            NeoMap.of("group", "group_batch", "name", "name_batch_3_chg"),
            NeoMap.of("group", "group_batch", "name", "name_batch_4_chg"),
            NeoMap.of("group", "group_batch", "name", "name_batch_5_chg")
        );
        neo.batchUpdate(TABLE_NAME, maps, Columns.of("group"));

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_1_chg"));
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_2_chg"));
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_3_chg"));
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_4_chg"));
        dataList.add(NeoMap.of("group", "group_batch", "name", "name_batch_5_chg"));
        List<NeoMap> valueList = neo.list(TABLE_NAME, NeoMap.of("group", "group_batch")).stream().map(e->e.assignExcept("id")).collect(Collectors.toList());
        Assert.assertEquals(dataList, valueList);
    }

    /**
     * 如果没有指定默认的条件，则会自动根据表中的主键进行过滤
     */
    @Test
    public void batchUpdateTest2() {
        List<NeoMap> maps = Arrays.asList(
            NeoMap.of("id", 1, "group", "group2", "name", "name11chg", "user_name", "v1"),
            NeoMap.of("id", 2, "group", "group2", "name", "name21chg"),
            NeoMap.of("id", 3, "group", "group3", "name", "name31chg"),
            NeoMap.of("id", 4, "group", "group4", "name", "name41chg"),
            NeoMap.of("id", 5, "group", "group5", "name", "name51chg")
        );

        show(neo.batchUpdate(TABLE_NAME, maps));
    }

    /**
     * 异常测试
     * 注意在Entity的批量更新这里，列的选择，里面输入是完全跟列的属性名字相同的，而上面NeoMap的批量更新里面是跟NeoMap的key相同，那里的key一般情况下都是DB中的名字
     */
    @Test(expected = NeoException.class)
    public void batchUpdateTest3() {
        List<DemoEntity> entities = Arrays.asList(
            new DemoEntity().setGroup("group11chg").setName("name1chg").setUserName("userName1"),
            new DemoEntity().setGroup("group12chg").setName("name2chg").setUserName("userName2"),
            new DemoEntity().setGroup("group13chg").setName("name3chg").setUserName("userName3"),
            new DemoEntity().setGroup("group14chg").setName("name4chg").setUserName("userName4"),
            new DemoEntity().setGroup("group15chg").setName("name5chg").setUserName("userName5"),
            new DemoEntity().setGroup("group16chg").setName("name6chg").setUserName("userName6")
        );
        // 错误写法
        show(neo.batchUpdateEntity(TABLE_NAME, entities, Columns.of("userName")));
    }

    @Test
    public void batchUpdateTest3_1() {
        List<DemoEntity> entities = Arrays.asList(
            new DemoEntity().setGroup("group11chg").setName("name1chg").setUserName("userName1"),
            new DemoEntity().setGroup("group12chg").setName("name2chg").setUserName("userName2"),
            new DemoEntity().setGroup("group13chg").setName("name3chg").setUserName("userName3"),
            new DemoEntity().setGroup("group14chg").setName("name4chg").setUserName("userName4"),
            new DemoEntity().setGroup("group15chg").setName("name5chg").setUserName("userName5"),
            new DemoEntity().setGroup("group16chg").setName("name6chg").setUserName("userName6")
        );
        show(neo.batchUpdateEntity(TABLE_NAME, entities, Columns.of("user_name")));
    }

    @Test
    public void batchUpdateTest4(){
        List<DemoEntity> entities = Arrays.asList(
            new DemoEntity().setId(1L).setGroup("group11chg").setName("name12chg"),
            new DemoEntity().setId(2L).setGroup("group12chg").setName("name22chg"),
            new DemoEntity().setId(3L).setGroup("group13chg").setName("name32chg"),
            new DemoEntity().setId(4L).setGroup("group14chg").setName("name42chg"),
            new DemoEntity().setId(5L).setGroup("group15chg").setName("name52chg"),
            new DemoEntity().setId(6L).setGroup("group16chg").setName("name62chg")
        );
        show(neo.batchUpdateEntity(TABLE_NAME, entities));
    }

    @Test
    public void batchUpdateTest5(){
        List<DemoEntity> entities = Arrays.asList(
            new DemoEntity().setId(1L).setGroup("group31chg").setName("name12chg"),
            new DemoEntity().setId(2L).setGroup("group32chg").setName("name22chg"),
            new DemoEntity().setId(3L).setGroup("group33chg").setName("name32chg"),
            new DemoEntity().setId(4L).setGroup("group34chg").setName("name42chg"),
            new DemoEntity().setId(5L).setGroup("group35chg").setName("name52chg"),
            new DemoEntity().setId(6L).setGroup("group36chg").setName("name62chg")
        );
        show(neo.batchUpdateEntity(TABLE_NAME, entities, Columns.of("id", "name")));
    }

    @Test
    public void batchUpdateTest6() {
        List<NeoMap> maps = Arrays.asList(
            NeoMap.of("id", 1, "group", "group1", "name", "name1chg"),
            NeoMap.of("id", 2, "group", "group2", "name", "name2chg"),
            NeoMap.of("id", 3, "group", "group3", "name", "name3chg"),
            NeoMap.of("id", 4, "group", "group4", "name", "name4chg"),
            NeoMap.of("id", 5, "group", "group5", "name", "name5chg")
        );
        show(neo.batchUpdate(TABLE_NAME, maps));
        show(maps);
    }

    /**
     * 字段不固定
     */
    @Test
    public void batchUpdateTest7(){
        List<NeoMap> maps = new ArrayList<>();
        maps.add(NeoMap.of("group", "group3", "name", "name3chg", "user_name", "user_name3"));
        maps.add(NeoMap.of("group", "group5", "name", "name5chg", "user_name", "user_name5"));

        NeoMap dataMap = NeoMap.of();
        dataMap.setSupportValueNull(true);
        dataMap.put("group", "group4");
        dataMap.put("name", "name4chg");
        dataMap.put("user_name", null);
        maps.add(dataMap);

        show(neo.batchUpdate(TABLE_NAME, maps, Columns.of("user_name")));
    }

    @Test
    public void batchUpdateTest7_1(){
        List<DemoEntity> entities = Arrays.asList(
            new DemoEntity().setId(1L).setGroup("group1").setName("name1").setUserName("user_name1"),
            new DemoEntity().setId(2L).setGroup("group2").setName("name2").setUserName("user_name1")
        );
        show(neo.batchUpdateEntity(TABLE_NAME, entities, Columns.of("id")));
    }

    /**
     * 字段不固定，则上报异常，需要将为空的字段，添加为null才行
     */
    @Test(expected = NeoException.class)
    public void batchUpdateTest8() {
        List<NeoMap> maps = Arrays.asList(
            NeoMap.of("group", "group3", "name", "name3chg", "user_name", "user_name3"),
            NeoMap.of("group", "group4", "name", "name4chg"),
            NeoMap.of("group", "group5", "name", "name5chg", "user_name", "user_name5")
        );
        show(neo.batchUpdate(TABLE_NAME, maps, Columns.of("user_name")));
    }

    /**
     * 字段不固定
     */
    @Test
    public void batchUpdateTest9() {
        List<DemoEntity> maps = Arrays.asList(
            new DemoEntity().setId(1L).setGroup("group71chg").setName("name72chg"),
            new DemoEntity().setId(2L).setGroup("group72chg").setName("name72chg").setAge(12).setUserName("user_name83")
        );
        show(neo.batchUpdateEntity(TABLE_NAME, maps));
        show(maps);
    }

    private List<DemoEntity> getList(Integer num) {
        return neo.page(DemoEntity.class, TABLE_NAME, NeoMap.of(), NeoPage.of(1, num)).stream().peek(e->{
            e.setGroup(e.getGroup() + "chg");
            e.setName(e.getName() + "chg");
        }).collect(Collectors.toList());
    }

    /**
     * 大批量更新测试
     */
    @Test
    public void batchUpdate10() {
        Integer num = 3;
        List<DemoEntity> resultList = getList(num);

        long startTime = System.currentTimeMillis();
        show(neo.batchUpdateEntity(TABLE_NAME, resultList, Columns.of("id")));
        long endTime = System.currentTimeMillis();
        show("耗时" + TimeRangeStrUtil.parseTime(endTime - startTime));
    }
}
