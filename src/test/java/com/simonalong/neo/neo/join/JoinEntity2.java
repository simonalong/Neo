package com.simonalong.neo.neo.join;

import com.simonalong.neo.annotation.Column;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shizi
 * @since 2020/3/23 上午11:57
 */
@Data
@Accessors(chain = true)
public class JoinEntity2 {

    @Column(table = "neo_table1")
    private String name;
    @Column(table = "neo_table2")
    private Long id;
}
