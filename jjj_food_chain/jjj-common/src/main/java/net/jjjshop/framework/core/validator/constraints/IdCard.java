

package net.jjjshop.framework.core.validator.constraints;

import net.jjjshop.framework.core.validator.IdCardValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义身份证号码正则验证注解
 */
@Documented
@Constraint(validatedBy = { IdCardValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface IdCard {
	String message() default "请输入有效的身份证号码";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
