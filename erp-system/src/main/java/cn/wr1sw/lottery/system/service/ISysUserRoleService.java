package cn.wr1sw.lottery.system.service;

import cn.wr1sw.lottery.system.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;

/**
 * <p>
 * 系统用户和角色关联表 服务类
 * </p>
 *
 * @author 拾年之璐
 * @since 2021-09-23
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 通过角色ID获取用户角色关联列表
     *
     * @param roleId 角色ID
     * @return 用户角色关联列表
     */
    public List<SysUserRole> getListByRoleId(Integer roleId);

    /**
     * 通过用户ID获取用户角色关联列表
     *
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    public List<SysUserRole> getListByUserId(Integer userId);

}
