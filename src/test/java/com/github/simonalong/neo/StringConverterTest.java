package com.github.simonalong.neo;

import com.github.simonalong.neo.StringConverter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/18 下午11:07
 */
public class StringConverterTest {

    @Test
    public void bigCamelToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", StringConverter.bigCamelToSmallCamel("DataBaseUser"));
    }

    @Test
    public void smallCameToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", StringConverter.bigCamel("dataBaseUser"));
    }

    @Test
    public void underLineToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", StringConverter.underLineToSmallCamel("data_base_user"));
        Assert.assertEquals("dataBaseUser", StringConverter.underLineToSmallCamel("data_base_user_"));
        Assert.assertEquals("dataBaseUser", StringConverter.underLineToSmallCamel("_data_base_user"));
        Assert.assertEquals("dataBaseUser", StringConverter.underLineToSmallCamel("_data_base_user_"));
    }

    @Test
    public void underLineToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", StringConverter.underLineToBigCamel("data_base_user"));
        Assert.assertEquals("DataBaseUser", StringConverter.underLineToBigCamel("data_base_user_"));
        Assert.assertEquals("DataBaseUser", StringConverter.underLineToBigCamel("_data_base_user"));
        Assert.assertEquals("DataBaseUser", StringConverter.underLineToBigCamel("_data_base_user_"));
    }

    @Test
    public void smallCamelToUnderLineTest(){
        Assert.assertEquals("data_base_user", StringConverter.underLine("dataBaseUser"));
    }

    @Test
    public void middleLineToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", StringConverter.middleLineToSmallCamel("data-base-user"));
    }

    @Test
    public void smallCamelToMiddleLineTest(){
        Assert.assertEquals("data-base-user", StringConverter.middleLine("dataBaseUser"));
    }

    @Test
    public void upperUnderToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", StringConverter.upperUnderToSmallCamel("DATA_BASE_USER"));
    }

    @Test
    public void smallCamelToUpperUnderTest(){
        Assert.assertEquals("DATA_BASE_USER", StringConverter.upperUnder("dataBaseUser"));
    }

    @Test
    public void upperMiddleToSmallCamelTest(){
        Assert.assertEquals("dataBaseUser", StringConverter.upperUnderMiddleToSmallCamel("DATA-BASE-USER"));
    }

    @Test
    public void smallCamelToUpperMiddleTest(){
        Assert.assertEquals("DATA-BASE-USER", StringConverter.upperUnderMiddle("dataBaseUser"));
    }

    @Test
    public void smallCamelToPostUnderTest(){
        Assert.assertEquals("data_base_user_", StringConverter.postUnder("dataBaseUser"));
    }

    @Test
    public void smallCamelToPreUnderTest(){
        Assert.assertEquals("_data_base_user", StringConverter.preUnder("dataBaseUser"));
    }

    @Test
    public void smallCamelToPrePostUnderTest(){
        Assert.assertEquals("_data_base_user_", StringConverter.prePostUnder("dataBaseUser"));
    }

    @Test
    public void bigCamelToUnderLineTest(){
        Assert.assertEquals("data_base_user", StringConverter.bigCamelToUnderLine("DataBaseUser"));
    }

    @Test
    public void middleLineToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", StringConverter.middleLineToBigCamel("data-base-user"));
    }

    @Test
    public void bigCamelToMiddleLineTest(){
        Assert.assertEquals("data-base-user", StringConverter.bigCamelToMiddleLine("DataBaseUser"));
    }

    @Test
    public void upperUnderToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", StringConverter.upperUnderToBigCamel("DATA_BASE_USER"));
    }

    @Test
    public void bigCamelToUpperUnderTest(){
        Assert.assertEquals("DATA_BASE_USER", StringConverter.bigCamelToUpperUnder("DataBaseUser"));
    }

    @Test
    public void upperMiddleToBigCamelTest(){
        Assert.assertEquals("DataBaseUser", StringConverter.upperMiddleToBigCamel("DATA-BASE-USER"));
    }

    @Test
    public void bigCamelToUpperMiddleTest(){
        Assert.assertEquals("DATA-BASE-USER", StringConverter.bigCamelToUpperMiddle("DataBaseUser"));
    }

    @Test
    public void bigCamelToPostUnderTest(){
        Assert.assertEquals("data_base_user_", StringConverter.bigCamelToPostUnder("DataBaseUser"));
    }

    @Test
    public void bigCamelToPreUnderTest(){
        Assert.assertEquals("_data_base_user", StringConverter.bigCamelToPreUnder("DataBaseUser"));
    }

    @Test
    public void bigCamelToPrePostUnderTest(){
        Assert.assertEquals("_data_base_user_", StringConverter.bigCamelToPrePostUnder("DataBaseUser"));
    }
}
