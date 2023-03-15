package cn.wr1sw.lottery.admin.system;


import cn.dev33.satoken.stp.StpUtil;
import cn.wr1sw.lottery.common.base.BaseController;
import cn.wr1sw.lottery.system.entity.SysUser;
import cn.wr1sw.lottery.system.service.ISysUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统模块基础控制器
 *
 * @author 拾年之璐
 * @since 2021-09-30 0030 23:45
 */
@Controller
@AllArgsConstructor
public class SysController extends BaseController {

    ISysUserService userService;

    /**
     * 开发模式下：默认访问域名，跳转至后台主页
     * 如果后期有新的首页，删除即可
     *
     * @return 页面跳转
     */
    @RequestMapping("")
    public String defaultPage() {
        return "redirect:/system/login";
    }

    /**
     * @return 【后台主页】访问入口
     */
    @RequestMapping("system/index")
    public String index(ModelMap map) {
        // 已登录，访问后台主页
        if (StpUtil.isLogin()) {
            SysUser user = userService.getById(StpUtil.getLoginIdAsInt());
            map.put("user", user);
            return "system/index";
        }
        // 未登录，跳转至登录页面
        return "redirect:/system/login";
    }

    /**
     * @return 【登录】页面入口
     */
    @RequestMapping("system/login")
    public String login() {
        // 已登录：跳转至后台首页
        if (StpUtil.isLogin()) {
            return "redirect:/system/index";
        }
        // 未登录，跳转至登录页面
        return "system/login";
    }

    /**
     * @return 后台欢迎页面访问入口
     */
    @RequestMapping("system/welcome")
    public String welcome() {
        return "system/welcome";
    }


}
