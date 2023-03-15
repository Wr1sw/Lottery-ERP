package cn.wr1sw.lottery.erp.domain.activity.service;

import cn.wr1sw.lottery.common.Constants;
import cn.wr1sw.lottery.common.base.BaseResult;
import cn.wr1sw.lottery.erp.application.IActivityService;
import cn.wr1sw.lottery.erp.domain.activity.model.ActivityListPageReq;
import cn.wr1sw.lottery.rpc.activity.deploy.ILotteryActivityDeploy;
import cn.wr1sw.lottery.rpc.activity.deploy.req.ActivityPageReq;
import cn.wr1sw.lottery.rpc.activity.deploy.res.ActivityRes;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @author wr1sw
 * @version 1.0.0
 * @description
 */
@Service
public class ActivityServiceImpl implements IActivityService {

    @DubboReference(interfaceClass = ILotteryActivityDeploy.class, url = "dubbo://127.0.0.1:20880")
//    @DubboReference(interfaceClass = ILotteryActivityDeploy.class)
    private ILotteryActivityDeploy activityDeploy;

    @Override
    public BaseResult queryActivityListPage(ActivityListPageReq req) {
        // 1. 封装数据
        ActivityPageReq activityPageReq = new ActivityPageReq(req.getPage(), req.getRows());
        activityPageReq.setErpId(req.getErpId());
        activityPageReq.setActivityId(req.getActivityId());
        activityPageReq.setActivityName(req.getActivityName());

        // 2. 查询数据
        ActivityRes activityRes = activityDeploy.queryActivityListByPageForErp(activityPageReq);

        // 3. 封装结果
        if (Constants.ResponseCode.SUCCESS.getCode().equals(activityRes.getResult().getCode())) {
            return new BaseResult(0,activityRes.getActivityDTOList()).put("count", activityRes.getCount());
        } else {
            return new BaseResult(1, activityRes.getResult().getInfo());
        }
    }
}
