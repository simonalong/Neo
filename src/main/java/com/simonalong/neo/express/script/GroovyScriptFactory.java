package com.simonalong.neo.express.script;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * groovy脚本类享元工厂
 *
 * @author zhouzhenyong
 * @since 2019/1/17 下午7:33
 */
class GroovyScriptFactory {

    private static final Map<String, Class<Script>> scriptCache = new HashMap<>();
    private final GroovyClassLoader classLoader = new GroovyClassLoader();
    private static final GroovyScriptFactory factory = new GroovyScriptFactory();

    /**
     * 设置为单例模式
     */
    private GroovyScriptFactory() {
    }

    static GroovyScriptFactory getInstance() {
        return factory;
    }

    private Class<Script> getScript(String key) {
        if (scriptCache.containsKey(key)) {
            return scriptCache.get(key);
        } else {
            // 脚本不存在则创建新的脚本
            @SuppressWarnings("unchecked")
            Class<Script> scriptClass = classLoader.parseClass(key);
            scriptCache.put(key, scriptClass);
            return scriptClass;
        }
    }

    private Object run(Class<Script> script, Binding binding) {
        Script scriptObj = InvokerHelper.createScript(script, binding);
        Object result = scriptObj.run();
        // 每次脚本执行完之后，一定要清理掉内存
        classLoader.clearCache();
        return result;
    }

    Object scriptGetAndRun(String key, Binding binding) {
        return run(getScript(key), binding);
    }
}
