package io.github.curdx.nova.orm;

import com.mybatisflex.core.tenant.TenantFactory;
import io.github.curdx.nova.core.context.TenantContextHolder;

/**
 * 多租户取值：来自 {@link TenantContextHolder}（由 Web 过滤器/登录态写入）。
 * 返回空数组时 MyBatis-Flex 跳过租户条件 —— 即「平台级」访问。
 */
public class NovaTenantFactory implements TenantFactory {

    private static final Object[] EMPTY = new Object[0];

    @Override
    public Object[] getTenantIds() {
        Long tenantId = TenantContextHolder.get();
        return tenantId == null ? EMPTY : new Object[]{tenantId};
    }
}
