package cn.stylefeng.guns.modular.bulletin.controller;

import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.bulletin.model.JsonResult;
import cn.stylefeng.guns.modular.bulletin.model.UploadFileDto;
import cn.stylefeng.guns.modular.meta_data.constant.factory.MetaDataFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

@RestController
@RequestMapping(value = {"/upload","/api/upload"})
public class UploadFileController {

    /**
     * 文件保存目录，物理路径
     */
    public final String rootPath =MetaDataFactory.me().getValueByCode("UPLOAD_PATH");

    public final String allowSuffix = ".bmp.jpg.jpeg.png.gif.pdf.doc.zip.rar.gz.mp4.wav.avi.mp3.BMP.JPG.JPEG.PNG.GIF.PDF.DOC.ZIP.RAR.GZ.MP4.WAV.AVI.MP3";

    /**
     * 上传文件 APP 端
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/img", method = RequestMethod.POST)
    public Result uploadFile(@RequestParam("file") MultipartFile file) {

        //1.文件后缀过滤，只允许部分后缀
        //文件的完整名称,如spring.jpeg
        String filename = file.getOriginalFilename();
        //文件名,如spring
        String name = filename.substring(0, filename.indexOf("."));
        //文件后缀,如.jpeg
        String suffix = filename.substring(filename.lastIndexOf("."));

        if (allowSuffix.indexOf(suffix) == -1) {
            return Result.fail(500,"不允许上传该后缀的文件");
        }


        //2.创建文件目录
        //创建年月文件夹
        Calendar date = Calendar.getInstance();
        File dateDirs = new File(date.get(Calendar.YEAR)+
                "-"+ (date.get(Calendar.MONTH) + 1));

        //目标文件
        File descFile = new File(rootPath + File.separator + dateDirs + File.separator + filename);
        int i = 1;
        //若文件存在重命名
        String newFilename = filename;
        while (descFile.exists()) {
            newFilename = UUID.randomUUID().toString().substring(3,20)+suffix;
            String parentPath = descFile.getParent();
            descFile = new File(parentPath + File.separator + newFilename);
            i++;
        }
        //判断目标文件所在的目录是否存在
        if (!descFile.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            descFile.getParentFile().mkdirs();
        }

        //3.存储文件
        //将内存中的数据写入磁盘
        try {
            file.transferTo(descFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //完整的url
        String fileUrl = "/uploads/" + dateDirs + "/" + newFilename;

        //4.返回URL
        UploadFileDto uploadFileDto = new UploadFileDto();
        uploadFileDto.setTitle(filename);
        uploadFileDto.setSrc(fileUrl);
        return Result.success("上传成功",200,uploadFileDto);
    }

    /**
     * 上传文件 PC 端
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public JsonResult uploadFile2(@RequestParam("file") MultipartFile file) {

        //1.文件后缀过滤，只允许部分后缀
        //文件的完整名称,如spring.jpeg
        String filename = file.getOriginalFilename();
        //文件名,如spring
        String name = filename.substring(0, filename.indexOf("."));
        //文件后缀,如.jpeg
        String suffix = filename.substring(filename.lastIndexOf("."));

        if (allowSuffix.indexOf(suffix) == -1) {
            return new JsonResult().fail("不允许上传该后缀的文件！");
        }


        //2.创建文件目录
        //创建年月文件夹
        Calendar date = Calendar.getInstance();
//        File dateDirs = new File(date.get(Calendar.YEAR)
//                + File.separator + (date.get(Calendar.MONTH) + 1));
        File dateDirs = new File(date.get(Calendar.YEAR)+
                "-"+ (date.get(Calendar.MONTH) + 1));
        //目标文件
        File descFile = new File(rootPath + File.separator + dateDirs + File.separator + filename);
        int i = 1;
        //若文件存在重命名
        String newFilename = filename;
//        while (descFile.exists()) {
        newFilename = UUID.randomUUID().toString().substring(3,20)+suffix;
        String parentPath = descFile.getParent();
        descFile = new File(parentPath + File.separator + newFilename);
        i++;
//        }
        String flag = "父目录存在："+parentPath + File.separator + newFilename;
        //判断目标文件所在的目录是否存在
        if (!descFile.getParentFile().exists()) {

            //如果目标文件所在的目录不存在，则创建父目录
            boolean f = descFile.getParentFile().mkdirs();
            flag="父目录不存在，创建父目录："+descFile.getParentFile().getPath()+"创建父目录"+f;
        }

        //3.存储文件
        //将内存中的数据写入磁盘
        try {
            descFile = new File(parentPath + File.separator + newFilename);
            file.transferTo(descFile);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult().fail(flag+e.getMessage());
        }
        //完整的url
        String fileUrl = F.me().getSysConfigValueByCode("URL")+ "/uploads/" + dateDirs + "/" + newFilename;

        //4.返回URL
        UploadFileDto uploadFileDto = new UploadFileDto();
        uploadFileDto.setTitle(filename);
        uploadFileDto.setSrc(fileUrl);
        return new JsonResult().ok(uploadFileDto);
    }


    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/apkUpload", method = RequestMethod.POST)
    public JsonResult apkUpload(@RequestParam("file") MultipartFile file) {

        //1.文件后缀过滤，只允许部分后缀
        //文件的完整名称,如spring.jpeg
        String filename = file.getOriginalFilename();

        //目标文件
//      File descFile = new File("/www/server/cayman/apk/ios/ios.ipa");
        File descFile = new File(F.me().getSysConfigValueByCode("UPLOAD_PATH")+"android.apk");
//      File descFile = new File("D:/ios.ipa");

        //3.存储文件
        //将内存中的数据写入磁盘
        try {
            file.transferTo(descFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //4.返回URL
        UploadFileDto uploadFileDto = new UploadFileDto();
        uploadFileDto.setTitle(filename);
        uploadFileDto.setSrc(filename);
        return new JsonResult().ok(uploadFileDto);
    }

  /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/iosUpload", method = RequestMethod.POST)
    public JsonResult iosUpload(@RequestParam("file") MultipartFile file) {

        //1.文件后缀过滤，只允许部分后缀
        //文件的完整名称,如spring.jpeg
        String filename = file.getOriginalFilename();

        //目标文件
        File descFile = new File(F.me().getSysConfigValueByCode("UPLOAD_PATH")+"ios.ipa");
//        File descFile = new File(PromotionFactory.me().getSysConfigValueByCode("UPLOAD_PATH")+"android.apk");
//        File descFile = new File("D:/ios.ipa");

        //3.存储文件
        //将内存中的数据写入磁盘
        try {
            file.transferTo(descFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //4.返回URL
        UploadFileDto uploadFileDto = new UploadFileDto();
        uploadFileDto.setTitle(filename);
        uploadFileDto.setSrc(filename);
        return new JsonResult().ok(uploadFileDto);
    }


}
