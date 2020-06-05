package com.simonalong.neo.neo;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.entity.DemoEntity;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.simonalong.neo.exception.NeoException;
import com.simonalong.neo.util.TimeRangeStrUtil;
import org.junit.Test;

/**
 * 批次执行
 * @author zhouzhenyong
 * @since 2019/3/25 下午20:04
 */
public class NeoBatchTest extends NeoBaseTest {

    public NeoBatchTest() throws SQLException {}

    @Test
    public void batchInsertTest1(){
        List<NeoMap> maps = Arrays.asList(
          NeoMap.of("group", "group1", "name", "name1", "user_name", "user_name1"),
          NeoMap.of("group", "group2", "name", "name2", "user_name", "user_name2"),
          NeoMap.of("group", "group3", "name", "name3", "user_name", "user_name3"),
          NeoMap.of("group", "group4", "name", "name4", "user_name", "user_name4"),
          NeoMap.of("group", "group5", "name", "name5", "user_name", "user_name5")
        );
        show(neo.batchInsert(TABLE_NAME, maps));

        show(maps);
    }

    @Test
    public void batchInsertTest2(){
        List<DemoEntity> entities = Arrays.asList(
            new DemoEntity().setGroup("group11").setName("name11"),
            new DemoEntity().setGroup("group12").setName("name12"),
            new DemoEntity().setGroup("group13").setName("name13"),
            new DemoEntity().setGroup("group14").setName("name14"),
            new DemoEntity().setGroup("group15").setName("name15"),
            new DemoEntity().setGroup("group16").setName("name16")
        );
        show(neo.batchInsertEntity(TABLE_NAME, entities));
    }

    @Test
    public void batchInsertTest3(){
        List<DemoEntity> entities = Arrays.asList(
            new DemoEntity().setGroup("group11").setName("name11"),
            new DemoEntity().setGroup("group12").setName("name12"),
            new DemoEntity().setGroup("group13").setName("name13"),
            new DemoEntity().setGroup("group14").setName("name14"),
            new DemoEntity().setGroup("group15").setName("name15"),
            new DemoEntity().setGroup("group16").setName("name16")
        );
        neo.batchInsertEntity(TABLE_NAME, entities);
    }

    /**
     * 测试每一项在不一样的情况下的插入
     */
    @Test
    public void batchInsertTest4(){
        List<NeoMap> maps = Arrays.asList(
            NeoMap.of("group", "group1", "name", "name1", "user_name", "user_name1"),
            NeoMap.of("group", "group2", "name", "name2", "user_name", "user_name2"),
            NeoMap.of("group", "group3", "name", "name3", "user_name", "user_name3")
        );
        show(neo.batchInsert(TABLE_NAME, maps));
        show(maps);
    }

    @Test
    public void batchInsertTest5(){
        List<DemoEntity> entities = Arrays.asList(
            new DemoEntity().setGroup("group11").setName("name11").setAge(123),
            new DemoEntity().setGroup("group12").setName("name12"),
            new DemoEntity().setGroup("group13").setName("name13"),
            new DemoEntity().setGroup("group14").setName("name14"),
            new DemoEntity().setGroup("group15").setName("name15"),
            new DemoEntity().setGroup("group16").setName("name16")
        );
        show(neo.batchInsertEntity(TABLE_NAME, entities));
    }

    @Test
    public void batchUpdateTest1(){
        List<NeoMap> maps = Arrays.asList(
            NeoMap.of("group", "group1", "name", "name1chg", "user_name", "user_name1"),
            NeoMap.of("group", "group2", "name", "name2chg", "user_name", "user_name2"),
            NeoMap.of("group", "group3", "name", "name3chg", "user_name", "user_name3"),
            NeoMap.of("group", "group4", "name", "name4chg", "user_name", "user_name4"),
            NeoMap.of("group", "group5", "name", "name5chg", "user_name", "user_name5")
        );
        show(neo.batchUpdate(TABLE_NAME, maps, Columns.of("user_name")));
    }

    /**
     * 如果没有指定默认的条件，则会自动根据表中的主键进行过滤
     */
    @Test
    public void batchUpdateTest2() {
        List<NeoMap> maps = Arrays.asList(
            NeoMap.of("id", 1, "group", "group1", "name", "name11chg"),
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
        show(neo.batchUpdateEntity(TABLE_NAME, entities, Columns.of("userName")));
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
        List<NeoMap> maps = Arrays.asList(
            NeoMap.of("group", "group3", "name", "name3chg", "user_name", "user_name3"),
            NeoMap.of("group", "group4", "name", "name4chg", "user_name", null),
            NeoMap.of("group", "group5", "name", "name5chg", "user_name", "user_name5")
        );
        show(neo.batchUpdate(TABLE_NAME, maps, Columns.of("group")));
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
