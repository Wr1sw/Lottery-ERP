package cn.wr1sw.lottery.component.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 阿里云短信服务配置信息
 *
 * @author 拾年之璐
 * @since 2022/1/3 20:28
 */
@Data
@Accessors(chain = true)
public class AliyunSmsBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 本条短信配置信息的key
     */
    private String key;

    /**
     * 必填。开发者的 AccessKey ID
     */
    private String accessKeyId;

    /**
     * 必填。开发者的 AccessKey Secret
     */
    private String accessKeySecret;

    /**
     * 必填。短信模板的签名，严格按照"签名名称"填写，格式如：阿里云
     */
    private String signName;

    /**
     * 必填。模板CODE，应严格按"模板CODE"填写，格式：SMS_88888888
     */
    private String templateCode;

    /**
     * 选填。如果消息模板中有参数，则必填。设置模板参数，如：[code,data]
     */
    private String[] templateParam;
}
