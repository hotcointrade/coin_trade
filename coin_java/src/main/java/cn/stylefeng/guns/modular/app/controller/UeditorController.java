package cn.stylefeng.guns.modular.app.controller;

import cn.stylefeng.guns.modular.app.service.FileUploadService;
import cn.stylefeng.guns.modular.base.state.F;
import com.baidu.ueditor.ActionEnter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;

@Controller
@RequestMapping("/ueditor")
public class UeditorController {


    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 执行单个文件上传操作
     * @param upfile 文件数据
     * @return 响应json
     */
    @RequestMapping(value="/config", method = RequestMethod.POST)
    @ResponseBody
    public Object action_upload(@RequestParam MultipartFile upfile,HttpServletRequest request, HttpServletResponse response){

        //System.out.println("upfile: " + upfile.getOriginalFilename());

        String filename = upfile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String boole = fileUploadService.saveFile(upfile, uuid);
        HashMap<String, String> map = new HashMap<>();
        if (boole !=null) {
            map.put("state", "SUCCESS");
            map.put("url", F.me().getSysConfigValueByCode("URL")+boole);
            map.put("title", filename);
            map.put("original", filename);
        } else {
            map.put("state", "FAIL");
            map.put("url","");
            map.put("title", filename);
            map.put("original", filename);
        }
        return map;
    }
    @RequestMapping(value="/config", method = RequestMethod.GET)
    public String config(@RequestParam String action, HttpServletRequest request, HttpServletResponse response) {
        //System.out.println("action: " + action);
        switch (action){
            //执行上传图片或截图的action名称
            case "uploadimage":
                break;
            //执行上传涂鸦的action名称
            case "uploadscrawl":
                break;
            //执行上传视频的action名称
            case "uploadvideo":
                break;
            //controller里,执行上传视频的action名称
            case "uploadfile":
                break;
            //执行抓取远程图片的action名称
            case "catchimage":
                break;
            //执行列出图片的action名称
            case "listimage":
                break;
            //执行列出文件的action名称
            case "listfile":
                break;
            default:
                break;
        }
        return "redirect:/assets/ueditor/jsp/config.json";
    }

}
