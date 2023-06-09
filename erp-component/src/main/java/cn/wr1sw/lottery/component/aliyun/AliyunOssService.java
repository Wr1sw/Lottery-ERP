package cn.wr1sw.lottery.component.aliyun;

import cn.hutool.core.codec.Base64;
import cn.wr1sw.lottery.common.exception.ServiceException;
import cn.wr1sw.lottery.common.service.IGlobalConfigService;
import cn.wr1sw.lottery.component.bo.AliyunOssBO;
import cn.wr1sw.lottery.component.vo.AliyunOssPolicyVO;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

/**
 * 阿里云对象存储服务
 *
 * @author 拾年之璐
 * @since 2022/1/5 20:37
 */
@Service
@AllArgsConstructor
public class AliyunOssService {

    IGlobalConfigService configService;

    private static final String DEFAULT_SERVICE = "aliyunOss";

    private static final String DEFAULT_KEY = "aliyunOss";

    public boolean saveConfig(AliyunOssBO aliyunOssBO) throws JsonProcessingException {
        if (null == aliyunOssBO.getBucketName() || null == aliyunOssBO.getBucketDomain() || null == aliyunOssBO.getEndpoint() || null == aliyunOssBO.getAccessKeyId() || null == aliyunOssBO.getAccessKeySecret()) {
            throw new ServiceException("部分参数为空，请检查！");
        }
        if (null == aliyunOssBO.getKey()) {
            return configService.save(DEFAULT_SERVICE, DEFAULT_KEY, aliyunOssBO, true);
        } else {
            return configService.save(DEFAULT_SERVICE, aliyunOssBO.getKey(), aliyunOssBO, true);
        }
    }

    public AliyunOssBO getConfig(String confKey) {
        AliyunOssBO aliyunOssBO = new AliyunOssBO();
        return (AliyunOssBO) configService.get(DEFAULT_SERVICE, confKey, aliyunOssBO);
    }

    public AliyunOssBO getConfig() {
        return this.getConfig(DEFAULT_KEY);
    }



    /**
     * 前端直传用的方法
     *
     * @return
     */
    public AliyunOssPolicyVO createUploadToken(String confKey, String fileName, Long expireTime) {
        // 读取秘钥等信息
        AliyunOssBO aliyunOssBO = this.getConfig(confKey);
        // 创建OSSClient实例。
        OSS oss = new OSSClientBuilder().build(aliyunOssBO.getEndpoint(), aliyunOssBO.getAccessKeyId(), aliyunOssBO.getAccessKeySecret());

        PolicyConditions policyConditions = new PolicyConditions();
        // 策略：最大上传文件1G
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);

        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expireEndDate = new Date(expireEndTime);
        try {
            // 生成上传策略
            String postPolicy = oss.generatePostPolicy(expireEndDate, policyConditions);
            // 生成签名
            String postSignature = oss.calculatePostSignature(postPolicy);
            // 策略转码
            String base64Policy = Base64.encode(postPolicy);

            // 构造返回数据
            return new AliyunOssPolicyVO()
                    .setAccessKeyId(aliyunOssBO.getAccessKeyId())
                    .setPolicy(base64Policy)
                    .setSignature(postSignature)
                    .setHost(aliyunOssBO.getBucketDomain())
                    .setCallback("")
                    .setKey(fileName);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            oss.shutdown();
        }
    }

    /**
     * 前端直传用的方法
     *
     * @return
     */
    public AliyunOssPolicyVO createUploadToken(String fileName, Long expireTime) {
        return this.createUploadToken(DEFAULT_KEY, fileName, expireTime);
    }


    public boolean uploadFile(String confKey, File file) {
        // 读取配置
        AliyunOssBO aliyunOssBO = this.getConfig(confKey);
        // 创建OSSClient实例。
        OSS oss = new OSSClientBuilder().build(aliyunOssBO.getEndpoint(), aliyunOssBO.getAccessKeyId(), aliyunOssBO.getAccessKeySecret());
        try {
            PutObjectResult putObjectRequest = oss.putObject(aliyunOssBO.getBucketName(), file.getName(), file);
            return putObjectRequest.getResponse().isSuccessful();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            oss.shutdown();
        }
    }

    public boolean uploadFile(File file) {
        return this.uploadFile(DEFAULT_KEY, file);
    }
}
