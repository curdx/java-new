package io.github.curdx.nova.orm;

import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import io.github.curdx.nova.core.context.CurrentUserProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * ORM 全局约定：审计监听 + 多租户。
 *
 * <p>信创说明：金仓/openGauss 走 PostgreSQL 方言开箱即用；达梦（Oracle 系）方言
 * 由 MyBatis-Flex 按 JDBC URL 自动识别，专项适配见 docs 信创矩阵任务。</p>
 */
@AutoConfiguration
public class NovaOrmAutoConfiguration {

    @Bean
    public MyBatisFlexCustomizer novaFlexCustomizer(ObjectProvider<CurrentUserProvider> currentUserProvider) {
        return globalConfig -> {
            AuditEntityListener auditListener = new AuditEntityListener(currentUserProvider.getIfAvailable());
            globalConfig.registerInsertListener(auditListener, BaseEntity.class);
            globalConfig.registerUpdateListener(auditListener, BaseEntity.class);
            TenantManager.setTenantFactory(new NovaTenantFactory());
        };
    }
}
