package cn.wr1sw.lottery.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目启动入口
 *
 * @author 拾年之璐
 */
@RestController
@SpringBootApplication(scanBasePackages = {
        "cn.wr1sw.lottery",
        "cn.wr1sw.lottery.system.service",
        "cn.wr1sw.lottery.common.service",
        "cn.wr1sw.lottery.erp.domain.activity",
})
@MapperScan(basePackages = {
        "cn.wr1sw.lottery.system.mapper",
        "cn.wr1sw.lottery.common.mapper",
        "cn.wr1sw.lottery.erp.mapper",
})
public class LotteryAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteryAdminApplication.class, args);
        System.out.println("系统启动成功，访问 http://localhost:8181 ");
    }

}
