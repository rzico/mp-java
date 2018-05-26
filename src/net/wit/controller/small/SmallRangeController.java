package net.wit.controller.small;

import net.sf.json.JSONObject;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.BaseController;
import net.wit.entity.BaseEntity;
import net.wit.entity.Member;
import net.wit.entity.OperationRecord;
import net.wit.entity.SmallRange;
import net.wit.service.OperationRecordService;
import net.wit.service.SmallRangeService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Eric on 2018/4/30.
 */
@Controller("smallController")
@RequestMapping("/small/smallRange")
public class SmallRangeController extends BaseController {

    @Resource(name = "smallRangeServiceImpl")
    SmallRangeService smallRangeService;

    @Resource(name ="operationRecordServiceImpl")
    OperationRecordService operationRecordService;
    /**
     * 获取信息
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    public Message add(String nickName,
                    String avatarUrl,
                    String gender,
                    String city,
                    String province,
                    String country,
                    String language,
                    HttpServletResponse response,
                    HttpServletRequest request) {
        String s="";
        try {
            s=new String(nickName.getBytes("iso8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //筛选
        Pageable pageable=new Pageable();
        ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
        Filter nickNameFilter = new Filter("nickName", Filter.Operator.eq,s);
        filters.add(nickNameFilter);
        Page<SmallRange> page=smallRangeService.findPage(null,null,pageable);
        List<SmallRange> list= page.getContent();
        if(list.size()>=1){
            return Message.success(list.get(list.size()-1),"admin.save.success");
        }
        //添加
        SmallRange smallRange = new SmallRange();
        smallRange.setNickName(s);
        smallRange.setAvatarUrl(avatarUrl);
        if (gender != null) {
            if (gender.equals("0")) {
                smallRange.setGender(Member.Gender.secrecy);
            } else if (gender.equals("1")) {
                smallRange.setGender(Member.Gender.male);
            } else if (gender.equals("2")) {
                smallRange.setGender(Member.Gender.female);
            }
        }else{
            smallRange.setGender(null);
        }
        smallRange.setCity(city);
        smallRange.setProvince(province);
        smallRange.setCountry(country);
        smallRange.setLanguage(language);
        if (!isValid(smallRange, BaseEntity.Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            smallRangeService.save(smallRange);
            return Message.success(smallRange,"admin.save.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.save.error");
        }
    }


    @RequestMapping(value = "/upLodeChange", method = RequestMethod.POST)
    public void upload(String type,String name,String userId,String id, HttpServletRequest req,HttpServletResponse resp) throws IOException {
        System.out.println("name=" + name+"      userId=" + userId+"         id=" + id + "      tyoe="+type);
        String path = req.getRealPath("/upload") + "/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        System.out.println("path=" + path);
        req.setCharacterEncoding("utf-8");  //设置编码

        MultipartHttpServletRequest re =(MultipartHttpServletRequest)req;
        MultipartFile multipartFile =  re.getFile("file");

        try {

            File file  =  new File(path,userId+".jpg");
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
//        //获得磁盘文件条目工厂
//        DiskFileItemFactory factory = new DiskFileItemFactory();
//
//        //如果没以下两行设置的话,上传大的文件会占用很多内存，
//        //设置暂时存放的存储室,这个存储室可以和最终存储文件的目录不同
//        /**
//         * 原理: 它是先存到暂时存储室，然后再真正写到对应目录的硬盘上，
//         * 按理来说当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的
//         * 然后再将其真正写到对应目录的硬盘上
//         */
//        factory.setRepository(dir);
//        //设置缓存的大小，当上传文件的容量超过该缓存时，直接放到暂时存储室
//        factory.setSizeThreshold(1024 * 1024);
//        //高水平的API文件上传处理
//        ServletFileUpload upload = new ServletFileUpload(factory);
//        try {
//            List<FileItem> list = upload.parseRequest(req);
//            FileItem picture = null;
//            for (FileItem item : list) {
//                //获取表单的属性名字
//                String name = item.getFieldName();
//                //如果获取的表单信息是普通的 文本 信息
//                if (item.isFormField()) {
//                    //获取用户具体输入的字符串
//                    String value = item.getString();
//                    req.setAttribute(name, value);
//                    System.out.println("name=" + name + ",value=" + value);
//                } else {
//                    picture = item;
//                }
//            }
//
//            //自定义上传图片的名字为userId.jpg
//            String fileName = req.getAttribute("userId") + ".jpg";
//            String destPath = path + fileName;
//            System.out.println("destPath=" + destPath);
//
//            //真正写到磁盘上
//            File file = new File(destPath);
//            OutputStream out = new FileOutputStream(file);
//            InputStream in = picture.getInputStream();
//            int length = 0;
//            byte[] buf = new byte[1024];
//            // in.read(buf) 每次读到的数据存放在buf 数组中
//            while ((length = in.read(buf)) != -1) {
//                //在buf数组中取出数据写到（输出流）磁盘上
//                out.write(buf, 0, length);
//            }
//            in.close();
//            out.close();
//
//            Long num= (Long) req.getAttribute("id");
            OperationRecord operationRecord;
            if(type==null) {
                operationRecord=new OperationRecord();
                operationRecord.setOneImgUrl(path+userId+".jpg");

                operationRecordService.save(operationRecord);
                SmallRange smallRange=smallRangeService.find(Long.valueOf(id));
                List<OperationRecord> lists=smallRange.getOperationRecords();
                lists.add(operationRecord);
                smallRange.setOperationRecords(lists);
                smallRangeService.save(smallRange);
            }else{
                List<OperationRecord> lists=smallRangeService.find(Long.valueOf(id)).getOperationRecords();
                operationRecord=lists.get(lists.size()-1);
                operationRecord.setTwoImgUrl(path+userId+".jpg");
                operationRecordService.update(operationRecord);
            }
//        } catch (FileUploadException e1) {
//            System.out.println(e1);
//        } catch (Exception e) {
//            System.out.println(e);
//        }


