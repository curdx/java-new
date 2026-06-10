package io.github.curdx.nova.web;

import io.github.curdx.nova.core.api.ErrorCode;
import io.github.curdx.nova.core.api.R;
import io.github.curdx.nova.core.exception.BizException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常兜底。业务异常不打堆栈，系统异常打 error 日志并隐藏内部细节。
 */
@Slf4j
@Order(100)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public R<Void> handleBizException(BizException e) {
        log.warn("业务异常: code={}, msg={}", e.getCode(), e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValidation(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String msg = fieldError == null
                ? ErrorCode.BAD_REQUEST.getMessage()
                : fieldError.getField() + " " + fieldError.getDefaultMessage();
        return R.fail(ErrorCode.BAD_REQUEST, msg);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public R<Void> handleConstraintViolation(ConstraintViolationException e) {
        return R.fail(ErrorCode.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return R.fail(ErrorCode.INTERNAL_ERROR);
    }
}
