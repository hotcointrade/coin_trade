package cn.stylefeng.guns.modular.system.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.roses.core.datascope.DataScope;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 修改用户状态
     */
    int setStatus(@Param("userId") Long userId, @Param("status") String status);

    /**
     * 修改密码
     */
    int changePwd(@Param("userId") Long userId, @Param("pwd") String pwd);

    /**
     * 根据条件查询用户列表
     */
    @SuppressWarnings("rawtypes")
	Page<Map<String, Object>> selectUsers(@Param("page") Page page,
                                          @Param("dataScope") DataScope dataScope,
                                          @Param("name") String name, @Param("beginTime") String beginTime,
                                          @Param("endTime") String endTime, @Param("deptId") Long deptId
            , @Param("memberId") Long memberId, @Param("recommendIds") String recommendIds);

    /**
     * 设置用户的角色
     */
    int setRoles(@Param("userId") Long userId, @Param("roleIds") String roleIds);

    /**
     * 通过账号获取用户
     */
    User getByAccount(@Param("account") String account);

}
