package cn.wr1sw.lottery.admin.system;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.wr1sw.lottery.common.base.BaseController;
import cn.wr1sw.lottery.common.base.BaseResult;
import cn.wr1sw.lottery.common.config.LotteryConfig;
import cn.wr1sw.lottery.common.enums.SystemCode;
import cn.wr1sw.lottery.framework.aop.AnnotationSaveLoginLog;
import cn.wr1sw.lottery.framework.aop.AnnotationSaveReLog;
import cn.wr1sw.lottery.system.entity.SysUser;
import cn.wr1sw.lottery.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 系统用户表 前端控制器
 *
 * @author 拾年之璐
 * @since 2021-09-01
 */
@Tag(name = "后台用户控制器")
@Controller
@AllArgsConstructor
@RequestMapping("/system/user")
public class SysUserController extends BaseController {

    private ISysUserService userService;

//    private RedisService redisUtils;

    private LotteryConfig exciteConfig;

    /**
     * 访问页面：用户管理 - 列表页面
     *
     * @return 跳转至列表页面
     */
    @RequestMapping("index")
    public String index() {
        return "system/user/index";
    }

    /**
     * 访问页面：用户管理 - 添加用户页面
     *
     * @return 跳转至添加用户页面
     */
    @RequestMapping("goAdd")
    public String goAdd() {
        return "system/user/add";
    }

    /**
     * 访问页面：用户管理 - 编辑用户信息页面
     *
     * @return 跳转至编辑用户页面
     */
    @RequestMapping("goEdit/{id}")
    public String goEdit(@PathVariable String id, ModelMap map) {
        // 获取该用户的信息
        SysUser user = userService.getById(id);
        // 加载至前端
        if (user == null) {
            map.put("code", 404);
            map.put("msg", "用户不存在！");
            return "error/error";
        }
        map.put("user", user);
        return "system/user/edit";
    }

    /**
     * 访问页面：用户管理 - 为用户分配角色页面
     *
     * @return 跳转至编辑用户页面
     */
    @RequestMapping("goAuthRole/{id}")
    public String goAuthRole(@PathVariable String id, ModelMap map) {
        // 获取该用户的信息
        SysUser user = userService.getById(id);
        // 加载至前端
        if (user == null) {
            map.put("code", 404);
            map.put("msg", "用户不存在！");
            return "error/error";
        }
        map.put("user", user);
        return "system/user/authRole";
    }


    /**
     * 接口功能：用户登录
     * 基本逻辑：校验前端信息-->校验验证码-->校验用户名、密码-->登录
     *
     * @param username 用户名
     * @param password 密码
     * @param captcha  验证码
     * @param remember 是否记住登录状态 1-记住 | 0-不记住（记住登录状态后，关闭浏览器，不退出。不记住登录状态，关闭浏览器后即退出登录。只有手动退出才可以）
     * @return JSON：登录结果
     */
    @PostMapping(value = "/login")
    @ResponseBody
    @AnnotationSaveLoginLog
    public BaseResult login(String username, String password, String captcha, String remember) {
        // 如果提交的信息有空信息
        if (StrUtil.hasBlank(username, password, captcha)) {
            return error("用户名、密码或验证码不能为空！");
        }
        // 前后端同域：使用session存储验证码。如果不同域：使用redis
        HttpSession session = request.getSession();
        // 如果验证码session不为空，表示以获取验证码
        if (null != session.getAttribute("captcha")) {
            // 验证码正确
            if (session.getAttribute("captcha").toString().equals(captcha)) {
                // 使用用户名和密码登录
                SysUser user = userService.login(username, SecureUtil.md5(this.decrypt(password)));
                // 如果用户不为空，则登录成功
                if (null != user) {
                    // 账号状态正常
                    if (SystemCode.STATUS_N.getCode() != user.getStatus()) {
                        // 保持登录与否
                        StpUtil.login(user.getId(), "1".equals(remember));
                        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
                        return success("登录成功！", tokenInfo);
                    }
                    // 账号已被封禁
                    else {
                        return error("该账号已被封禁，请联系系统管理员");
                    }
                }
                // 登录失败
                else {
                    return error("登录失败：用户名或密码错误");
                }
            }
            // 验证码错误
            else {
                return error("验证码错误，请重新输入！");
            }
        }
        // 未获取验证码：提示重新获取
        else {
            return error("验证码已失效，请重新获取！");
        }
    }

