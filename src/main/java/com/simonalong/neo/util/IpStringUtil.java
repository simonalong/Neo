//package com.simonalong.neo.util;
//
//import lombok.experimental.UtilityClass;
//
///**
// * @author shizi
// * @since 2020/3/21 上午11:52
// */
//@UtilityClass
//public class IpStringUtil {
//
//    public long str2Long(String strIp) {
//        if (null == strIp || "".equals(strIp)) {
//            return 0;
//        }
//        int[] ip = new int[4];
//        int position1 = strIp.indexOf(".");
//        int position2 = strIp.indexOf(".", position1 + 1);
//        int position3 = strIp.indexOf(".", position2 + 1);
//        ip[0] = Integer.parseInt(strIp.substring(0, position1));
//        ip[1] = Integer.parseInt(strIp.substring(position1 + 1, position2));
//        ip[2] = Integer.parseInt(strIp.substring(position2 + 1, position3));
//        ip[3] = Integer.parseInt(strIp.substring(position3 + 1));
//        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
//    }
//
//    public String long2String(long longIp) {
//        if (0 == longIp) {
//            return "";
//        }
//        StringBuilder sb = new StringBuilder().append(String.valueOf((longIp >>> 24))).append(".");
//        sb.append(String.valueOf((longIp & 0xFFFFFF) >>> 16)).append(".");
//        sb.append(String.valueOf((longIp & 0xFFFF) >>> 8)).append(".");
//        sb.append(String.valueOf((longIp & 0xFF)));
//        return sb.toString();
//    }
//}
