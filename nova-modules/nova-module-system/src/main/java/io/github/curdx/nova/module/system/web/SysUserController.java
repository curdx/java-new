package io.github.curdx.nova.module.system.web;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.secure.BCrypt;
import io.github.curdx.nova.core.api.ErrorCode;
import io.github.curdx.nova.core.api.PageResult;
import io.github.curdx.nova.core.api.R;
import io.github.curdx.nova.core.exception.BizException;
import io.github.curdx.nova.module.system.dto.UserVO;
import io.github.curdx.nova.module.system.entity.SysUser;
import io.github.curdx.nova.module.system.service.SysUserService;
import io.github.curdx.nova.orm.PageUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/users")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping
    public R<PageResult<UserVO>> page(@RequestParam(defaultValue = "1") long pageNumber,
                                      @RequestParam(defaultValue = "10") long pageSize,
                                      @RequestParam(required = false) String keyword) {
        return R.ok(PageUtils.toResult(sysUserService.pageUsers(pageNumber, pageSize, keyword), UserVO::from));
    }

    public record UserUpsertRequest(
            @NotBlank(message = "用户名不能为空") String username,
            String password,
            String nickname,
            String mobile) {
    }

    @SaCheckRole("admin")
    @PostMapping
    public R<UserVO> create(@Valid @RequestBody UserUpsertRequest request) {
        if (sysUserService.findByUsername(request.username()) != null) {
            throw new BizException(ErrorCode.CONFLICT, "用户名已存在");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new BizException(ErrorCode.BAD_REQUEST, "密码不能为空");
        }
        SysUser user = new SysUser();
        user.setUsername(request.username());
        user.setPassword(BCrypt.hashpw(request.password(), BCrypt.gensalt()));
        user.setNickname(request.nickname());
        user.setMobile(request.mobile());
        user.setStatus(1);
        sysUserService.save(user);
        return R.ok(UserVO.from(user));
    }

    @SaCheckRole("admin")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        sysUserService.removeById(id);
        return R.ok();
    }
}
