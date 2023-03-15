package cn.wr1sw.lottery.system.service.impl;

import cn.wr1sw.lottery.system.entity.SysLogLogin;
import cn.wr1sw.lottery.system.mapper.SysLogLoginMapper;
import cn.wr1sw.lottery.system.service.ISysLogLoginService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 拾年之璐
 * @since 2021-12-28
 */
@Service
@AllArgsConstructor
public class SysLogLoginServiceImpl extends ServiceImpl<SysLogLoginMapper, SysLogLogin> implements ISysLogLoginService {


    SysLogLoginMapper loginMapper;

    @Override
    public Page<SysLogLogin> getPage(Integer current, Integer size, String userId, String ip, String startDate, String endDate) {
        // 页码信息
        current = null == current ? 1 : current;
        size = null == size ? 10 : size;
        QueryWrapper<SysLogLogin> wrapper = new QueryWrapper<>();
        // 检索请求用户
        wrapper.like(userId != null && !"".equals(userId), "user_id", userId)
                // 检索IP
                .like(ip != null && !"".equals(ip), "ip", ip)
                // 检索起始时间
                .ge(startDate != null && !"".equals(startDate), "create_time", startDate)
                // 检索终止时间
                .le(endDate != null && !"".equals(endDate), "create_time", endDate)
                .orderByDesc("id");
        return loginMapper.selectPage(new Page<>(current, size), wrapper);
    }

    @Override
    public int deleteLog(Integer[] logIds) {
        return loginMapper.deleteBatchIds(Arrays.asList(logIds));
    }
}
