package cn.wr1sw.lottery.erp.application;

import cn.wr1sw.lottery.common.base.BaseResult;
import cn.wr1sw.lottery.erp.domain.activity.model.ActivityListPageReq;

/**
 * @author wr1sw
 * @version 1.0.0
 * @description 活动服务接口
 */
public interface IActivityService {
    /**
     * 查询活动分页数据
     * @param req   入参
     * @return      结果
     */
    BaseResult queryActivityListPage(ActivityListPageReq req);
}
