package cn.wr1sw.lottery.admin.system;


import cn.wr1sw.lottery.common.base.BaseController;
import cn.wr1sw.lottery.common.base.BaseResult;
import cn.wr1sw.lottery.system.entity.SysLogLogin;
import cn.wr1sw.lottery.system.entity.SysLogRequest;
import cn.wr1sw.lottery.system.service.ISysLogLoginService;
import cn.wr1sw.lottery.system.service.ISysLogRequestService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 日志管理控制器
 *
 * @author 拾年之璐
 * @since 2021-12-09
 */
@Controller
@AllArgsConstructor
@RequestMapping("/system/log")
public class SysLogController extends BaseController {

    ISysLogRequestService logRequestService;

    ISysLogLoginService logLoginService;

    /**
     * @return 【操作日志】页面访问入口
     */
    @RequestMapping("request")
    public String requestLogPage() {
        return "system/log/request";
    }

    /**
     * @return 【登录日志】页面访问入口
     */
    @RequestMapping("login")
    public String loginLogPage() {
        return "system/log/login";
    }


    /**
     * 获取操作日志列表（含查询条件）
     *
     * @param page      页码
     * @param limit     页条数
     * @param userId    用户ID，格式：10001
     * @param startDate 开始时间，格式：2022-01-11 00:00:00
     * @param endDate   结束时间，格式：2022-01-11 00:00:00
     * @return 操作日志列表
     */
    @GetMapping("/requestList")
    @ResponseBody
    public BaseResult requestList(Integer page, Integer limit, String userId, String startDate, String endDate) {
        try {
            // TODO 加上根据ID和开始、结束时间的搜索功能
            Page<SysLogRequest> rolePage = logRequestService.getPage(page, limit, userId, startDate, endDate);
            return success("获取日志列表成功", rolePage.getRecords(), (int) rolePage.getTotal());
        } catch (Exception e) {
            return error(500, "发生错误:" + e.getMessage());
        }
    }

    /**
     * 获取登录日志列表
     *
     * @param page      页码
     * @param limit     页条数
     * @param userId    用户ID，格式：10001
     * @param startDate 开始时间，格式：2022-01-11 00:00:00
     * @param endDate   结束时间，格式：2022-01-11 00:00:00
     * @return 登录日志列表
     */
    @GetMapping("/loginList")
    @ResponseBody
    public BaseResult loginList(Integer page, Integer limit, String userId, String ip, String startDate, String endDate) {
        try {
            // TODO 加上搜索功能，详情同上
            Page<SysLogLogin> rolePage = logLoginService.getPage(page, limit, userId, ip, startDate, endDate);
            return success("获取日志列表成功", rolePage.getRecords(), (int) rolePage.getTotal());
        } catch (Exception e) {
            return error(500, "发生错误:" + e.getMessage());
        }
    }

    @PostMapping("/request/delete")
    @ResponseBody
    public BaseResult deleteRequestLog(Integer[] logIds) {
        if (null == logIds || logIds.length == 0) {
            return error("请求数据为空！");
        }
        if (logRequestService.deleteLog(logIds) > 0) {
            return success("删除成功！");
        }
        return error("删除失败！");
    }

    @PostMapping("/login/delete")
    @ResponseBody
    public BaseResult deleteLoginLog(Integer[] logIds) {
        if (null == logIds || logIds.length == 0) {
            return error("请求数据为空！");
        }
        if (logLoginService.deleteLog(logIds) > 0) {
            return success("删除成功！");
        }
        return error("删除失败！");
    }
}
