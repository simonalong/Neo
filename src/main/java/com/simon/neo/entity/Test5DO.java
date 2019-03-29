package com.simon.neo.entity;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 配置项
 * @author robot
 */
@Data
public class Test5DO {

    private Long id;
    /**
     * 数据来源组，外键关联lk_config_group
     */
    private String group;
    /**
     * 任务name
     */
    private String name;
    /**
     * 修改人名字
     */
    private String userName;
    /**
     * 性别：Y=男；N=女
     */
    private String gander;
    private Long biginta;
    private byte[] binarya;
    private Boolean bit2;
    private byte[] blob2;
    private Boolean boolean2;
    private String char1;
    private Timestamp datetime1;
    private Date date2;
    private BigDecimal decimal1;
    private Double double1;
    private String enum1;
    private Float float1;
    private byte[] geometry;
    private byte[] geometrycollection;
    private Integer int2;
    private String json1;
    private byte[] linestring;
    private byte[] longblob;
    private String longtext;
    private byte[] medinumblob;
    private Integer medinumint;
    private String mediumtext;
    private byte[] multilinestring;
    private byte[] multipoint;
    private byte[] mutipolygon;
    private byte[] point;
    private byte[] polygon;
    private Integer smallint;
    private String text;
    private Time time;
    private Timestamp timestamp;
    private byte[] tinyblob;
    private Integer tinyint;
    private String tinytext;
    private String text1;
    private String text1123;
    private Time time1;
    private Timestamp timestamp1;
    private byte[] tinyblob1;
    private Integer tinyint1;
    private String tinytext1;
    private Date year2;

}
