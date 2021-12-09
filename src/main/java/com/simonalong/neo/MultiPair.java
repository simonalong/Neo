package com.simonalong.neo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author shizi
 * @since 2020/3/30 7:35 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class MultiPair<L,M,R> implements Serializable {

    private L leftValue;
    private M middleValue;
    private R rightValue;
}
