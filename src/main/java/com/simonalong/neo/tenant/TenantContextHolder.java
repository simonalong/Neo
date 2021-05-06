package com.simonalong.neo.tenant;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * @author shizi
 * @since 2021-05-06 14:09:40
 */
@UtilityClass
public class TenantContextHolder {

    private final TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();

    public void setTenantId(String tenantId) {
        context.set(tenantId);
    }

    public String getTenantId() {
        return context.get();
    }
}
