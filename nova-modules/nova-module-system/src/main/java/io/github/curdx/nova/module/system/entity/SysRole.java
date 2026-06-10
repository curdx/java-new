package io.github.curdx.nova.module.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.github.curdx.nova.orm.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_role")
public class SysRole extends BaseEntity {

    @Id(keyType = KeyType.Auto)
    private Long id;

    @Column(tenantId = true)
    private Long tenantId;

    private String roleCode;

    private String roleName;

    @Column(isLogicDelete = true)
    private Integer deleted;
}
