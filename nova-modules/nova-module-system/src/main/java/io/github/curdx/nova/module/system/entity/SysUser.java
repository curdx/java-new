package io.github.curdx.nova.module.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.github.curdx.nova.crypto.Sm4FieldTypeHandler;
import io.github.curdx.nova.orm.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统用户。演示三个框架约定：
 * 多租户（tenantId）、逻辑删除（deleted）、国密字段加密（mobile）。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_user")
public class SysUser extends BaseEntity {

    @Id(keyType = KeyType.Auto)
    private Long id;

    @Column(tenantId = true)
    private Long tenantId;

    private String username;

    /**
     * BCrypt 哈希，任何接口不得返回该字段。
     */
    private String password;

    private String nickname;

    /**
     * 手机号 SM4 加密落库（国密合规演示字段）。
     */
    @Column(typeHandler = Sm4FieldTypeHandler.class)
    private String mobile;

    /**
     * 1 启用 / 0 停用。
     */
    private Integer status;

    /**
     * 逻辑删除：0 正常 / 1 已删除。统一用 int（达梦 Oracle 模式无 boolean，int 全库通用）。
     */
    @Column(isLogicDelete = true)
    private Integer deleted;
}
