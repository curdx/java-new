package io.github.curdx.nova.core.context;

/**
 * 当前登录用户提供者契约：由 security 模块实现（Sa-Token），orm 模块消费（审计字段填充）。
 * core 只定义契约，保持框架各 starter 之间单向依赖。
 */
@FunctionalInterface
public interface CurrentUserProvider {

    /**
     * @return 当前登录用户 ID；未登录返回 {@code null}
     */
    Long currentUserId();
}
