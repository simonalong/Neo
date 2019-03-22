package com.simon.neo.sql;

import com.simon.neo.Neo;
import com.simon.neo.NeoMap;
import java.math.BigInteger;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * 该类主要用于对对应的sql进行解析，用于分析使用
 * @author zhouzhenyong
 * @since 2019/3/22 下午8:26
 */
@Slf4j
public class SqlExplain {

    private static final String PRE_LOG = "[Neo-explain]";

    private static SqlExplain instance = new SqlExplain();
    private SqlExplain(){}

    public static SqlExplain getInstance(){
        return instance;
    }

    /**
     * 专门用于对sql进行explain解析
     * @param sql 待解析的sql
     */
    public void explain(Neo neo, String sql, List<Object> parameters){
        neo.setMonitorFlag(false);
        neo.setStandardFlag(false);
        List<List<NeoMap>> explainList = neo.execute("explain " + sql, parameters.toArray());
        if(null != explainList && !explainList.isEmpty()){
            List<NeoMap> mExplain = explainList.get(0);
            if(null != mExplain && !mExplain.isEmpty()){
                Explain explain = Explain.parse(mExplain.get(0));
                if(null != explain && explain.getType().equals("ALL")){
                    log.warn(PRE_LOG + " [sql走了全表扫描] (sql => " + sql + ")");
                }
            }
        }

        neo.setMonitorFlag(true);
        neo.setStandardFlag(true);
    }

    /**
     * sql执行评测类
     */
    @Setter
    @Getter
    @Accessors(chain = true)
    static class Explain{
        private Integer id;
        private String selectType;
        private String table;
        private String partitions;
        private String type;
        private String possibleKeys;
        private String key;
        private String keyLen;
        private String ref;
        private BigInteger rows;
        private Double filtered;
        private String extra;

        private Explain(){}

        static Explain parse(NeoMap neoMap) {
            if (NeoMap.isEmpty(neoMap)) {
                return null;
            }
            return new Explain()
                .setId((Integer)(neoMap.get("neoMap")))
                .setSelectType((String) neoMap.get("select_type"))
                .setTable((String) neoMap.get("table"))
                .setPartitions((String) neoMap.get("partitions"))
                .setType((String) neoMap.get("type"))
                .setPossibleKeys((String) neoMap.get("possible_keys"))
                .setKey((String) neoMap.get("key"))
                .setKeyLen((String) neoMap.get("key_len"))
                .setRef((String) neoMap.get("ref"))
                .setRows((BigInteger) neoMap.get("rows"))
                .setFiltered((Double) neoMap.get("filtered"))
                .setExtra((String) neoMap.get("extra"));
        }
    }
}
