package com.simon.neo;

import com.simon.neo.NeoMap.NamingChg;
import com.simon.neo.entity.DemoEntity;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:49
 */
public class NeoMapTest {
    
    private static final String TABLE_NAME = "neo_table1";

    @Test
    public void testAppend(){
        NeoMap neoMap1 = NeoMap.of("a", 123);
        NeoMap neoMap2 = NeoMap.of("b", 123);

        NeoMap data = NeoMap.of().append(neoMap1).append(neoMap2);
        System.out.println(data);
    }

    @Test
    public void testAppend2(){
        NeoMap neoMap1 = NeoMap.of("a", 111);
        NeoMap neoMap2 = NeoMap.of("a", 222);

        NeoMap data = NeoMap.of().append(neoMap1).append(neoMap2);
        System.out.println(data);
    }

    /**
     * 默认情况下，属性名和map的key完全一致
     */
    @Test
    public void testAs1() {
        NeoMap map1 = NeoMap.of("user_name", "name", "id", 123L, "data_base_name", TABLE_NAME);
        DemoEntity demo1 = map1.as(DemoEntity.class);
        // 只有id完全匹配
        // DemoEntity(group=null, name=null, userName=null, id=123, dataBaseName=null)
        System.out.println(demo1);
    }

    /**
     * 可以指定全局命名转换规则，比如UNDERLINE，就是 dataBaseUser -> data_base_user，请注意，该设置，会对所有的NeoMap生效
     */
    @Test
    public void testAs2() {
        NeoMap.setDefaultNamingChg(NamingChg.UNDERLINE);
        NeoMap map2 = NeoMap.of("user_name", "name", "id", 123L, "data_base_name", TABLE_NAME);
        DemoEntity demo2 = map2.as(DemoEntity.class);
        // 其中，user_name、id和data_base_name都能匹配上
        // DemoEntity(group=null, name=null, userName=name, id=123, dataBaseName=neo_table1)
        System.out.println(demo2);
    }

    /**
     * 如果不想使用全局，则可以通过设置本类的自己的命名转换，可以覆盖全局的
     */
    @Test
    public void testAs3() {
        NeoMap.setDefaultNamingChg(NamingChg.UNDERLINE);
        NeoMap map3 = NeoMap.of("_user_name", "name", "id", 123L, "data_base_name", TABLE_NAME);
        // 设置本地命名转换，用于覆盖全局命名转换：dataBaseUser -> _data_base_user
        map3.setLocalNaming(NamingChg.PREUNDER);
        // 其中，只有_user_name能匹配上
        // DemoEntity(group=null, name=null, userName=name, id=null, dataBaseName=null)
        DemoEntity demo3 = map3.as(DemoEntity.class);
        System.out.println(demo3);
    }

    /**
     * 在as的时候单独设置命名转换
     */
    @Test
    public void testAs4() {
        NeoMap.setDefaultNamingChg(NamingChg.DEFAULT);
        NeoMap map4 = NeoMap.of("user_name", "name", "id", 123L, "data-base-name", TABLE_NAME);
        // dataBaseUser -> data-base-user
        DemoEntity demo4 = map4.as(DemoEntity.class, NamingChg.MIDDLELINE);
        // DemoEntity(group=null, name=null, userName=null, id=123, dataBaseName=neo_table1)
        System.out.println(demo4);
    }

    @Test
    public void testFrom1(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        NeoMap neoMap = NeoMap.from(demo);
        // NeoMap={dataBaseName=databasename, group=group1, id=212, name=name1, userName=userName1}
        System.out.println(neoMap);
    }

    @Test
    public void testFrom2(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        NeoMap neoMap = NeoMap.fromInclude(demo, "group", "name");
        // NeoMap={group=group1, name=name1}
        System.out.println(neoMap);
    }

    @Test
    public void testFrom3(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        NeoMap neoMap = NeoMap.fromExclude(demo, "group", "name");
        // NeoMap={dataBaseName=databasename, id=212, userName=userName1}
        System.out.println(neoMap);
    }

    @Test
    public void testFrom4(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        // 将map的key全部转换为下划线
        NeoMap neoMap = NeoMap.from(demo, NamingChg.UNDERLINE);
        // NeoMap={data_base_name=databasename, group=group1, id=212, name=name1, user_name=userName1}
        System.out.println(neoMap);
    }

    @Test
    public void testFrom5(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        // 将map的key全部转换为下划线
        NeoMap neoMap = NeoMap.fromInclude(demo, "userName");
        // NeoMap={data_base_name=databasename, group=group1, id=212, name=name1, user_name=userName1}
        System.out.println(neoMap);
    }

    @Test
    public void testFrom6(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        // 将map的key全部转换为下划线
        NeoMap neoMap = NeoMap.from(demo, Columns.of("userName"));
        // NeoMap={data_base_name=databasename, group=group1, id=212, name=name1, user_name=userName1}
        System.out.println(neoMap);
    }

    @Test
    public void testFrom7(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        // 将map的key全部转换为下划线
        NeoMap neoMap = NeoMap.from(demo, Columns.of("userName"), NamingChg.UNDERLINE);
        // NeoMap={data_base_name=databasename, group=group1, id=212, name=name1, user_name=userName1}
        System.out.println(neoMap);
    }

    /**
     * 用于自定义命名转换
     */
    @Test
    public void testUserDefineNaming(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212L);

        // 自定义转换规则
        NeoMap namingChg = NeoMap.of().append("group", "m_group")
            .append("name", "m_name")
            .append("id", "m_id")
            .append("dataBaseName", "m_data_base_name")
            .append("userName", "m_user_name");

        NeoMap neoMap = NeoMap.from(demo, namingChg);

        // NeoMap={m_data_base_name=databasename, m_group=group1, m_id=212, m_name=name1, m_user_name=userName1}
        System.out.println(neoMap);

        // 生成新的数据
        DemoEntity newDemo = neoMap.as(DemoEntity.class);
        // DemoEntity(group=group1, name=name1, userName=userName1, id=212, dataBaseName=databasename)
        System.out.println(newDemo);

        // 数据完全一直
        Assert.assertEquals(demo, newDemo);
    }

    @Test
    public void testAssign(){
        NeoMap neoMap1 = NeoMap.of("a", "1", "b", "2", "c", "3");
        NeoMap neoMap2 = NeoMap.of("a", "1", "c", "3");
        NeoMap neoMapResult = neoMap1.assign(Columns.of("a", "c"));

        Assert.assertEquals(neoMap2.toString(), neoMapResult.toString());
    }

}
