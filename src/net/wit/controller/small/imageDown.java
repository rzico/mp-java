package net.wit.controller.small;

import org.apache.http.HttpStatus;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by Eric on 2018/4/24.
 */
//@WebServlet("/downImage")
//public class imageDown extends HttpServlet {
public class imageDown {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //开始计时
        Long start = System.currentTimeMillis();
        String path = request.getRealPath("/upload") + "/";
        imageChange image = new imageChange();
        try{
            //处理第一张图片
            File one=new File(path+"one.jpg");
            BufferedImage oneimg= ImageIO.read(one);
            //阈值二分
            oneimg=image.binary(oneimg);
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
            File two=new File(path+"two.jpg");
            BufferedImage twoimg= ImageIO.read(two);
            //阈值二分
            twoimg=image.binary(twoimg);
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
            newone=image.paste(newone,oneqc,newtwo);

            //生成图片副本
            File newfile = new File(path+"three.jpg");
            ImageIO.write(newone, "jpg", newfile);

//            PrintWriter printWriter = response.getWriter();

            DataInputStream in = new DataInputStream(new FileInputStream(newfile));
            OutputStream out = response.getOutputStream();
            int length = 0;
            byte[] buf = new byte[1024];
            // in.read(buf) 每次读到的数据存放在buf 数组中
            while ((length = in.read(buf)) != -1) {
                out.write(buf,0,length);
            }
            in.close();
            out.close();
            out.flush();
        }catch (Exception e){
            System.out.println(e);
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
