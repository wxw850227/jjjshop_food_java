

package net.jjjshop.framework.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.jjjshop.framework.common.api.ApiCode;

/**
 * spring-boot-jjj配置异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpringBootJjjConfigException extends SpringBootJjjException {

    private static final long serialVersionUID = 8952028631871769425L;

    private Integer errorCode;
    private String message;

    public SpringBootJjjConfigException() {
        super();
    }

    public SpringBootJjjConfigException(String message) {
        super(message);
        this.message = message;
    }

    public SpringBootJjjConfigException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public SpringBootJjjConfigException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.errorCode = apiCode.getCode();
        this.message = apiCode.getMessage();
    }

    public SpringBootJjjConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpringBootJjjConfigException(Throwable cause) {
        super(cause);
    }

}