        PrintWriter printWriter = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        HashMap<String, Object> res = new HashMap<String, Object>();
        res.put("success", true);
        printWriter.write(String.valueOf(JSONObject.fromObject(res)));
        printWriter.flush();
    }


    @RequestMapping(value = "/downImage", method = RequestMethod.GET)
    public void downImage(HttpServletResponse response, HttpServletRequest request) throws IOException {
        Long start = System.currentTimeMillis();
        String path = request.getRealPath("/upload") + "/";
        imageChange image = new imageChange();
        try {
            //处理第一张图片
            File one = new File(path + "one.jpg");
            BufferedImage oneimg = ImageIO.read(one);
            //阈值二分
            oneimg = image.binary(oneimg);
            //单倍腐蚀
            oneimg = image.corrosion(oneimg, 1);
            //膨胀
            oneimg = image.swell(oneimg, 1);
            //生成图片矩阵
            int[][] onemap = image.juzhen(oneimg);
            //生成图片轮廓
            int[][] oneline = image.outline(onemap);
            //获取二维码坐标
            java.util.List<XYWH> onecoor = image.recognition(oneline);
            //获取该二维码属性
            QCCode oneqc = image.calculate(onecoor.get(0), onecoor.get(1), onecoor.get(2));

            //处理第二张图片
            File two = new File(path + "two.jpg");
            BufferedImage twoimg = ImageIO.read(two);
            //阈值二分
            twoimg = image.binary(twoimg);
            //单倍腐蚀
            twoimg = image.corrosion(twoimg, 1);
            //膨胀
            twoimg = image.swell(twoimg, 1);
            //生成图片矩阵
            int[][] twomap = image.juzhen(twoimg);
            //生成图片轮廓
            int[][] twoline = image.outline(twomap);
            //获取二维码坐标
            java.util.List<XYWH> twocoor = image.recognition(twoline);
            //获取该二维码属性
            QCCode twoqc = image.calculate(twocoor.get(0), twocoor.get(1), twocoor.get(2));

            //清除第一张图片二维码
            BufferedImage newone = ImageIO.read(one);
            newone = image.remove(newone, oneqc);

            //二维码缩放
            BufferedImage newtwo = ImageIO.read(two);
            newtwo = image.change(newtwo, oneqc, twoqc);

            //二维码粘贴
            newone = image.paste(newone, oneqc, newtwo);

            //生成图片副本
            File newfile = new File(path + "three.jpg");
            ImageIO.write(newone, "jpg", newfile);

//            Long id= (Long) request.getAttribute("id");
//            OperationRecord operationRecord;
//
//                List<OperationRecord> lists=smallRangeService.find(id).getOperationRecords();
//                operationRecord=lists.get(lists.size()-1);
//                operationRecord.setTwoImgUrl(path + "three.jpg");
//                operationRecordService.update(operationRecord);
//            PrintWriter printWriter = response.getWriter();

            DataInputStream in = new DataInputStream(new FileInputStream(newfile));
            OutputStream out = response.getOutputStream();
            int length = 0;
            byte[] buf = new byte[1024];
            // in.read(buf) 每次读到的数据存放在buf 数组中
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            in.close();
            out.close();
            out.flush();
        } catch (Exception e) {
            System.out.println(e);
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
