package net.jjjshop.common.util.poster;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.table.Table;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.service.product.ProductService;
import net.jjjshop.common.service.table.TableService;
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
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

/**
 * 桌码生成
 */
@Slf4j
@Component
public class TablePosterUtils {
    @Lazy
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Autowired
    private TableService tableService;
    @Lazy
    @Autowired
    private WxMaService wxMaService;
    @Lazy
    @Autowired
    private AppWxUtils appWxUtils;

    public void genePoster(Integer id, String source, HttpServletResponse response){
        Table table = tableService.getById(id);
        // 保存目录
        String savePath = this.getPosterPath(id, source, table.getAppId());
        File saveFile = new File(savePath, this.getPosterName(id, source));
        //创建输入流（读文件）输出流（写文件）
        BufferedInputStream bis = null;
        OutputStream os = null;
        InputStream inputStream = null;
        try{
            BufferedImage qrCodeImage = null;
            // 二维码
            if("wx".equals(source)){
                // 小程序码参数
                String scene = String.format("tableId:%s", id);
                File qrCode = wxMaService.switchoverTo(appWxUtils.getConfig(wxMaService, null)).getQrcodeService().createWxaCodeUnlimit(scene, "pages/product/list/store");
                qrCodeImage = ImageIO.read(qrCode);
            }else{
                // h5码
                String url = String.format("%s/h5/pages/product/list/store?tableId=%s&appId=%s",springBootJjjProperties.getServerIp(), id, table.getAppId());
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 300, 300);
                MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig();
                qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);
            }
            // 文件保存目录
            File saveDir = new File(savePath);
            // 判断目录是否存在，不存在，则创建，如创建失败，则抛出异常
            if (!saveDir.exists()) {
                boolean flag = saveDir.mkdirs();
                if (!flag) {
                    throw new RuntimeException("创建" + saveDir + "目录失败！");
                }
            }
            // 保存文件到服务器指定路径
            ImageIO.write(qrCodeImage, "png", saveFile);

            response.setContentType("application/force-download");//设置强制下载
            //设置文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + this.getPosterName(id, source));
            byte[] buff = new byte[1024];// 用来存储每次读取到的字节数组

            os = response.getOutputStream();
            inputStream = new FileInputStream(saveFile.getPath());
            bis = new BufferedInputStream(inputStream);
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        }catch (Exception e){
            log.info("邀请有礼二维码下载失败",e);
        }finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 海报图文件路径
     */
    private String getPosterPath(Integer id, String source, Integer appId)
    {
        // 保存路径
        String tempPath = springBootJjjProperties.getUploadPath() + "/temp";
        return tempPath + "/invite/" + appId + "/" + source;
    }

    /**
     * 海报图文件名称
     */
    private String getPosterName(Integer id, String source)
    {
        return "invite_" + DigestUtils.md5Hex(id+ "_" + source) + ".png";
    }
}
