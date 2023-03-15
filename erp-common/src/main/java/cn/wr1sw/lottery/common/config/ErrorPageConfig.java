package cn.wr1sw.lottery.common.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author wr1sw
 * @version 1.0.0
 * @description 全局错误访问返回结果
 */
@Component
public class ErrorPageConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(
                // 当遇到 NOT_FOUND 错误时，请求 /error/404 路由。
                new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"),
                // 当遇到 INTERNAL_SERVER_ERROR 错误时，请求 /error/500 路由。
                new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500")
        );
    }
}
