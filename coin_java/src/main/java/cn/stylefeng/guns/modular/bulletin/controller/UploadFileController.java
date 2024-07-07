package cn.stylefeng.guns.modular.bulletin.controller;

import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.bulletin.model.JsonResult;
import cn.stylefeng.guns.modular.bulletin.model.UploadFileDto;
import cn.stylefeng.guns.modular.metaData.constant.factory.MetaDataFactory;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(value = {"/upload","/api/upload"})
public class UploadFileController {
    
    
    @Autowired
    private FileStorageService fileStorageService;//注入实列
    
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
        //文件后缀,如.jpeg
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (!allowSuffix.contains(suffix)) {
            return Result.fail(500,"不允许上传该后缀的文件");
        }
        FileInfo upload = fileStorageService.of(file)
                .upload();
        UploadFileDto uploadFileDto = new UploadFileDto();
        uploadFileDto.setTitle(upload.getFilename());
        uploadFileDto.setSrc(upload.getUrl());
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
        //文件后缀,如.jpeg
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (!allowSuffix.contains(suffix)) {
            return new JsonResult().fail("不允许上传该后缀的文件");
        }
        FileInfo upload = fileStorageService.of(file)
                .upload();
        UploadFileDto uploadFileDto = new UploadFileDto();
        uploadFileDto.setTitle(upload.getFilename());
        uploadFileDto.setSrc(upload.getUrl());
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
        FileInfo upload = fileStorageService.of(file)
                .setSaveFilename(file.getOriginalFilename())
                .upload();
        UploadFileDto uploadFileDto = new UploadFileDto();
        uploadFileDto.setTitle(upload.getFilename());
        uploadFileDto.setSrc(upload.getUrl());
        return new JsonResult().ok(uploadFileDto);
        
        
//        //1.文件后缀过滤，只允许部分后缀
//        //文件的完整名称,如spring.jpeg
//        String filename = file.getOriginalFilename();
//
//        //目标文件
////      File descFile = new File("/www/server/cayman/apk/ios/ios.ipa");
//        File descFile = new File(F.me().getSysConfigValueByCode("UPLOAD_PATH")+"android.apk");
////      File descFile = new File("D:/ios.ipa");
//
//        //3.存储文件
//        //将内存中的数据写入磁盘
//        try {
//            file.transferTo(descFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //4.返回URL
//        UploadFileDto uploadFileDto = new UploadFileDto();
//        uploadFileDto.setTitle(filename);
//        uploadFileDto.setSrc(filename);
//        return new JsonResult().ok(uploadFileDto);
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
