package io.github.curdx.nova.orm;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体审计基类：创建/更新时间与操作人由 {@link AuditEntityListener} 自动填充。
 */
@Data
public abstract class BaseEntity implements Serializable {

    private Long createBy;

    private LocalDateTime createTime;

    private Long updateBy;

    private LocalDateTime updateTime;
}
