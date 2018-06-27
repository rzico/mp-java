package net.wit.controller.website;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
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
import net.wit.plugin.StoragePlugin;
import net.wit.service.*;
import net.wit.ueditor.MyActionEnter;
import net.wit.util.ImageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

import static javax.imageio.ImageIO.createImageInputStream;


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
    public void qrcode(Long id,HttpServletRequest request,HttpServletResponse response) {
        try {
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            Member member = memberService.find(id);
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            String url = "";
            if (member!=null) {
                url = "https://"+bundle.getString("weixin.url") + "?xuid=" + member.getId();
            } else {
                url = "https://"+bundle.getString("weixin.url");
            }
            String tempFile = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID() + ".jpg";
            String srcFile = rootPath+"/upload/qrcode/default.jpg";
            String destFile = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID() + ".jpg";
            File srcfile = new File(srcFile);
            File destfile = new File(destFile);
            File file = new File(tempFile);

            try {
                int width=300;
                int hight=300;
                String format="jpg";
                HashMap hints=new HashMap();
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//纠错等级L,M,Q,H
                hints.put(EncodeHintType.MARGIN, 1); //边距
                BitMatrix bitMatrix=new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, hight, hints);
                OutputStream os = new FileOutputStream(file);
                MatrixToImageWriter.writeToPath(bitMatrix, format, os);

                ImageUtils.addWatermark(srcfile,destfile,file,580,1315,100);
            } catch (WriterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.reset();
            response.setContentType("image/jpeg;charset=utf-8");

            ServletOutputStream output = response.getOutputStream();// 得到输出流
            InputStream imageIn = new FileInputStream(destfile);
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