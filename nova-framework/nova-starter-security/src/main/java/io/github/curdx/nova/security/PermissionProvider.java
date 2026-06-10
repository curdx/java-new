package io.github.curdx.nova.security;

import java.util.List;

/**
 * 角色/权限数据源契约：由业务模块（如 system 模块）实现，向 Sa-Token 提供鉴权数据。
 */
public interface PermissionProvider {

    List<String> getRoles(Object loginId);

    List<String> getPermissions(Object loginId);
}
