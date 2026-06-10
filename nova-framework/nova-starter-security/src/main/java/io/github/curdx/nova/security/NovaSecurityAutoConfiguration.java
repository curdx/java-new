package io.github.curdx.nova.security;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import io.github.curdx.nova.core.context.CurrentUserProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@AutoConfiguration
@EnableConfigurationProperties(NovaSecurityProperties.class)
public class NovaSecurityAutoConfiguration {

    /**
     * 全局登录校验：默认全部拦截，白名单放行。细粒度权限用 @SaCheckPermission/@SaCheckRole 注解。
     */
    @Bean
    public WebMvcConfigurer novaSaTokenWebMvcConfigurer(NovaSecurityProperties properties) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                        .addPathPatterns("/**")
                        .excludePathPatterns(properties.getIgnoreUrls());
            }
        };
    }

    /**
     * Sa-Token 鉴权数据源，桥接业务模块的 PermissionProvider 实现。
     */
    @Bean
    public StpInterface novaStpInterface(ObjectProvider<PermissionProvider> permissionProvider) {
        return new StpInterface() {
            @Override
            public List<String> getPermissionList(Object loginId, String loginType) {
                PermissionProvider provider = permissionProvider.getIfAvailable();
                return provider == null ? List.of() : provider.getPermissions(loginId);
            }

            @Override
            public List<String> getRoleList(Object loginId, String loginType) {
                PermissionProvider provider = permissionProvider.getIfAvailable();
                return provider == null ? List.of() : provider.getRoles(loginId);
            }
        };
    }

    /**
     * 为 ORM 审计提供当前登录用户。
     */
    @Bean
    public CurrentUserProvider novaCurrentUserProvider() {
        return () -> StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
    }

    @Bean
    public SecurityExceptionHandler novaSecurityExceptionHandler() {
        return new SecurityExceptionHandler();
    }
}
