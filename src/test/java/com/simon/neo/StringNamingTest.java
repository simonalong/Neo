package com.simon.neo;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/18 下午11:07
 */
public class StringNamingTest {

    @Test
    public void bigCamelToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", StringNaming.bigCamelToSmallCamel("DataBaseUser"));
    }

    @Test
    public void smallCameToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", StringNaming.smallCamelToBigCamel("dataBaseUser"));
    }

    @Test
    public void underLineToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", StringNaming.underLineToSmallCamel("data_base_user"));
        Assert.assertEquals("dataBaseUser", StringNaming.underLineToSmallCamel("data_base_user_"));
        Assert.assertEquals("dataBaseUser", StringNaming.underLineToSmallCamel("_data_base_user"));
        Assert.assertEquals("dataBaseUser", StringNaming.underLineToSmallCamel("_data_base_user_"));
    }

    @Test
    public void underLineToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", StringNaming.underLineToBigCamel("data_base_user"));
        Assert.assertEquals("DataBaseUser", StringNaming.underLineToBigCamel("data_base_user_"));
        Assert.assertEquals("DataBaseUser", StringNaming.underLineToBigCamel("_data_base_user"));
        Assert.assertEquals("DataBaseUser", StringNaming.underLineToBigCamel("_data_base_user_"));
    }

    @Test
    public void smallCamelToUnderLineTest(){
        Assert.assertEquals("data_base_user", StringNaming.smallCamelToUnderLine("dataBaseUser"));
    }

    @Test
    public void middleLineToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", StringNaming.middleLineToSmallCamel("data-base-user"));
    }

    @Test
    public void smallCamelToMiddleLineTest(){
        Assert.assertEquals("data-base-user", StringNaming.smallCamelToMiddleLine("dataBaseUser"));
    }

    @Test
    public void upperUnderToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", StringNaming.upperUnderToSmallCamel("DATA_BASE_USER"));
    }

    @Test
    public void smallCamelToUpperUnderTest(){
        Assert.assertEquals("DATA_BASE_USER", StringNaming.smallCamelToUpperUnder("dataBaseUser"));
    }

    @Test
    public void upperMiddleToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", StringNaming.upperMiddleToSmallCamel("DATA-BASE-USER"));
    }

    @Test
    public void smallCamelToUpperMiddleTest(){
        Assert.assertEquals("DATA-BASE-USER", StringNaming.smallCamelToUpperMiddle("dataBaseUser"));
    }

    @Test
    public void smallCamelToPostUnderTest(){
        Assert.assertEquals("data_base_user_", StringNaming.smallCamelToPostUnder("dataBaseUser"));
    }

    @Test
    public void smallCamelToPreUnderTest(){
        Assert.assertEquals("_data_base_user", StringNaming.smallCamelToPreUnder("dataBaseUser"));
    }

    @Test
    public void smallCamelToPrePostUnderTest(){
        Assert.assertEquals("_data_base_user_", StringNaming.smallCamelToPrePostUnder("dataBaseUser"));
    }

    @Test
    public void bigCamelToUnderLineTest(){
        Assert.assertEquals("data_base_user", StringNaming.bigCamelToUnderLine("DataBaseUser"));
    }

    @Test
    public void middleLineToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", StringNaming.middleLineToBigCamel("data-base-user"));
    }

    @Test
    public void bigCamelToMiddleLineTest(){
        Assert.assertEquals("data-base-user", StringNaming.bigCamelToMiddleLine("DataBaseUser"));
    }

    @Test
    public void upperUnderToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", StringNaming.upperUnderToBigCamel("DATA_BASE_USER"));
    }

    @Test
    public void bigCamelToUpperUnderTest(){
        Assert.assertEquals("DATA_BASE_USER", StringNaming.bigCamelToUpperUnder("DataBaseUser"));
    }

    @Test
    public void upperMiddleToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", StringNaming.upperMiddleToBigCamel("DATA-BASE-USER"));
    }

    @Test
    public void bigCamelToUpperMiddleTest(){
        Assert.assertEquals("DATA-BASE-USER", StringNaming.bigCamelToUpperMiddle("DataBaseUser"));
    }

    @Test
    public void bigCamelToPostUnderTest(){
        Assert.assertEquals("data_base_user_", StringNaming.bigCamelToPostUnder("DataBaseUser"));
    }

    @Test
    public void bigCamelToPreUnderTest(){
        Assert.assertEquals("_data_base_user", StringNaming.bigCamelToPreUnder("DataBaseUser"));
    }

    @Test
    public void bigCamelToPrePostUnderTest(){
        Assert.assertEquals("_data_base_user_", StringNaming.bigCamelToPrePostUnder("DataBaseUser"));
    }

    @Test
    public void prefixDelToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", StringNaming.prefixDelToSmallCamel("test_", "test_data_base_user_"));
        Assert.assertEquals("dataBaseUser", StringNaming.prefixDelToSmallCamel("test_", "test_data_base_user"));
    }

    @Test
    public void prefixDelToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", StringNaming.prefixDelToBigCamel("test_", "test_data_base_user_"));
        Assert.assertEquals("DataBaseUser", StringNaming.prefixDelToBigCamel("test_", "test_data_base_user"));
    }
}
