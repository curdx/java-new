package io.github.curdx.nova.orm;

import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;
import io.github.curdx.nova.core.context.CurrentUserProvider;

import java.time.LocalDateTime;

/**
 * 审计字段自动填充：insert 填 create/update 四件套，update 只刷新 update 两件套。
 * 操作人来自 {@link CurrentUserProvider}（由 security 模块提供，未登录/无实现时留空）。
 */
public class AuditEntityListener implements InsertListener, UpdateListener {

    private final CurrentUserProvider currentUserProvider;

    public AuditEntityListener(CurrentUserProvider currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public void onInsert(Object entity) {
        if (entity instanceof BaseEntity base) {
            LocalDateTime now = LocalDateTime.now();
            Long userId = currentUserId();
            if (base.getCreateTime() == null) {
                base.setCreateTime(now);
            }
            if (base.getCreateBy() == null) {
                base.setCreateBy(userId);
            }
            base.setUpdateTime(now);
            base.setUpdateBy(userId);
        }
    }

    @Override
    public void onUpdate(Object entity) {
        if (entity instanceof BaseEntity base) {
            base.setUpdateTime(LocalDateTime.now());
            base.setUpdateBy(currentUserId());
        }
    }

    private Long currentUserId() {
        if (currentUserProvider == null) {
            return null;
        }
        try {
            return currentUserProvider.currentUserId();
        } catch (Exception ignore) {
            // 非 Web 线程（任务调度等）取不到登录态属正常情况
            return null;
        }
    }
}
