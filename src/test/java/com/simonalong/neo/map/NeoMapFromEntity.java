package com.simonalong.neo.map;

import com.simonalong.neo.annotation.Column;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author shizi
 * @since 2020/3/21 下午11:21
 */
@Data
@Accessors(chain = true)
public class NeoMapFromEntity {

    private String name;
    private String userName;
    @Column("friend_name")
    private String myFriendName;
    private Integer age;
    private Date date;

    private Date utilDate;
    private java.sql.Date sqlDate;
    private Time time;
    private Timestamp timestamp;
}
