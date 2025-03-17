package net.jjjshop.common.util.poster;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.enums.OrderPayStatusEnum;
import net.jjjshop.common.service.order.OrderService;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.common.exception.BusinessException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 自提核销码生成
 */
@Slf4j
@Component
public class ExtractUtils {
    @Autowired
    private ResourceLoader resourceLoader;
    @Lazy
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Autowired
    private OrderService orderService;

    private static final int width = 430;

    public String genePoster(Integer orderId, User user){
        Order order = orderService.getById(orderId);
        if(order.getUserId().intValue() != user.getUserId().intValue()){
            throw new BusinessException("非法请求");
        }
        // 已支付，发货方式自提，未发货
        if(order.getPayStatus().intValue() != OrderPayStatusEnum.SUCCESS.getValue().intValue()
            || order.getDeliveryType().intValue() != DeliveryTypeEnum.ZITI.getValue().intValue()
            || order.getDeliveryStatus() != 10){
            throw new BusinessException("该订单不能被核销");
        }
        try{
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(order.getOrderNo(), BarcodeFormat.QR_CODE, width, width);
            Path path = Paths.get(this.getPosterPath(user, orderId));
            // 判断目录是否存在，不存在，则创建，如创建失败，则抛出异常
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            return this.getPosterUrl(user, orderId);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 海报图文件路径
     */
    private String getPosterPath(User user, Integer orderId)
    {
        // 保存路径
        String tempPath = springBootJjjProperties.getUploadPath() + "/temp";
        return tempPath + "/extract/" + user.getAppId() + "/" + this.getPosterName(user, orderId);
    }

    /**
     * 海报图文件名称
     */
    private String getPosterName(User user, Integer orderId)
    {
        String userId = "";
        if(user != null){
            userId = "" + user.getUserId();
        }
        return "extract_" + DigestUtils.md5Hex(userId+ "_" + orderId) + ".png";
    }

    /**
     * 海报图url
     */
    private String getPosterUrl(User user, Integer orderId)
    {
        return springBootJjjProperties.getResourceAccessUrl() + "temp/extract/" + user.getAppId() + "/" + this.getPosterName(user, orderId) + "?t="+System.currentTimeMillis();
    }
}
