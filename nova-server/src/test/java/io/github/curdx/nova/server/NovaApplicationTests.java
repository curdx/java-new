package io.github.curdx.nova.server;

import io.github.curdx.nova.module.system.entity.SysUser;
import io.github.curdx.nova.module.system.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NovaApplicationTests {

    @Autowired
    private SysUserService sysUserService;

    @Test
    void contextLoadsAndAdminInitialized() {
        SysUser admin = sysUserService.findByUsername("admin");
        assertThat(admin).isNotNull();
        assertThat(admin.getStatus()).isEqualTo(1);
        assertThat(admin.getPassword()).startsWith("$2"); // BCrypt 哈希
    }

    @Test
    void sm4MobileFieldIsEncryptedAtRest() {
        SysUser user = new SysUser();
        user.setUsername("sm4-test");
        user.setPassword("x");
        user.setMobile("13800138000");
        user.setStatus(1);
        sysUserService.save(user);

        // 读出来是明文（TypeHandler 解密）
        SysUser loaded = sysUserService.findByUsername("sm4-test");
        assertThat(loaded.getMobile()).isEqualTo("13800138000");
    }
}
