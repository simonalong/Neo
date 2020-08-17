package com.simonalong.neo.db;

import com.simonalong.neo.Neo;
import lombok.experimental.UtilityClass;

/**
 * @author shizi
 * @since 2020/8/17 5:39 PM
 */
@UtilityClass
public class NeoContext {

    private ThreadLocal<Neo> dbTypeLocal = new ThreadLocal<>();

    public void load(Neo neo) {
        dbTypeLocal.set(neo);
    }

    public void clear() {
        dbTypeLocal.remove();
    }

    public Neo getNeo() {
        return dbTypeLocal.get();
    }
}
