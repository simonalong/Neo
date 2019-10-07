package com.simonalong.neo.neo.join;

import com.simonalong.neo.annotation.Column;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/10/4 上午11:59
 */
@Data
@Accessors(chain = true)
public class JoinEntity {

    @Column(value = "name", table = "t1")
    private String name1;
    @Column(value = "id", table = "t1")
    private Long id1;
    @Column(value = "age", table = "t1")
    private Integer age1;
    @Column(value = "sort", table = "t1")
    private Integer sort1;
    @Column(value = "enum", table = "t1")
    private String enum1;
    @Column(value = "user_name", table = "t1")
    private String userName1;
    @Column(value = "group", table = "t1")
    private String group1;

    @Column(value = "name", table = "t2")
    private String name2;
    @Column(value = "id", table = "t2")
    private Long id2;
    @Column(value = "age", table = "t2")
    private Integer age2;
    @Column(value = "sort", table = "t2")
    private Integer sort2;
    @Column(value = "enum", table = "t2")
    private String enum2;
    @Column(value = "user_name", table = "t2")
    private String userName2;
    @Column(value = "group", table = "t2")
    private String group2;
}
