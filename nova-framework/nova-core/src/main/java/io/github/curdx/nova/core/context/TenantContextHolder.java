package io.github.curdx.nova.core.context;

/**
 * 租户上下文。
 *
 * <p>当前基线为 JDK 21，{@link ThreadLocal} 实现；ScopedValue（JEP 506）在 JDK 25 才转正，
 * 框架基线升至 25 后此处整体切换为 ScopedValue（调用方接口不变）。</p>
 *
 * <p>租户为空表示「平台级/不限租户」操作，ORM 层将跳过租户条件。</p>
 */
public final class TenantContextHolder {

    private static final ThreadLocal<Long> TENANT = new ThreadLocal<>();

    private TenantContextHolder() {
    }

    public static void set(Long tenantId) {
        TENANT.set(tenantId);
    }

    public static Long get() {
        return TENANT.get();
    }

    public static void clear() {
        TENANT.remove();
    }
}
