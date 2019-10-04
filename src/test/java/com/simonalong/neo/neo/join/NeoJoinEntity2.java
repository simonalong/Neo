package com.simonalong.neo.neo.join;

import com.simonalong.neo.annotation.Column;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/10/5 上午2:16
 */
@Data
@Accessors(chain = true)
public class NeoJoinEntity2 {

    @Column(value = "name", table = "neo_table1")
    private String name1;
    @Column(value = "id", table = "neo_table1")
    private Long id1;
    @Column(value = "age", table = "neo_table1")
    private Integer age1;
    @Column(value = "sort", table = "neo_table1")
    private Integer sort1;
    @Column(value = "enum", table = "neo_table1")
    private String enum1;
    @Column(value = "user_name", table = "neo_table1")
    private String userName1;
    @Column(value = "group", table = "neo_table1")
    private String group1;

    @Column(value = "name", table = "neo_table2")
    private String name2;
    @Column(value = "id", table = "neo_table2")
    private Long id2;
    @Column(value = "age", table = "neo_table2")
    private Integer age2;
    @Column(value = "sort", table = "neo_table2")
    private Integer sort2;
    @Column(value = "enum", table = "neo_table2")
    private String enum2;
    @Column(value = "user_name", table = "neo_table2")
    private String userName2;
    @Column(value = "group", table = "neo_table2")
    private String group2;
}