    /**
     * 接口功能：退出登录
     *
     * @return JSON：退出登录结果
     */
    @PostMapping(value = "/logout")
    @ResponseBody
    public BaseResult logout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
            return success("已退出登录！");
        }
        return error("请求失败，未登录任何账户");
    }

    /**
     * 接口功能：获取用户列表
     *
     * @param page     当前页面
     * @param limit    每页请求数
     * @param username 检索用户名
     * @param account  检索电话或邮箱
     * @return JSON：用户列表 | 错误信息
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public BaseResult getUserList(Integer page, Integer limit, String username, String account) {
        Page<SysUser> userPage = userService.getPage(page, limit, username, account);
        if (null != userPage) {
            return success("查询成功", userPage.getRecords(), (int) userPage.getTotal());
        } else {
            return error("查询失败，用户不存在");
        }
    }

    /**
     * 接口功能：添加用户
     * 基本逻辑：接收前端的用户信息-->校验与初始化新用户信息-->添加至[用户表]-->获取新添加用户的ID-->用户ID与角色ID添加至[用户角色关联表].
     *
     * @param user    用户信息
     * @param roleIds 用户分配的角色信息
     * @return JSON：成败结果
     * @apiNote 启用注解[@AnnotationSaveReLog]保存请求日志
     */
    @PostMapping(value = "/add")
    @ResponseBody
    @AnnotationSaveReLog
    public BaseResult add(SysUser user, Integer[] roleIds) {
        // 新建用户：其邮箱或手机号、密码不能为空，因为这是登录凭证.
        if (null != user && null != user.getPassword() && (null != user.getEmail() || null != user.getPhone())) {
            // 解密
            user.setPassword(SecureUtil.md5(this.decrypt(user.getPassword())));
            // 添加
            if (userService.save(user, roleIds) > 0) {
                return success("新用户添加成功！");
            } else {
                return error("新用户添加失败，请重试！");
            }
        }
        return error("用户信息（邮箱或手机号、密码）不能为空，请重试！");
    }

    /**
     * 接口功能：更新用户基本信息（不含角色信息）
     *
     * @param user 用户信息
     * @return JSON：成败结果
     * @apiNote 启用注解[@AnnotationSaveReLog]保存请求日志
     */
    @PostMapping(value = "/update")
    @ResponseBody
    @AnnotationSaveReLog
    public BaseResult update(SysUser user) {
        // 编辑用户基本信息：用户名、头像、性别、邮箱和手机号。
        if (null != user && null != user.getId()) {
            // 其他信息（状态、密码）不能通过此接口修改，故其应为null.
            if (null == user.getStatus() && null == user.getPassword()) {
                if (userService.save(user, null) > 0) {
                    return success("用户信息编辑成功！");
                } else {
                    return error("用户信息编辑失败，请重试！");
                }
            } else {
                return error("接口请求信息错误！");
            }
        } else {
            return error("用户信息不能为空，请重试！");
        }
    }

    /**
     * 接口功能：删除用户
     * 基本逻辑：删除该用户的角色信息[用户-角色关联表]-->删除用户
     *
     * @param userIds 用户ID列表
     * @return JSON：成败
     * @apiNote 启用注解[@AnnotationSaveReLog]保存请求日志
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    @AnnotationSaveReLog
    public BaseResult delete(Integer[] userIds) {
        try {
            if (userService.deleteUserById(userIds)[0] > 0) {
                return success("用户删除成功");
            }
            return error("用户删除失败");
        } catch (Exception e) {
            return error("发生错误：" + e.getMessage());
        }
    }

    /**
     * 接口功能：给指定用户分配角色
     *
     * @param id      用户ID
     * @param roleIds 角色信息列表
     * @return 分配结果
     */
    @PostMapping(value = "/authRole")
    @ResponseBody
    public BaseResult setRole(String id, Integer[] roleIds) {
        try {
            if (userService.setRoleForUser(Integer.parseInt(id), roleIds)) {
                return success(200, "分配角色成功！");
            }
            return success("分配角色失败，请重试！");
        } catch (Exception e) {
            return error("发生错误：" + e.getMessage());
        }

    }

    /**
     * 接口功能：改变用户的状态
     *
     * @param status  新状态：1-正常 0-禁用
     * @param userIds 用户ID列表
     * @return 修改结果
     */
    @PostMapping(value = "/changeStatus/{status}")
    @ResponseBody
    public BaseResult changeStatus(@PathVariable String status, Integer[] userIds) {
        try {
            int[] result = userService.changeStatus(Integer.parseInt(status), userIds);
            // 没有失败
            if (result[0] != 0 && result[1] == 0) {
                return success(200, "用户状态修改成功！");
            }
            // 有成功的
            else if (result[0] != 0) {
                return success(200, result[0] + "个角色状态更新成功，" + result[1] + "个失败！");
            } else {
                return success(500, "用户状态修改失败，请重试！");
            }
        } catch (Exception e) {
            return error(500, "发生错误:" + e.getMessage());
        }
    }

    /**
     * 接口功能：重置密码
     *
     * @param userId       用户ID
     * @param newPassword  新密码
     * @param newPassword2 新密码
     * @return 重置结果
     */
    @PostMapping(value = "/resetPassword")
    @ResponseBody
    @AnnotationSaveReLog
    public BaseResult resetPassword(String userId, String newPassword, String newPassword2) {
        if (null == userId || null == newPassword || null == newPassword2) {
            return error("输入信息有误，请重新输入！");
        }
        // 密码先解密
        newPassword = this.decrypt(newPassword);
        newPassword2 = this.decrypt(newPassword2);
        // 密码相同
        if (newPassword.equals(newPassword2)) {
            SysUser user = new SysUser();
            try {
                user.setId(Integer.parseInt(userId));
                // 密码MD5加密
                user.setPassword(SecureUtil.md5(newPassword));
                if (userService.save(user, null) > 0) {
                    return success("密码重置成功！");
                } else {
                    error("密码重置失败，请重试！");
                }
            } catch (Exception e) {
                return error(e.getMessage());
            }
        }
        return error("密码重置失败，新密码不一致！");
    }

    /**
     * 接口功能：获取指定角色所分配的用户列表（拥有/不拥有此角色）
     *
     * @param roleId 角色ID
     * @param have   是否用于此角色：0-不拥有 | 1-拥有
     * @return 用户列表
     */
    @GetMapping("/listForRole/{have}/{roleId}")
    @ResponseBody
    public BaseResult getUserListForRole(@PathVariable("have") String have, @PathVariable("roleId") String roleId) {
        try {
            // 拥有 此角色
            List<SysUser> userList;
            if (have.equals("1")) {
                userList = userService.getUserListByRoleId(Integer.parseInt(roleId));
            }
            // 不拥有 此角色
            else {
                userList = userService.getUserListByRoleIdNotIn(Integer.parseInt(roleId));
            }
            return success(200, "获取成功", userList);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 访问页面：用户个人信息管理 - 修改用户信息
     *
     * @return 跳转至编辑用户页面
     */
    @RequestMapping("center/setting")
    public String goSetting(ModelMap map) {
        SysUser user = userService.getById(StpUtil.getLoginIdAsInt());
        if (user != null) {
            map.put("user", user);
            return "system/userCenter/setting";
        }
        map.put("code", 404);
        map.put("msg", "访问错误！");
        return "error/error";
    }

    /**
     * 访问页面：用户管理 - 编辑用户信息页面
     *
     * @return 跳转至编辑用户页面
     */
    @RequestMapping("center/password")
    public String goChangePassword() {
        return "system/userCenter/password";
    }

    /**
     * 接口功能：个人中心保存用户信息接口
     *
     * @return 结果
     */
    @PostMapping(value = "/center/saveSetting")
    @ResponseBody
    public BaseResult saveSetting(SysUser user) {
        user.setId(StpUtil.getLoginIdAsInt());
//        if (userService.save(user, null) > 0) {
//            return success(200, "修改成功");
//        }
        return error("修改失败，此接口暂不开放！");
    }

    /**
     * 接口功能：个人中心修改用户密码接口
     *
     * @param oldPassword  旧密码
     * @param newPassword1 新密码
     * @param newPassword2 新密码
     * @return 修改结果
     */
    @PostMapping(value = "/center/changePassword")
    @ResponseBody
    public BaseResult changePassword(String oldPassword, String newPassword1, String newPassword2) {
        if (!newPassword1.equals(newPassword2)) {
            return error("新密码不一致！");
        }
        return error("修改失败，此接口暂不开放！");
    }

    /**
     * 私有方法：解密密码
     *
     * @param encrypt 加密密码
     * @return 解密密码
     */
    private String decrypt(String encrypt) {
        // 根据私钥生成新的对象
        RSA rsa = new RSA(exciteConfig.getRsaPrivateKey(), null);
        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
        return StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
    }
}
