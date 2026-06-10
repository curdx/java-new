package io.github.curdx.nova.security;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import io.github.curdx.nova.core.api.ErrorCode;
import io.github.curdx.nova.core.api.R;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 鉴权异常优先于全局兜底处理（Order 更小）。
 */
@Order(0)
@RestControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(NotLoginException.class)
    public R<Void> handleNotLogin(NotLoginException e) {
        return R.fail(ErrorCode.UNAUTHORIZED);
    }

    @ExceptionHandler({NotPermissionException.class, NotRoleException.class})
    public R<Void> handleNotPermission(Exception e) {
        return R.fail(ErrorCode.FORBIDDEN);
    }
}
