package cn.wr1sw.lottery.common.utils;

import com.google.code.kaptcha.text.impl.DefaultTextCreator;

import java.security.SecureRandom;
import java.util.Map;

/**
 * <p>
 * 验证码随机文本生成器之：一位数的加减乘除
 * </p>
 *
 * @author 拾年之璐
 * @since 2021-10-04 0004 20:24
 */
public class KaptchaMathOneTextCreator extends DefaultTextCreator {

    @Override
    public String getText() {
//        Random random = new SecureRandom();
        SecureRandom random = new SecureRandom();
        // 生成两个随机数，随机数范围：[0,10)，并返回结果
        Map<String, String> result = MyCaptchaUtil.mathTextCreator(random.nextInt(10), random.nextInt(10));
        return result.get("resultString");
    }

}
