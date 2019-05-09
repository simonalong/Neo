package com.simon.neo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:52
 */
@Setter
@Getter
@Accessors(chain = true)
@EqualsAndHashCode(of = {"group", "name", "id"})
public class DemoEntity {

    private String group;
    private String name;
    private String userName;
    private Long id;
    private String dataBaseName;
}
