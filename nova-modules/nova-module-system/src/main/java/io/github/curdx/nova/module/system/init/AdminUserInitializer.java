package io.github.curdx.nova.module.system.init;

import cn.dev33.satoken.secure.BCrypt;
import io.github.curdx.nova.module.system.entity.SysRole;
import io.github.curdx.nova.module.system.entity.SysUser;
import io.github.curdx.nova.module.system.mapper.SysRoleMapper;
import io.github.curdx.nova.module.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 首次启动初始化管理员（admin / admin123）。
 * 密码哈希运行时生成，避免在 SQL 脚本里硬编码哈希值。
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "nova.system", name = "init-admin", havingValue = "true", matchIfMissing = true)
public class AdminUserInitializer implements ApplicationRunner {

    private final SysUserService sysUserService;
    private final SysRoleMapper sysRoleMapper;

    public AdminUserInitializer(SysUserService sysUserService, SysRoleMapper sysRoleMapper) {
        this.sysUserService = sysUserService;
        this.sysRoleMapper = sysRoleMapper;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (sysUserService.findByUsername("admin") != null) {
            return;
        }
        SysRole role = new SysRole();
        role.setRoleCode("admin");
        role.setRoleName("超级管理员");
        sysRoleMapper.insert(role);

        SysUser admin = new SysUser();
        admin.setUsername("admin");
        admin.setPassword(BCrypt.hashpw("admin123", BCrypt.gensalt()));
        admin.setNickname("管理员");
        admin.setStatus(1);
        sysUserService.save(admin);

        sysRoleMapper.insertUserRole(admin.getId(), role.getId());
        log.info("已初始化管理员账号 admin / admin123，生产环境请立即修改密码");
    }
}
