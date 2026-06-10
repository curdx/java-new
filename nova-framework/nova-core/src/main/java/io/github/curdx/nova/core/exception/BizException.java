package io.github.curdx.nova.core.exception;

import io.github.curdx.nova.core.api.ErrorCode;

/**
 * 业务异常：由全局异常处理器转换为统一响应，不打印错误堆栈（非系统故障）。
 */
public class BizException extends RuntimeException {

    private final int code;

    public BizException(String message) {
        super(message);
        this.code = ErrorCode.INTERNAL_ERROR.getCode();
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BizException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
