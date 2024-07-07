package cn.stylefeng.guns.modular.h5;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/visitor")
public class H5Controller
{

    @Value("${h5.prefix}")
    private String PREFIX ;

    //h5注册页
    @Value("${h5.reg}")
    private String reg;


    //h5用户协议
    @Value("${h5.agree}")
    private String agree;


    //h5下载页
    @Value("${h5.link}")
    private String link;

    @Value("${h5.url}")
    private String url;

    @Value("${h5.agreeApi}")
    private String agreeApi;

    @Value("${h5.linkApi}")
    private String linkApi;

    @Value("${h5.zhengCeApi}")
    private String zhengCeApi;

    /**
     * 注册页
     */
    @RequestMapping("/reg/{invite}")
    public String index(@PathVariable("invite") String invite, Model model)
    {
        model.addAttribute("invite", invite);
        model.addAttribute("url", url);
        model.addAttribute("agreeApi", agreeApi);
        model.addAttribute("zhengCeApi", zhengCeApi);
        model.addAttribute("linkApi", linkApi);
        return PREFIX + reg;
    }

    /**
     * 用户协议页
     */
    @RequestMapping("/reg/agree")
    public String agree(Model model)
    {
        model.addAttribute("url", url);
        return PREFIX + agree;
    }

    @RequestMapping("/reg/zhengce")
    public String zhengce(Model model){
        model.addAttribute("url", url);
        return PREFIX + "zhengce.html";
    }

    /**
     * APP下载链接
     */
    @RequestMapping("/reg/link")
    public String link(Model model)
    {

        model.addAttribute("url", url);
        return PREFIX + link;
    }

}
