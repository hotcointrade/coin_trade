package cn.stylefeng.guns.modular.h5;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * H5 下载页 上传页
 */
@Controller
@RequestMapping("/api/personal/ios")
public class IosApi
{

    @Value("${h5.url}")
    private String url;

    @RequestMapping("")
    public String iosUpload(){
        return "/modular/com/ios/upload.html";
    }


    @RequestMapping("/reg/link")
    public String link(Model model) {
        model.addAttribute("url",url);
        return "/modular/com/h5_reg/exitLink.html";
    }

}
