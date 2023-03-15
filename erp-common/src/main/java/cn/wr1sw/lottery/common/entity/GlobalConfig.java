package cn.wr1sw.lottery.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wr1sw
 * @version 1.0.0
 * @description 组件配置信息表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GlobalConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 配置信息的服务：如qiniu，aliyun
     */
    private String confService;

    /**
     * 配置信息的主键：如qiniu、alipay等等
     */
    private String confKey;

    /**
     * 配置信息的JSON值
     */
    private String confValue;

    /**
     * 是否加密：1-是 0-否
     */
    private Integer encrypt;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;


}
