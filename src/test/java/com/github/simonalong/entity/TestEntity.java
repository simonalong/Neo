package com.github.simonalong.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/6/30 上午12:58
 */
@Setter
@Getter
@Accessors(chain = true)
@ToString
public class TestEntity {
    private String userName;
    private String group;
    private String name;
    private Long id;
    private String dataBaseName;
}
