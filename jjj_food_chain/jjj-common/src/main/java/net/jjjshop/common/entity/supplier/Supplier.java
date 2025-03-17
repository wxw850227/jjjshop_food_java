package net.jjjshop.common.entity.supplier;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * 供应商表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_supplier")
@ApiModel(value = "Supplier对象")
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "shop_supplier_id", type = IdType.AUTO)
    private Integer shopSupplierId;

    @ApiModelProperty("供应商姓名")
    private String name;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("联系人")
    private String linkName;

    @ApiModelProperty("联系电话")
    private String linkPhone;

    @ApiModelProperty("logo")
    private String logo;

    @ApiModelProperty("联系地址")
    private String address;

    @ApiModelProperty("营业执照")
    private Integer businessId;

    @ApiModelProperty("商家介绍")
    private String description;

    @ApiModelProperty("总货款")
    private BigDecimal totalMoney;

    @ApiModelProperty("当前可提现金额")
    private BigDecimal money;

    @ApiModelProperty("已冻结金额")
    private BigDecimal freezeMoney;

    @ApiModelProperty("累积提现佣金")
    private BigDecimal cashMoney;

    @ApiModelProperty("保证金")
    private BigDecimal depositMoney;

    @ApiModelProperty("会员id")
    private Integer userId;

    @ApiModelProperty("关注人数")
    private Integer favCount;

    @ApiModelProperty("店铺状态0营业中1停止营业")
    private Integer status;

    @ApiModelProperty("店铺类型10加盟20自营")
    private Integer storeType;

    @ApiModelProperty("收到的礼物币总数")
    private Integer totalGift;

    @ApiModelProperty("是否禁用0否1是")
    private Integer isRecycle;

    @ApiModelProperty("是否总店，0否1是")
    private Integer isMain;

    @ApiModelProperty("所在省份id")
    private Integer provinceId;

    @ApiModelProperty("所在城市id")
    private Integer cityId;

    @ApiModelProperty("所在辖区id")
    private Integer regionId;

    @ApiModelProperty("门店坐标经度")
    private String longitude;

    @ApiModelProperty("门店坐标纬度")
    private String latitude;

    @ApiModelProperty("配送费")
    private BigDecimal shippingFee;

    @ApiModelProperty("外卖包装费类型0按商品收费1按单收费")
    private Integer bagType;

    @ApiModelProperty("外卖包装费")
    private BigDecimal bagPrice;

    @ApiModelProperty("店内包装费类型0按商品收费1按单收费")
    private Integer storebagType;

    @ApiModelProperty("店内包装费")
    private BigDecimal storebagPrice;

    @ApiModelProperty("外卖营业时间")
    private String deliveryTime;

    @ApiModelProperty("自提营业时间")
    private String pickTime;

    @ApiModelProperty("店内营业时间")
    private String storeTime;

    @ApiModelProperty("配送范围km")
    private Float deliveryDistance;

    @ApiModelProperty("外卖配送方式")
    private String deliverySet;

    @ApiModelProperty("店内用餐方式")
    private String storeSet;

    @ApiModelProperty("最低消费")
    private BigDecimal minMoney;

    @ApiModelProperty("计算模式10先结账后用餐20先用餐后结账")
    private Integer settleType;

    @ApiModelProperty("服务费类型0按就餐人数1按桌台收费")
    private Integer serviceType;

    @ApiModelProperty("服务费")
    private BigDecimal serviceMoney;

    @ApiModelProperty("0定时清台1立即清台")
    private Integer autoClose;

    @ApiModelProperty("0分钟清台")
    private Integer closeTime;

    @ApiModelProperty("商品分类设置10同步主店20分店创建")
    private Boolean categorySet;

    @ApiModelProperty("是否删除0，否1是")
    private Integer isDelete;

    @ApiModelProperty("程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
