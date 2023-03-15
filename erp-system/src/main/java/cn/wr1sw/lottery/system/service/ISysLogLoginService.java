package cn.wr1sw.lottery.system.service;

import cn.wr1sw.lottery.system.entity.SysLogLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 拾年之璐
 * @since 2021-12-28
 */
public interface ISysLogLoginService extends IService<SysLogLogin> {

    Page<SysLogLogin> getPage(Integer current, Integer size, String userId, String ip, String startDate, String endDate);

    int deleteLog(Integer[] logIds);
}
