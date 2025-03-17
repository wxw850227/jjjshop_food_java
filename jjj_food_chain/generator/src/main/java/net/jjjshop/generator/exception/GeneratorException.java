

package net.jjjshop.generator.exception;

import net.jjjshop.framework.common.api.ApiCode;
import net.jjjshop.framework.common.exception.SpringBootJjjException;

/**
 * 代码生成异常
 */
public class GeneratorException extends SpringBootJjjException {
	private static final long serialVersionUID = 2556853577480934761L;

	public GeneratorException(String message) {
        super(message);
    }

    public GeneratorException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public GeneratorException(ApiCode apiCode) {
        super(apiCode);
    }
}
