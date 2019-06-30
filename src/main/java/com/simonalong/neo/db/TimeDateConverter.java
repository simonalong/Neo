package com.simonalong.neo.db;

import java.util.Calendar;
import java.util.Date;
import lombok.experimental.UtilityClass;

/**
 * 时间日期转换器
 *
 * @author zhouzhenyong
 * @since 2019/5/16 下午7:05
 */
@UtilityClass
public class TimeDateConverter {

    private final String YEAR = "YEAR";

    /**
     * 数据库中的时间对象转到long
     * 也就是数据库类型：{@link java.sql.Date} {@link java.sql.Time} {@link java.sql.Timestamp}
     * @param time 数据库中的数据对象
     * @return 时间对应的long类型
     */
    public Object dbTimeToLong(Object time){
        if (time instanceof java.sql.Date){
            return java.sql.Date.class.cast(time).getTime();
        } else if(time instanceof java.sql.Time){
            return java.sql.Time.class.cast(time).getTime();
        } else if(time instanceof java.sql.Timestamp){
            return java.sql.Timestamp.class.cast(time).getTime();
        }
        return time;
    }

    /**
     * NeoMap中的long对象转换到db中的时间类型，用于数据插入
     *
     * mysql中有5中时间类型，但是该函数只支持其中四种：Date, Time, DateTime, Timestamp，其中不支持year，year的插入需要通过函数{@link TimeDateConverter#longToYearTime}转换
     * @param tClass 数据对应的类型
     * @param columnTypeName 列在数据库中的类型
     * @param data long类型的时间类型
     * @return 对于时间类型的字段，这里返回的是java.util.Date类型，否则就是原类型
     */
    public Object longToDbTime(Class<?> tClass, String columnTypeName, Object data){
        if(!(data instanceof Long)){
            return data;
        }
        Long dataLong = Long.class.cast(data);
        if (java.sql.Date.class.isAssignableFrom(tClass)){
            if (columnTypeName.equals(YEAR)) {
                return longToYearTime(dataLong);
            }else{
                return new java.util.Date(dataLong);
            }
        } else if (java.sql.Date.class.isAssignableFrom(tClass)
            || java.sql.Time.class.isAssignableFrom(tClass)
            || java.sql.Timestamp.class.isAssignableFrom(tClass)) {
            return new java.util.Date(dataLong);
        }
        return data;
    }

    /**
     * 这里针对mysql的时间字段year
     * @param time Long类型的时间字段
     * @return 转换成年之后的字符串
     */
    private String longToYearTime(Long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        return String.valueOf(calendar.getWeekYear());
    }

    /**
     * 根据实体类型转时间类型，对于time为Long的，且待转换的为时间类型的，则转换为时间类型
     * {@link java.sql.Date} {@link java.sql.Time} {@link java.sql.Timestamp} {@link java.util.Date}
     * todo 后续将java8的时间类型转换也放进去
     * @param tClass 实体属性对应的类型
     * @param time 时间
     * @return 类型对应的值
     */
    public Object valueToEntityTime(Class<?> tClass, Object time){
        if (time instanceof Long){
            return longToEntityTime(tClass, Long.class.cast(time));
        }
        return time;
    }

    /**
     * 根据实体将Long类型的时间转为对应的时间类型
     * {@link java.sql.Date} {@link java.sql.Time} {@link java.sql.Timestamp} {@link java.util.Date}
     * todo 后续将java8的时间类型转换也放进去
     * @param tClass 实体属性对应的类型
     * @param time 时间
     * @param <T> 时间类型
     * @return 类型对应的值
     */
    @SuppressWarnings("unchecked")
    public <T> T longToEntityTime(Class<T> tClass, Long time) {
        if (java.sql.Date.class.isAssignableFrom(tClass)) {
            return tClass.cast(new java.sql.Date(time));
        } else if(java.sql.Time.class.isAssignableFrom(tClass)) {
            return tClass.cast(new java.sql.Time(time));
        } else if(java.sql.Timestamp.class.isAssignableFrom(tClass)) {
            return tClass.cast(new java.sql.Timestamp(time));
        } else if(java.util.Date.class.isAssignableFrom(tClass)) {
            return tClass.cast(new java.util.Date(time));
        }
        return (T) time;
    }

    /**
     * 实体中的时间类型转为Long
     *
     * 实体中的时间类型有这么几种：{@link java.sql.Date} {@link java.sql.Time} {@link java.sql.Timestamp} {@link java.util.Date}
     *
     * 对于其他的新的时间类型比如java8中的{@link java.time.LocalDateTime} 等这里暂时先不做处理，留待后续处理
     * todo 后续将java8的时间类型转换也放进去
     * @param fieldTime 实体中属性的值
     * @return long类型的时间
     */
    public Object entityTimeToLong(Object fieldTime){
        if (fieldTime instanceof java.sql.Date){
           return java.sql.Date.class.cast(fieldTime).getTime();
        } else if(fieldTime instanceof java.sql.Time){
            return java.sql.Time.class.cast(fieldTime).getTime();
        } else if(fieldTime instanceof java.sql.Timestamp){
            return java.sql.Timestamp.class.cast(fieldTime).getTime();
        } else if(fieldTime instanceof java.util.Date){
            return java.util.Date.class.cast(fieldTime).getTime();
        }
        return fieldTime;
    }

    /**
     * 判断值是否是时间类型
     *
     * @param tClass 待校验的类型
     * @return true:是时间类型，false：不是时间类型
     */
    public Boolean isTimeType(Class<?> tClass) {
        return java.sql.Date.class.isAssignableFrom(tClass)
            || java.sql.Time.class.isAssignableFrom(tClass)
            || java.sql.Timestamp.class.isAssignableFrom(tClass)
            || Date.class.isAssignableFrom(tClass);
    }
}
