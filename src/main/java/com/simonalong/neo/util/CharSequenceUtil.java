package com.simonalong.neo.util;

import lombok.experimental.UtilityClass;

/**
 * @author shizi
 * @since 2020/8/31 10:44 下午
 */
@UtilityClass
public class CharSequenceUtil {

    public Boolean isEmpty(CharSequence charSequence) {
        return null == charSequence || "".contentEquals(charSequence) || "null".contentEquals(charSequence);
    }

    public Boolean isNotEmpty(CharSequence charSequence) {
        return !isEmpty(charSequence);
    }
}
