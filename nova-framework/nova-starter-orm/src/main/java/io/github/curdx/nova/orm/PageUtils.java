package io.github.curdx.nova.orm;

import com.mybatisflex.core.paginate.Page;
import io.github.curdx.nova.core.api.PageResult;

import java.util.List;
import java.util.function.Function;

/**
 * MyBatis-Flex Page 与框架 PageResult 之间的转换。
 */
public final class PageUtils {

    private PageUtils() {
    }

    public static <T> PageResult<T> toResult(Page<T> page) {
        return PageResult.of(page.getTotalRow(), page.getPageNumber(), page.getPageSize(), page.getRecords());
    }

    public static <T, V> PageResult<V> toResult(Page<T> page, Function<T, V> mapper) {
        List<V> records = page.getRecords().stream().map(mapper).toList();
        return PageResult.of(page.getTotalRow(), page.getPageNumber(), page.getPageSize(), records);
    }
}
