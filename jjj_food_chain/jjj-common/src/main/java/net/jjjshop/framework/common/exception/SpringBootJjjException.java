

package net.jjjshop.framework.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.jjjshop.framework.common.api.ApiCode;

/**
 * 自定义异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpringBootJjjException extends RuntimeException{

    private static final long serialVersionUID = -2470461654663264392L;

    private Integer errorCode;
    private String message;

    public SpringBootJjjException() {
        super();
    }

    public SpringBootJjjException(String message) {
        super(message);
        this.message = message;
    }

    public SpringBootJjjException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public SpringBootJjjException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.errorCode = apiCode.getCode();
        this.message = apiCode.getMessage();
    }

    public SpringBootJjjException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpringBootJjjException(Throwable cause) {
        super(cause);
    }

}
