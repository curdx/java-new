package io.github.curdx.nova.module.system.web;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import io.github.curdx.nova.core.api.ErrorCode;
import io.github.curdx.nova.core.api.R;
import io.github.curdx.nova.core.exception.BizException;
import io.github.curdx.nova.module.system.dto.LoginRequest;
import io.github.curdx.nova.module.system.dto.UserVO;
import io.github.curdx.nova.module.system.entity.SysUser;
import io.github.curdx.nova.module.system.service.SysUserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final SysUserService sysUserService;

    public AuthController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @PostMapping("/login")
    public R<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        SysUser user = sysUserService.findByUsername(request.username());
        if (user == null || !BCrypt.checkpw(request.password(), user.getPassword())) {
            throw new BizException(ErrorCode.UNAUTHORIZED, "用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BizException(ErrorCode.FORBIDDEN, "账号已停用");
        }
        StpUtil.login(user.getId());
        return R.ok(Map.of(
                "tokenName", StpUtil.getTokenName(),
                "tokenValue", StpUtil.getTokenValue()
        ));
    }

    @PostMapping("/logout")
    public R<Void> logout() {
        StpUtil.logout();
        return R.ok();
    }

    @GetMapping("/me")
    public R<UserVO> me() {
        SysUser user = sysUserService.getById(StpUtil.getLoginIdAsLong());
        return R.ok(UserVO.from(user));
    }
}
