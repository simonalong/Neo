package com.simonalong.neo.entity;

import com.simonalong.neo.annotation.Column;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:52
 */
@Setter
@Getter
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
public class DemoEntity {

    private String group;
    private String name;
    private String userName;
    private Long id;
    @Column("data_name")
    private String dataBaseName;
    private Integer a;
    private long sl = 0;
    private Date utilDate;
    private java.sql.Date sqlDate;
    private Time time;
    private Timestamp timestamp;
}
