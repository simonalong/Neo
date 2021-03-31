package com.simonalong.neo.express;

import java.util.*;

import com.simonalong.neo.NeoQueue;

import static com.simonalong.neo.express.BaseOperate.*;

/**
 * 运算符
 *
 * @author shizi
 * @since 2020/8/29 4:36 下午
 */
public interface Operate {

    /**
     * 生成运算符
     *
     * @return 运算符表达式
     */
    String generateOperate();

    /**
     * 获取操作符
     * @return 操作符
     */
    String getOperateSymbol();

    /**
     * 添加运算符数据
     *
     * @param value 待加入的运算
     */
    void offerOperate(Operate value);

    /**
     * 值是否合法
     *
     * <p>
     *     默认对值进行这种拦截处理
     *     <ul>
     *         <li>1.值为null，则默认为不合法</li>
     *         <li>2.如果为字符类型，则如果为空字符，则认为不合法</li>
     *         <li>1.如果为集合类型，则如果空，则认为不合法</li>
     *     </ul>
     *
     * @return true：合法，false：不合法
     */
    Boolean valueLegal();

    /**
     * 该表达式出现是否必需得有where语句
     *
     * @return true：必需，false：不必
     */
    default Boolean needWhere() {
        if(!valueLegal()) {
            return false;
        }

        return doNeedWhere();
    }

    Boolean doNeedWhere();

    /**
     * 获取值的队列
     *
     * @return 值队列
     */
    NeoQueue<Object> getValueQueue();

    /**
     * 查找等于操作符的key为某个列名对应的值
     *
     * @param columnName    列名
     * @param operateSymbol 符号
     * @return 列对应的值
     */
    Object getValueFromColumnOfOperate(String columnName, String operateSymbol);

    /**
     * 根据表达式获取表达式的第一个整体字符
     * <p>
     *     比如：根据=获取 得到{@code `a` = 12}
     * @param operateSymbol 表达式符号
     * @return 表达的前后字符
     */
    String getFirstOperateStr(String operateSymbol);

    /**
     * 根据表达式获取表达式所有的整体字符
     * <p>
     *     比如：根据=获取 得到{@code `a` = 12}
     * @param operateSymbol 表达式符号
     * @return 表达的前后字符
     */
    List<String> getAllOperateStr(String operateSymbol);

    /**
     * 解析数据为操作符队列
     *
     * @param logicOperate 逻辑操作符
     * @param objects      对象
     * @return 操作符队列
     */
    @SuppressWarnings({"rawtypes"})
    static NeoQueue<Operate> parse(SearchQuery.LogicEnum logicOperate, Object... objects) {
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        List<Object> parameters = Arrays.asList(objects);

        for (int index = 0; index < parameters.size(); index++) {
            Object parameter = parameters.get(index);

            // key-value处理：key必须为String类型，key后面必须为对应的value，kv形式默认转为无括号的and
            if (parameter instanceof String) {
                Operate operate;
                String key = (String) parameter;
                Object value = null;
                boolean haveValue = false;
                if(++index < parameters.size()) {
                    value = parameters.get(index);
                    haveValue = true;
                }

                switch (logicOperate) {
                    case AND:
                        operate = And(key, value);
                        break;
                    case AND_EM:
                        operate = AndEm(key, value);
                        break;
                    case OR:
                        operate = Or(key, value);
                        break;
                    case OR_EM:
                        operate = OrEm(key, value);
                        break;
                    case EMPTY:
                        if(haveValue) {
                            operate = Em(key, value);
                        } else{
                            operate = Em(key);
                        }
                        break;
                    default:
                        operate = Em(key, value);
                        break;
                }

                operateQueue.add(operate);
                continue;
            }

            // Operate类型处理
            if (parameter instanceof Operate) {
                Operate operate;
                switch (logicOperate) {
                    case AND:
                        operate = BaseOperate.And((Operate) parameter);
                        break;
                    case AND_EM:
                        operate = BaseOperate.AndEm((Operate) parameter);
                        break;
                    case OR:
                        operate = BaseOperate.Or((Operate) parameter);
                        break;
                    case OR_EM:
                        operate = BaseOperate.OrEm((Operate) parameter);
                        break;
                    case EMPTY:
                        operate = BaseOperate.Em((Operate) parameter);
                        break;
                    default:
                        operate = (Operate) parameter;
                        break;
                }
                operateQueue.add(operate);
                continue;
            }

            // NeoQueue
            if (parameter instanceof Collection) {
                operateQueue.addAll((Collection) parameter);
            }
        }

        return operateQueue;
    }
}
