package io.github.curdx.nova.module.system.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import io.github.curdx.nova.module.system.entity.SysUser;
import io.github.curdx.nova.module.system.mapper.SysUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static io.github.curdx.nova.module.system.entity.table.SysUserTableDef.SYS_USER;

/**
 * 用户服务。查询全部走 MyBatis-Flex APT 生成的强类型 TableDef（编译期校验字段）。
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    public SysUser findByUsername(String username) {
        return getOne(QueryWrapper.create().where(SYS_USER.USERNAME.eq(username)));
    }

    public Page<SysUser> pageUsers(long pageNumber, long pageSize, String keyword) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_USER.USERNAME.like(keyword, StringUtils.hasText(keyword))
                        .or(SYS_USER.NICKNAME.like(keyword, StringUtils.hasText(keyword))))
                .orderBy(SYS_USER.ID.desc());
        return page(Page.of(pageNumber, pageSize), query);
    }
}
