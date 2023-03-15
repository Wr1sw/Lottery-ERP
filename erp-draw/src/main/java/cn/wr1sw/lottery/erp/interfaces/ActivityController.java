package cn.wr1sw.lottery.erp.interfaces;


import cn.wr1sw.lottery.common.base.BaseController;
import cn.wr1sw.lottery.common.base.BaseResult;
import cn.wr1sw.lottery.erp.application.IActivityService;
import cn.wr1sw.lottery.erp.domain.activity.model.ActivityListPageReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wr1sw
 * @version 1.0.0
 * @description 活动管理
 */
@Controller
@RequestMapping("/draw/activity")
public class ActivityController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Resource
    private IActivityService activityService;

    @RequestMapping("index")
    public String index() {
        System.out.println("____+++++++>>");
        return "lottery/activity/index";
    }

    @RequestMapping("queryActivityListPage")
    @ResponseBody
    public BaseResult queryActivityListPage(String page, String rows) {
        try {
            logger.info("查询活动列表数据 page：{} rows：{}", page, rows);
            rows = null == rows ? "10" : rows;
            ActivityListPageReq req = new ActivityListPageReq(page, rows);
            req.setErpId("wr1sw");
            return activityService.queryActivityListPage(req);
        } catch (Exception e) {
            logger.error("查询活动列表数据失败 page：{} rows：{}", page, rows, e);
            return error(e.getMessage());
        }
    }
}
