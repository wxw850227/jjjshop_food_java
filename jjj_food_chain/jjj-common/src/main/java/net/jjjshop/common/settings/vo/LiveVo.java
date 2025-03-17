package net.jjjshop.common.settings.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("积分设置VO")
public class LiveVo implements Serializable {

    private static final long serialVersionUID = 1L;

    // 是否开启直播
    private Integer isOpen;
    //是否开启审核
    private Integer isAudit;
    //礼物名称
    private String giftName;
    // appId
    private String appId;
    // key
    private String key;
    // 是否开启录制
    private Integer isRecord;
    // 存储设置
    private String vendor;
    private String region;
    private String bucket;
    private String accessKey;
    private String secretKey;
    private String username;
    private String password;
    private String domain;
    private List<Server> storage;


    @Data
    @Accessors(chain = true)
    @ApiModel("存储Vo")
    public static class Server implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private JSONObject region;
        private Integer vendor;
    }


    public LiveVo() {
        this.isOpen = 0;
        this.isAudit = 1;
        this.giftName = "玖币";
        this.appId = "";
        this.key = "";
        this.isRecord = 0;
        this.vendor = "";
        this.region = "";
        this.bucket = "";
        this.accessKey = "";
        this.secretKey = "";
        this.username = "";
        this.password = "";
        this.domain = "";

        List<Server> list = new ArrayList<>();
        Server aliyun = new Server();
        aliyun.setName("阿里云");
        JSONObject aliyunRegion = new JSONObject();
        aliyunRegion.put("杭州",0);
        aliyunRegion.put("上海",1);
        aliyunRegion.put("青岛",2);
        aliyunRegion.put("北京",3);
        aliyunRegion.put("张家界",4);
        aliyunRegion.put("呼和浩特",5);
        aliyunRegion.put("深圳",6);
        aliyunRegion.put("香港",7);
        aliyun.setRegion(aliyunRegion);
        aliyun.setVendor(2);
        list.add(aliyun);

        Server qcloud = new Server();
        qcloud.setName("腾讯云");
        JSONObject qcloudRegion = new JSONObject();
        qcloudRegion.put("北京",1);
        qcloudRegion.put("上海",2);
        qcloudRegion.put("广州",3);
        qcloudRegion.put("成都",4);
        qcloudRegion.put("重庆",5);
        qcloudRegion.put("深圳金融",6);
        qcloudRegion.put("上海金融",7);
        qcloudRegion.put("北京金融",8);
        qcloudRegion.put("香港",9);
        qcloud.setRegion(qcloudRegion);
        qcloud.setVendor(3);
        list.add(qcloud);

        Server qiniu = new Server();
        qiniu.setName("七牛云");
        JSONObject qiniuRegion = new JSONObject();
        qiniuRegion.put("华东",0);
        qiniuRegion.put("华北",1);
        qiniuRegion.put("华南",2);
        qiniuRegion.put("北美",3);
        qiniuRegion.put("东南亚",4);
        qiniu.setRegion(qiniuRegion);
        qiniu.setVendor(0);
        list.add(qiniu);

        this.storage = list;
    }
}
