package com.simonalong.neo.devide.strategy;

import com.simonalong.neo.Neo;
import com.simonalong.neo.devide.TableDevideConfig;
import com.simonalong.neo.exception.NeoException;

import java.util.List;
import java.util.Map;

import static com.simonalong.neo.uid.UuidConstant.SEQ_BITS;
import static com.simonalong.neo.uid.UuidConstant.SEQ_LEFT_SHIFT;

/**
 * 针对uuid生成器生成的id进行的哈希
 * <p>
 * <p>
 * 由于uuid生成器{@link com.simonalong.neo.uid.UuidGenerator}生成的id的低13bit是workId（机器id），也就是同一台机器生成的低13bit都是相同的，因此无法完全哈希均分化，因此采用新的哈希方式
 *
 * @author shizi
 * @since 2020/6/10 5:59 PM
 */
public class UuidHashDevideStrategy implements DevideStrategy {

    private Integer dbSize;
    private Integer markNum;
    /**
     * key：表名，value表的分表配置
     */
    private Map<String, TableDevideConfig> tableDevideConfigMap;

    public UuidHashDevideStrategy(Integer dbSize, Map<String, TableDevideConfig> tableDevideConfigMap) {
        this.dbSize = dbSize;
        if ((dbSize & (dbSize - 1)) == 0) {
            this.markNum = dbSize - 1;
        }
        this.tableDevideConfigMap = tableDevideConfigMap;
    }

    @Override
    public Neo getDb(List<Neo> neoList, Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Number) {
            Long seqNum = getSeqNum(Number.class.cast(value).intValue());
            if (null != neoList) {
                if (null != markNum) {
                    Integer index = seqNum.intValue() & markNum;
                    return neoList.get(index);
                } else {
                    Integer index = seqNum.intValue() % dbSize;
                    return neoList.get(index);
                }
            }
        }
        return null;
    }

    @Override
    public String getTable(String logicTableName, Object value) {
        if (null == value) {
            return logicTableName;
        }
        if (value instanceof Number) {
            Long seqNum = getSeqNum(Number.class.cast(value).intValue());
            if (tableDevideConfigMap.containsKey(logicTableName)) {
                TableDevideConfig tableDevideConfig = tableDevideConfigMap.get(logicTableName);
                return logicTableName + (tableDevideConfig.getMin() + (seqNum % tableDevideConfig.getSize()));
            }
        }
        return null;
    }

    private Long getSeqNum(Number number) {
        // 获取其中的序列的值
        Long seqMark = (~(-1L << SEQ_BITS)) << SEQ_LEFT_SHIFT;
        return (number.longValue() & seqMark) >> SEQ_LEFT_SHIFT;
    }
}
