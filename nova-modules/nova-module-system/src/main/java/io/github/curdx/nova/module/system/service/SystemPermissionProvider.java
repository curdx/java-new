package io.github.curdx.nova.module.system.service;

import io.github.curdx.nova.module.system.mapper.SysRoleMapper;
import io.github.curdx.nova.security.PermissionProvider;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 向 Sa-Token 提供角色/权限。权限码（菜单按钮级）随菜单模块落地后接入。
 */
@Component
public class SystemPermissionProvider implements PermissionProvider {

    private final SysRoleMapper sysRoleMapper;

    public SystemPermissionProvider(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
    }

    @Override
    public List<String> getRoles(Object loginId) {
        return sysRoleMapper.selectRoleCodesByUserId(Long.valueOf(loginId.toString()));
    }

    @Override
    public List<String> getPermissions(Object loginId) {
        return List.of();
    }
}
