package io.github.curdx.nova.module.system.mapper;

import com.mybatisflex.core.BaseMapper;
import io.github.curdx.nova.module.system.entity.SysRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("""
            select r.role_code from sys_role r
            join sys_user_role ur on ur.role_id = r.id
            where ur.user_id = #{userId} and r.deleted = 0
            """)
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    @Insert("insert into sys_user_role (user_id, role_id) values (#{userId}, #{roleId})")
    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
