package com.simonalong.neo.map;

import com.simonalong.neo.annotation.Column;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/9/8 上午12:57
 */
@Data
@Accessors(chain = true)
public class NeoMapEntity {

    private Integer age;
    @Column("user_address")
    private String userAddress;
    @Column("name")
    private String userName;
    @Column("data_user")
    private String dataNameUser;
}
