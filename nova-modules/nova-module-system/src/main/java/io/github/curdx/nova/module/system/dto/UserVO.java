package io.github.curdx.nova.module.system.dto;

import io.github.curdx.nova.module.system.entity.SysUser;

/**
 * 用户对外视图：永不携带密码哈希。
 */
public record UserVO(Long id, String username, String nickname, String mobile, Integer status) {

    public static UserVO from(SysUser user) {
        return new UserVO(user.getId(), user.getUsername(), user.getNickname(),
                user.getMobile(), user.getStatus());
    }
}
