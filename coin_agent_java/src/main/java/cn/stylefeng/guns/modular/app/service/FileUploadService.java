package cn.stylefeng.guns.modular.app.service;

import cn.stylefeng.guns.modular.app.dto.ImgFileDto;
import cn.stylefeng.guns.modular.bulletin.controller.UploadFileController;
import cn.stylefeng.guns.modular.meta_data.constant.factory.MetaDataFactory;
import org.apache.tomcat.util.net.IPv6Utils;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

@Service
public class FileUploadService {
    public final String rootPath = MetaDataFactory.me().getValueByCode("UPLOAD_PATH");

    /**
     * 保存文件
     *
     * @param file 文件
     * @param uuid uuid
     * @return 保存成功返回true
     */
    public String saveFile(MultipartFile file, String uuid) {
        try {
            //2.创建文件目录
            //创建年月文件夹
            Calendar date = Calendar.getInstance();
            File dateDirs = new File(date.get(Calendar.YEAR)+
                    "-"+ (date.get(Calendar.MONTH) + 1));

            //目标文件
            File path = path(rootPath + File.separator + dateDirs + File.separator );


            String filename = file.getOriginalFilename();
            ImgFileDto fileEntity = new ImgFileDto();
            fileEntity.setFileName(filename);
            fileEntity.setUuid(uuid);
            String storeaddress = path.getAbsolutePath();
            fileEntity.setStoreaddress(storeaddress);
            File saveFile = new File(path, uuid+filename);
            try {
                //imgFileMapper.save(fileEntity);
                file.transferTo(saveFile);
                return  "/uploads/" + dateDirs + "/" + uuid+filename;
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件保存异常");
            return null;
        }
    }

    /**
     * 检验文件路径是否存在
     *
     * @param type 文件类型
     * @return
     */
    public File path(String type) {
        File path  = new File(type);
        if (!path.isDirectory()) {
            path.mkdir();
        }
        return path;
    }

    /**
     * 下载
     *
     * @param uuid
     * @param request
     * @param response
     */

    public void download(String uuid, HttpServletRequest request, HttpServletResponse response) {
//        ImgFileDto fileentity = imgFileMapper.findByUuid(uuid);
//        String filename = fileentity.getFileName();
//        filename = getStr(request, filename);
//        File file = new File(fileentity.getStoreaddress(), uuid);
//        if (file.exists()) {
//            FileInputStream fis;
//            try {
//                fis = new FileInputStream(file);
//                response.setContentType("application/x-msdownload");
//                response.addHeader("Content-Disposition", "attachment; filename=" + filename);
//                ServletOutputStream out = response.getOutputStream();
//                byte[] buf = new byte[2048];
//                int n = 0;
//                while ((n = fis.read(buf)) != -1) {
//                    out.write(buf, 0, n);
//                }
//                fis.close();
//                out.flush();
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }


    public String getStr(HttpServletRequest request, String fileName) {
        String downloadFileName = null;
        String agent = request.getHeader("USER-AGENT");
        try {
            if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
                //downloadFileName = "=?UTF-8?B?" + (new String(Base64Utils.encode(fileName.getBytes("UTF-8")))) + "?=";
                //设置字符集
                downloadFileName = "=?UTF-8?B?" + Base64Utils.encodeToString(fileName.getBytes("UTF-8")) + "?=";
            } else {
                downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return downloadFileName;
    }

}
