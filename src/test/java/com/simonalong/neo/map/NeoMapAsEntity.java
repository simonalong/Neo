package com.simonalong.neo.map;

import com.simonalong.neo.annotation.Column;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author shizi
 * @since 2020/3/21 下午11:05
 */
@Data
@Accessors(chain = true)
public class NeoMapAsEntity {

    private String name;
    private String userName;
    @Column("friend_name")
    private String myFriendName;
    private Integer age;
    private Date time;
}
