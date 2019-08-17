package com.simonalong.neo.table;

import static com.simonalong.neo.table.AliasParser.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/5/12 上午12:48
 */
public class AliasParserTest {

    @Test
    public void haveAliasTest1(){
        Assert.assertEquals(true, haveAlias("group as g"));
        Assert.assertEquals(true, haveAlias("group  g"));
        Assert.assertEquals(true, haveAlias("group g"));
        Assert.assertEquals(false, haveAlias("groupg"));
    }

    @Test
    public void haveAliasTest2(){
        Assert.assertEquals(true, haveAlias("table.group as g"));
        Assert.assertEquals(true, haveAlias("table.group  g"));
        Assert.assertEquals(true, haveAlias("table.group g"));
        Assert.assertEquals(false, haveAlias("table.groupg"));
    }

    @Test
    public void metaDataTest(){
        Assert.assertEquals("group", metaData("group as g"));
        Assert.assertEquals("`group`", metaData("`group` as g"));
        Assert.assertEquals("group", metaData("group  g"));
        Assert.assertEquals("group", metaData("group g"));
        Assert.assertEquals("groupg", metaData("groupg"));
    }

    @Test
    public void getAliasTest(){
        Assert.assertEquals("g", getAlias("group as g"));
        Assert.assertEquals("g", getAlias("`group` as g"));
        Assert.assertEquals("g", getAlias("group  g"));
        Assert.assertEquals("g", getAlias("group g"));
        Assert.assertEquals("groupg", getAlias("groupg"));
    }

}
