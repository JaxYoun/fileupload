package com.yang.uploadfile.rest;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.Iterator;

@RestController
@RequestMapping("/file")
public class FileResource {

    @PostMapping("/uploadFile")
    public String uploadFile(MultipartFile multipartFile) {
        System.out.println(multipartFile.getContentType());
        System.out.println(multipartFile.getOriginalFilename());
        return "uploadFile";
    }

    @PostMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response) {
        String path = "F:\\pic\\1.jpg";
        int height = 50;
        int width = 40;


    }

    @GetMapping("/downloadFileFor")
    public void downloadFileFor(HttpServletResponse response) {
//        String path = "F:\\pic\\1.jpg";
//        String path = "F:\\pic\\text.txt";
//        String path = "F:\\pic\\nginx.zip";
        String path = "F:\\pic\\Kk.java";

        File file = new File(path);

        response.setCharacterEncoding("UTF-8");
        String name = file.getName();
        try {
            name = URLEncoder.encode(name, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + name);
            System.out.println(name);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");

        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            try {
                OutputStream outputStream = response.getOutputStream();
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = fileInputStream.read(b)) != -1) {
                    outputStream.write(b, 0, len);
                }
                outputStream.flush();
                outputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cut(int x, int y, int width, int height, String srcpath, String subpath) throws IOException {//裁剪方法
        FileInputStream is = null;
        ImageInputStream iis = null;
        try {
            is = new FileInputStream(srcpath); //读取原始图片
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName("jpg"); //ImageReader声称能够解码指定格式
            ImageReader reader = it.next();
            iis = ImageIO.createImageInputStream(is); //获取图片流
            reader.setInput(iis, true); //将iis标记为true（只向前搜索）意味着包含在输入源中的图像将只按顺序读取
            ImageReadParam param = reader.getDefaultReadParam(); //指定如何在输入时从 Java Image I/O框架的上下文中的流转换一幅图像或一组图像
            Rectangle rect = new Rectangle(x, y, width, height); //定义空间中的一个区域
            param.setSourceRegion(rect); //提供一个 BufferedImage，将其用作解码像素数据的目标。
            BufferedImage bi = reader.read(0, param); //读取索引imageIndex指定的对象
            ImageIO.write(bi, "jpg", new File(subpath)); //保存新图片
        } finally {
            if (is != null)
                is.close();
            if (iis != null)
                iis.close();
        }
    }

    /*public static void main(String[] args) throws Exception {
        FileResource pc = new FileResource();
        pc.cut(0, 0, 500, 500, "F:\\pic\\1.jpg", "F:\\pic\\11.jpg");
        System.out.println("ok");
    }*/

    public static void main(String[] args) {
        try {
            Thumbnails.of("F:\\pic\\1.jpg").scale(0.5D).toFile("F:\\pic\\half.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

}
