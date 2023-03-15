package cn.wr1sw.lottery.framework.satoken;

import cn.dev33.satoken.stp.StpInterface;
import cn.wr1sw.lottery.system.entity.SysMenu;
import cn.wr1sw.lottery.system.service.ISysMenuService;
import cn.wr1sw.lottery.system.service.ISysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限认证接口
 * </p>
 *
 * @author 拾年之璐
 * @since 2021-10-12 0012 21:02
 */
@Component
@AllArgsConstructor
public class SaTokenStpInterfaceImpl implements StpInterface {

    private ISysMenuService menuService;

    private ISysRoleService roleService;

    @Override
    public List<String> getPermissionList(Object userId, String userType) {
        List<SysMenu> menuList = menuService.getMenuListByUserIdForSaToken(Integer.valueOf(userId.toString()));
        if (null == menuList) {
            return null;
        }
        // 筛选，取出非空权限（href != null）
        List<String> permissions = new ArrayList<>();
        for (SysMenu menu : menuList) {
            if (null != menu.getPermission() && !"".equals(menu.getPermission())) {
                permissions.add(menu.getPermission());
            }
        }
        return permissions;
    }

    @Override
    public List<String> getRoleList(Object userId, String userType) {
        return null;
    }
}
