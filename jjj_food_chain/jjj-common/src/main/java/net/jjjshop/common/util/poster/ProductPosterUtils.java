package net.jjjshop.common.util.poster;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.service.product.ProductService;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.util.wx.AppWxUtils;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import sun.font.FontDesignMetrics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

/**
 * 商品海报生成
 */
@Slf4j
@Component
public class ProductPosterUtils {
    @Autowired
    private ResourceLoader resourceLoader;
    @Lazy
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Autowired
    private ProductService productService;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private FontUtils fontUtils;
    @Lazy
    @Autowired
    private WxMaService wxMaService;
    @Lazy
    @Autowired
    private AppWxUtils appWxUtils;

    private static final int width = 750;
    private static final int height = 1136;

    public String genePoster(Integer productId, String source, User user){
        try{
            //画布
            BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) canvas.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // 背景图片
            String bgPath = SpringBootJjjProperties.getStaticAccessUrl() + "image/poster/product_bg.png";
            Resource resource = resourceLoader.getResource(bgPath);
            BufferedImage bg = ImageIO.read(resource.getInputStream());
            g.drawImage(bg.getScaledInstance(width, height, Image.SCALE_DEFAULT), 0, 0, null);
            // 二维码
            if("wx".equals(source)) {
                // 小程序码参数
                String scene = String.format("gid:%s,uid:%s", productId, user == null ? "" : user.getUserId());
                File qrCode = wxMaService.switchoverTo(appWxUtils.getConfig(wxMaService, null)).getQrcodeService().createWxaCodeUnlimit(scene, "pages/product/detail/detail");
                BufferedImage qrCodeImage = ImageIO.read(qrCode);
                g.drawImage(qrCodeImage.getScaledInstance(140, 140, Image.SCALE_DEFAULT), 570, 914, null);
            }else{
                // h5码
                String url = String.format("%s/h5/pages/product/detail/detail?productId=%s&refereeId=%s&appId=%s", springBootJjjProperties.getServerIp(), productId, user.getUserId(), user.getAppId());
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 300, 300);
                MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig();
                BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);
                g.drawImage(qrCodeImage.getScaledInstance(140, 140, Image.SCALE_DEFAULT), 570, 914, null);
            }
            //商品
            Product product = productService.getById(productId);
            String imagePath = uploadFileUtils.getImagePathByProductId(productId);
            // 商品图片
            BufferedImage productImage = ImageIO.read(new URL(imagePath));
            g.drawImage(productImage.getScaledInstance(690, 690, Image.SCALE_DEFAULT), 30, 30, null);

            // 商品名称处理换行
            Font productNameFont = fontUtils.loadFont(40);
            g.setFont(productNameFont);
            FontDesignMetrics metrics = FontDesignMetrics.getMetrics(productNameFont);
            String productName = wrapText(product.getProductName(), metrics, 680);
            // 写入商品名称
            g.setColor(new Color(51, 51, 51));
            // 拆分行
            String[] zhWraps = productName.split("\n");
            // 将每一行在海报背景上打印
            g.drawString(zhWraps[0], 30, 770);
            if(zhWraps.length > 1){
                g.drawString(zhWraps[1], 30, 840);
            }
            // 写入商品价格
            g.setColor(new Color(255, 68, 68));
            g.setFont(fontUtils.loadFont(48));
            g.drawString(product.getProductPrice().toString(), 62, 1000);

            g.dispose();
            // 文件保存目录
            File saveDir = new File(this.getPosterPath(user, product, source));
            // 判断目录是否存在，不存在，则创建，如创建失败，则抛出异常
            if (!saveDir.exists()) {
                boolean flag = saveDir.mkdirs();
                if (!flag) {
                    throw new RuntimeException("创建" + saveDir + "目录失败！");
                }
            }

            File saveFile = new File(saveDir, this.getPosterName(user, productId));
            // 保存文件到服务器指定路径
            ImageIO.write(canvas, "png", saveFile);
            return this.getPosterUrl(product, user , source);
        }catch (Exception e){
            return null;
        }
    }

    private String wrapText(String zh, FontDesignMetrics metrics, int maxWidth) {
        StringBuilder sb = new StringBuilder();
        int lineWidth = 0;
        int lineCount = 0;
        for (int i = 0; i < zh.length(); i++) {
            char c = zh.charAt(i);
            sb.append(c);
            // FontDesignMetrics 的 charWidth() 方法可以计算字符的宽度
            int charWidth = metrics.charWidth(c);
            lineWidth += charWidth;
            // 如果当前字符的宽度加上之前字符串的已有宽度超出了海报的最大宽度，则换行
            if (lineWidth >= maxWidth - charWidth) {
                lineWidth = 0;
                sb.append("\n");
                if(lineCount >= 1){
                    break;
                }
                lineCount++;
            }
        }
        return sb.toString();
    }

    /**
     * 海报图文件路径
     */
    private String getPosterPath(User user, Product product, String source)
    {
        // 保存路径
        String tempPath = springBootJjjProperties.getUploadPath() + "/temp";
        return tempPath + "/product/" + product.getAppId() + "/" + source;
    }

    /**
     * 海报图文件名称
     */
    private String getPosterName(User user, Integer productId)
    {
        String userId = "";
        if(user != null){
            userId = "" + user.getUserId();
        }
        return "product_" + DigestUtils.md5Hex(userId+ "_" + productId) + ".png";
    }

    /**
     * 海报图url
     */
    private String getPosterUrl(Product product, User user, String source)
    {
        return springBootJjjProperties.getResourceAccessUrl() + "temp/product/" + product.getAppId() + "/" +source+ "/" + this.getPosterName(user, product.getProductId()) + "?t="+System.currentTimeMillis();
    }
}
