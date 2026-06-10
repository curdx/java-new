package io.github.curdx.nova.core.api;

import java.util.List;

/**
 * 分页结果，与具体 ORM 解耦（由 orm 模块负责从 MyBatis-Flex Page 转换）。
 */
public record PageResult<T>(long total, long pageNumber, long pageSize, List<T> records) {

    public static <T> PageResult<T> of(long total, long pageNumber, long pageSize, List<T> records) {
        return new PageResult<>(total, pageNumber, pageSize, records);
    }
}
