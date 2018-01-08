package net.wit.controller.website;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleRewardModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleReward;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * @ClassName: ArticleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websitePromoterController")
@RequestMapping("/website/promoter")
public class PromoterController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "articleRewardServiceImpl")
    private ArticleRewardService articleRewardService;

    /**
     * 推广二维码
     */
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    public void qrcode(HttpServletResponse response) {
        try {
            Member member = memberService.getCurrent();
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            String url = "http://"+bundle.getString("weixin.url") + "?xuid=" + member.getId();
            String tempFile = System.getProperty("java.io.tmpdir") + "/upload/" + UUID.randomUUID() + ".jpg";
            response.reset();
            response.setContentType("image/jpeg;charset=utf-8");
//            try {
//
//                int width=200;
//                int hight=200;
//                String format="png";
//                String content="www.baidu.com";
//                HashMap hints=new HashMap();
//                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//纠错等级L,M,Q,H
//                hints.put(EncodeHintType.MARGIN, 2); //边距
//                BitMatrix bitMatrix=new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, hight, hints);
//                Path file=new File("D:/download/imag.png").toPath();
//                MatrixToImageWriter.writeToPath(bitMatrix, format, file);
//
//                QRBarCodeUtil.encodeQRCode(url, tempFile, 200, 200);
//            } catch (WriterException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
            ServletOutputStream output = response.getOutputStream();// 得到输出流
            InputStream imageIn = new FileInputStream(new File(tempFile));
            // 得到输入的编码器，将文件流进行jpg格式编码
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
            // 得到编码后的图片对象
            BufferedImage image = decoder.decodeAsBufferedImage();
            // 得到输出的编码器
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
            encoder.encode(image);// 对图片进行输出编码
            imageIn.close();// 关闭文件流
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}