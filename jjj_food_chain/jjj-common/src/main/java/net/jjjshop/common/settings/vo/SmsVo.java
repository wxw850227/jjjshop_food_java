package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("短信设置VO")
public class SmsVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String acceptPhone;
    private String templateCode;
    private AliyunSms aliyunSms;
    private QcloudSms QcloudSms;
    private HWcloudSms HWcloudSms;
    private String radio;


    @Data
    @Accessors(chain = true)
    @ApiModel("阿里云短信VO")
    public static class AliyunSms implements Serializable {
        private static final long serialVersionUID = 1L;
        private String accessKeyId;
        private String accessKeySecret;
        private String sign;
        private String templateCode;

        public AliyunSms() {
            this.accessKeyId = "";
            this.accessKeySecret = "";
            this.sign = "";
            this.templateCode = "";
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("腾讯云短信VO")
    public static class QcloudSms implements Serializable {
        private static final long serialVersionUID = 1L;
        private String accessKeyId;
        private String secretId;
        private String secretKey;
        private String sign;
        private String templateCode;

        public QcloudSms() {
            this.accessKeyId = "";
            this.secretId = "";
            this.secretKey = "";
            this.sign = "";
            this.templateCode = "";
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("华为云短信VO")
    public static class HWcloudSms implements Serializable {
        private static final long serialVersionUID = 1L;
        private String accessKeyId;
        private String accessKeySecret;
        private String sender;
        private String sign;
        private String templateCode;
        //APP接入地址
        private String url;

        public HWcloudSms() {
            this.accessKeyId = "";
            this.accessKeySecret = "";
            this.sender = "";
            this.sign = "";
            this.templateCode = "";
            this.url = "";
        }
    }

    public SmsVo() {
        this.acceptPhone = "";
        this.templateCode = "";
        this.aliyunSms = new AliyunSms();
        this.QcloudSms = new QcloudSms();
        this.HWcloudSms = new HWcloudSms();
        this.radio = "aliyun";
    }
}
