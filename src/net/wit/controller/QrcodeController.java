
package net.wit.controller;

import net.wit.Message;
import net.wit.entity.Payment;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.MemberService;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;
import net.wit.service.RSAService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Controller - 二维码
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("qrcodeController")
@RequestMapping("/q")
public class QrcodeController extends BaseController {

    /**
     * 生成二维码
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
     public Boolean qrcode(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) {
//        try {
//            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
//            String url =bundle.getString("app.url") + "/q/"+id+".jhtml";
//            String tempFile = System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".jpg";
//            response.reset();
//            response.setContentType("image/jpeg;charset=utf-8");
//            try {
//                QRBarCodeUtil.encodeQRCode(url, tempFile, 400, 400);
//            } catch (WriterException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            ServletOutputStream output = response.getOutputStream();// 得到输出流
//            InputStream imageIn = new FileInputStream(new File(tempFile));
//            // 得到输入的编码器，将文件流进行jpg格式编码
//            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
//            // 得到编码后的图片对象
//            BufferedImage image = decoder.decodeAsBufferedImage();
//            // 得到输出的编码器
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
//            encoder.encode(image);// 对图片进行输出编码
//            imageIn.close();// 关闭文件流
//            output.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return false;
     }


}