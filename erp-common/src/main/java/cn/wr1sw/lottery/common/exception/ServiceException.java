package cn.wr1sw.lottery.common.exception;

/**
 * @author wr1sw
 * @version 1.0.0
 * @description 服务层异常处理，继承RunTimeException，来保证对异常进行事务回滚
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;

    private String details;

    public ServiceException() {
    }

    public ServiceException(String message) {
        this.message = message;
    }

    public ServiceException(String message, String details) {
        this.message = message;
        this.details = details;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
