package cn.wr1sw.lottery.common.service;

import cn.wr1sw.lottery.common.entity.GlobalConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;


import java.util.List;

/**
 * @author wr1sw
 * @version 1.0.0
 * @description 组件配置信息表 服务类
 */
public interface IGlobalConfigService extends IService<GlobalConfig> {

    boolean save(String confService, String confKey, Object object, boolean encrypt) throws JsonProcessingException;

    Object get(String confService, String confKey, Object object);

    List<Object> getList(String confService, Object object);
}
