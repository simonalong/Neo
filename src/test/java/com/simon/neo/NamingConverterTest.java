package com.simon.neo;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/18 下午11:07
 */
public class NamingConverterTest {

    @Test
    public void bigCamelToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", NamingConverter.bigCamelToSmallCamel("DataBaseUser"));
    }

    @Test
    public void smallCameToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", NamingConverter.bigCamel("dataBaseUser"));
    }

    @Test
    public void underLineToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", NamingConverter.underLineToSmallCamel("data_base_user"));
        Assert.assertEquals("dataBaseUser", NamingConverter.underLineToSmallCamel("data_base_user_"));
        Assert.assertEquals("dataBaseUser", NamingConverter.underLineToSmallCamel("_data_base_user"));
        Assert.assertEquals("dataBaseUser", NamingConverter.underLineToSmallCamel("_data_base_user_"));
    }

    @Test
    public void underLineToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", NamingConverter.underLineToBigCamel("data_base_user"));
        Assert.assertEquals("DataBaseUser", NamingConverter.underLineToBigCamel("data_base_user_"));
        Assert.assertEquals("DataBaseUser", NamingConverter.underLineToBigCamel("_data_base_user"));
        Assert.assertEquals("DataBaseUser", NamingConverter.underLineToBigCamel("_data_base_user_"));
    }

    @Test
    public void smallCamelToUnderLineTest(){
        Assert.assertEquals("data_base_user", NamingConverter.underLine("dataBaseUser"));
    }

    @Test
    public void middleLineToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", NamingConverter.middleLineToSmallCamel("data-base-user"));
    }

    @Test
    public void smallCamelToMiddleLineTest(){
        Assert.assertEquals("data-base-user", NamingConverter.middleLine("dataBaseUser"));
    }

    @Test
    public void upperUnderToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", NamingConverter.upperUnderToSmallCamel("DATA_BASE_USER"));
    }

    @Test
    public void smallCamelToUpperUnderTest(){
        Assert.assertEquals("DATA_BASE_USER", NamingConverter.upperUnder("dataBaseUser"));
    }

    @Test
    public void upperMiddleToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", NamingConverter.upperUnderMiddleToSmallCamel("DATA-BASE-USER"));
    }

    @Test
    public void smallCamelToUpperMiddleTest(){
        Assert.assertEquals("DATA-BASE-USER", NamingConverter.upperUnderMiddle("dataBaseUser"));
    }

    @Test
    public void smallCamelToPostUnderTest(){
        Assert.assertEquals("data_base_user_", NamingConverter.postUnder("dataBaseUser"));
    }

    @Test
    public void smallCamelToPreUnderTest(){
        Assert.assertEquals("_data_base_user", NamingConverter.preUnder("dataBaseUser"));
    }

    @Test
    public void smallCamelToPrePostUnderTest(){
        Assert.assertEquals("_data_base_user_", NamingConverter.prePostUnder("dataBaseUser"));
    }

    @Test
    public void bigCamelToUnderLineTest(){
        Assert.assertEquals("data_base_user", NamingConverter.bigCamelToUnderLine("DataBaseUser"));
    }

    @Test
    public void middleLineToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", NamingConverter.middleLineToBigCamel("data-base-user"));
    }

    @Test
    public void bigCamelToMiddleLineTest(){
        Assert.assertEquals("data-base-user", NamingConverter.bigCamelToMiddleLine("DataBaseUser"));
    }

    @Test
    public void upperUnderToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", NamingConverter.upperUnderToBigCamel("DATA_BASE_USER"));
    }

    @Test
    public void bigCamelToUpperUnderTest(){
        Assert.assertEquals("DATA_BASE_USER", NamingConverter.bigCamelToUpperUnder("DataBaseUser"));
    }

    @Test
    public void upperMiddleToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", NamingConverter.upperMiddleToBigCamel("DATA-BASE-USER"));
    }

    @Test
    public void bigCamelToUpperMiddleTest(){
        Assert.assertEquals("DATA-BASE-USER", NamingConverter.bigCamelToUpperMiddle("DataBaseUser"));
    }

    @Test
    public void bigCamelToPostUnderTest(){
        Assert.assertEquals("data_base_user_", NamingConverter.bigCamelToPostUnder("DataBaseUser"));
    }

    @Test
    public void bigCamelToPreUnderTest(){
        Assert.assertEquals("_data_base_user", NamingConverter.bigCamelToPreUnder("DataBaseUser"));
    }

    @Test
    public void bigCamelToPrePostUnderTest(){
        Assert.assertEquals("_data_base_user_", NamingConverter.bigCamelToPrePostUnder("DataBaseUser"));
    }
}
