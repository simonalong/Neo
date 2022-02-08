package com.simonalong.neo.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.simonalong.neo.NeoMap;
import lombok.experimental.UtilityClass;

/**
 * @author shizi
 * @since 2022-01-21 17:49:53
 */
@UtilityClass
public class NeoEnvContext {

    private final TransmittableThreadLocal<NeoMap> context = new TransmittableThreadLocal<>();

    public void set(String key, Object value) {
        NeoMap currentMap = context.get();
        if (NeoMap.isEmpty(currentMap)) {
            context.set(NeoMap.of(key, value));
        } else {
            currentMap.put(key, value);
            context.set(currentMap);
        }
    }

    public <T> T get(Class<T> tClass, String key) {
        NeoMap currentMap = context.get();
        if (NeoMap.isEmpty(currentMap)) {
            return null;
        }
        return currentMap.get(tClass, key);
    }

    public void clean(String key) {
        NeoMap currentMap = context.get();
        if (!NeoMap.isEmpty(currentMap)) {
            currentMap.remove(key);
            context.set(currentMap);
        }
    }

    public void clean() {
        context.remove();
    }
}
