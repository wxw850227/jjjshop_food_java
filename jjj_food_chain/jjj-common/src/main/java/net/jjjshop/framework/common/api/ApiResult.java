

package net.jjjshop.framework.common.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * REST API 返回结果
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
public class ApiResult<T> implements Serializable {
	private static final long serialVersionUID = 8004487252556526569L;

	/**
     * 请求响应码，1成功，0失败
     */
    private int code;

    /**
     * 错误码
     */
    private int errCode;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    public ApiResult() {
        time  = new Date();
    }

    public static ApiResult<Boolean> result(boolean flag){
        if (flag){
            return ok();
        }
        return fail();
    }

    public static ApiResult<Boolean> result(ApiCode apiCode){
        return result(apiCode,null);
    }

    public static <T> ApiResult<T> result(ApiCode apiCode,T data){
        return result(apiCode,null,data);
    }

    public static <T> ApiResult<T> result(ApiCode apiCode,String msg,T data){
        int code = 0;
        if (apiCode.getCode() == ApiCode.SUCCESS.getCode()){
            code = 1;
        }
        // 如果小于0，未登录
        if(apiCode.getCode() < 0){
            code = apiCode.getCode();
        }
        if(StringUtils.isBlank(msg)){
            String apiMessage = apiCode.getMessage();
            if (StringUtils.isNotBlank(apiMessage)){
                msg = apiMessage;
            }
        }
        return (ApiResult<T>) ApiResult.builder()
                .code(code)
                .errCode(apiCode.getCode())
                .msg(msg)
                .data(data)
                .time(new Date())
                .build();
    }

    public static ApiResult<Boolean> ok(){
        return ok(null);
    }

    public static <T> ApiResult<T> ok(T data){
        return result(ApiCode.SUCCESS,data);
    }

    public static <T> ApiResult<T> ok(T data,String msg){
        return result(ApiCode.SUCCESS,msg,data);
    }

    public static ApiResult<Map<String,Object>> okMap(String key,Object value){
        Map<String,Object> map = new HashMap<>(1);
        map.put(key,value);
        return ok(map);
    }

    public static ApiResult<Boolean> fail(ApiCode apiCode){
        return result(apiCode,null);
    }

    public static ApiResult<String> fail(String msg){
        return result(ApiCode.FAIL,msg,null);

    }

    public static <T> ApiResult<T> fail(ApiCode apiCode,T data){
        if (ApiCode.SUCCESS == apiCode){
            throw new RuntimeException("失败结果状态码不能为" + ApiCode.SUCCESS.getCode());
        }
        return result(apiCode,data.toString(),data);
    }

    public static  ApiResult<String> fail(Integer errorCode,String msg){
        int code = 0;
        // 如果小于0，未登录
        if(errorCode < 0){
            code = errorCode;
        }
        return new ApiResult<String>()
                .setCode(code)
                .setErrCode(errorCode)
                .setMsg(msg);
    }

    public static ApiResult<Map<String,Object>> fail(String key,Object value){
        Map<String,Object> map = new HashMap<>(1);
        map.put(key,value);
        return result(ApiCode.FAIL,map);
    }

    public static ApiResult<Boolean> fail() {
        return fail(ApiCode.FAIL);
    }
}