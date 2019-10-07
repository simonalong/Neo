package com.simonalong.neo.map;

import com.simonalong.neo.annotation.Column;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/10/4 上午11:22
 */
@Data
@Accessors(chain = true)
public class NeoMapEntity2 {

    private Integer age;
    @Column(value = "user_address", table = "neo_table1")
    private String userAddress;
    @Column(value = "name", table = "neo_table1")
    private String userName;
    @Column(value = "data_user", table = "neo_table2")
    private String dataNameUser;
}