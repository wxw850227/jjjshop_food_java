

package net.jjjshop.handler;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiCode;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.exception.DaoException;
import net.jjjshop.framework.common.exception.SpringBootJjjException;
import net.jjjshop.framework.core.bean.RequestDetail;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import net.jjjshop.shop.exception.SysLoginException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 非法参数验证异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ApiResult<List<String>> handleMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        printRequestDetail();
        BindingResult bindingResult = ex.getBindingResult();
        List<String> list = new ArrayList<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            list.add(fieldError.getDefaultMessage());
        }
        Collections.sort(list);
        log.error(getApiCodeString(ApiCode.PARAMETER_EXCEPTION) + ":" + JSON.toJSONString(list));
        return ApiResult.fail(ApiCode.PARAMETER_EXCEPTION, list);
    }

    /**
     * 系统登录异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = SysLoginException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<Boolean> sysLoginExceptionHandler(SysLoginException exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.LOGIN_EXCEPTION, exception);
        return ApiResult.fail(ApiCode.LOGIN_EXCEPTION);
    }


    /**
     * HTTP解析请求参数异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<Boolean> httpMessageNotReadableException(HttpMessageNotReadableException exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.PARAMETER_EXCEPTION, exception);
        return ApiResult.fail(ApiCode.PARAMETER_EXCEPTION);
    }

    /**
     * HTTP
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = HttpMediaTypeException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<Boolean> httpMediaTypeException(HttpMediaTypeException exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.HTTP_MEDIA_TYPE_EXCEPTION, exception);
        return ApiResult.fail(ApiCode.HTTP_MEDIA_TYPE_EXCEPTION);
    }

    /**
     * 自定义业务/数据异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {SpringBootJjjException.class})
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<String> springBootJjjExceptionHandler(SpringBootJjjException exception) {
        int errorCode;
        boolean isLog = true;
        if (exception instanceof BusinessException) {
            if(exception.getErrorCode() != null){
                errorCode = exception.getErrorCode();
            }else{
                errorCode = ApiCode.BUSINESS_EXCEPTION.getCode();
            }
            isLog = false;
        } else if (exception instanceof DaoException) {
            errorCode = ApiCode.DAO_EXCEPTION.getCode();
        } else {
            errorCode = ApiCode.SPRING_BOOT_JJJ_EXCEPTION.getCode();
        }
        // BusinessException不记录
        if(isLog){
            printRequestDetail();
            log.error("springBootJjjException:", exception);
        }
        return ApiResult.fail(errorCode, exception.getMessage());
    }


    /**
     * 登录授权异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<String> authenticationExceptionHandler(AuthenticationException exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.AUTHENTICATION_EXCEPTION, exception);
        return ApiResult.fail(exception.getMessage());
    }


    /**
     * 未认证异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = UnauthenticatedException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<Boolean> unauthenticatedExceptionHandler(UnauthenticatedException exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.UNAUTHENTICATED_EXCEPTION, exception);
        return ApiResult.fail(ApiCode.UNAUTHENTICATED_EXCEPTION);
    }


    /**
     * 未授权异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<Boolean> unauthorizedExceptionHandler(UnauthorizedException exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.UNAUTHORIZED_EXCEPTION, exception);
        return ApiResult.fail(ApiCode.UNAUTHORIZED_EXCEPTION);
    }


    /**
     * JWT Token解析异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = JWTDecodeException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<Boolean> jWTDecodeExceptionHandler(JWTDecodeException exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.JWTDECODE_EXCEPTION, exception);
        return ApiResult.fail(ApiCode.JWTDECODE_EXCEPTION);
    }

    /**
     * 默认的异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<String> httpRequestMethodNotSupportedExceptionHandler(Exception exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION, exception);
        return ApiResult.fail(ApiCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION.getCode(), exception.getMessage());
    }

    /**
     * 默认的异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<String> exceptionHandler(Exception exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.SYSTEM_EXCEPTION, exception);
        if(exception.getCause() != null && exception.getCause().getCause() != null){
            return ApiResult.fail(0, exception.getCause().getCause().getMessage());
        }else{
            return ApiResult.fail(0, exception.getMessage());
        }
    }


    /**
     * 打印请求详情
     */
    private void printRequestDetail() {
        RequestDetail requestDetail = RequestDetailThreadLocal.getRequestDetail();
        if (requestDetail != null) {
            log.error("异常来源：ip: {}, path: {}", requestDetail.getIp(), requestDetail.getPath());
        }
    }

    /**
     * 获取ApiCode格式化字符串
     *
     * @param apiCode
     * @return
     */
    private String getApiCodeString(ApiCode apiCode) {
        if (apiCode != null) {
            return String.format("errorCode: %s, errorMessage: %s", apiCode.getCode(), apiCode.getMessage());
        }
        return null;
    }

    /**
     * 打印错误码及异常
     *
     * @param apiCode
     * @param exception
     */
    private void printApiCodeException(ApiCode apiCode, Exception exception) {
        log.error(getApiCodeString(apiCode), exception);
    }

}
