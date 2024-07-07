package cn.stylefeng.guns.modular.bulletin.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HtmlUtil;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;

import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.app.service.AppCarouselService;
import cn.stylefeng.guns.modular.bulletin.entity.AppCarousel;
import cn.stylefeng.guns.modular.bulletin.model.AppCarouselDto;
import cn.stylefeng.guns.modular.bulletin.wrapper.CarouselWrapper;
import cn.stylefeng.guns.modular.com.entity.Business;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * 轮播图控制
 */
@Controller
@RequestMapping({"/bulletin","/running"})
public class CarouselBackController extends BaseController {


    private static String PREFIX = "/modular/bulletin/carousel/";

    @Autowired
    private AppCarouselService bulletinService;

    /**
     * 跳转到管理的首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bulletin.html";
    }


    /**
     * 跳转到添加公告
     */
    @RequestMapping("/bul_add")
    public String bulAdd() {
        return PREFIX + "bul_add.html";
    }

    /**
     * 跳转到编辑公告
     */
    @RequestMapping("/bul_edit")
    public String bulEdit(Long carouselId, Model model) {
        AppCarousel condition = bulletinService.getById(carouselId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "bul_edit.html";
    }



    /**
     * 查询公告列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String title) {

        //根据条件查询
        Page<Map<String, Object>> result = bulletinService.selectByCondition(title);
        Page wrapped = new CarouselWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);


    }

    /**
     * 查询公告内容详情
     */
    @RequestMapping("/content/{carouselId}")
    @ResponseBody
    public Object content(@PathVariable("carouselId") Long id) {
        AppCarousel bulletin = bulletinService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(bulletin);
        return super.warpObject(new CarouselWrapper(stringObjectMap));
    }

    /**
     * 轮播图所有数据详情
     */
    @RequestMapping(value = "/detail/{carouselId}")
    @ResponseBody
    public Object detail(@PathVariable("carouselId") Long contactId) {
        AppCarousel condition = bulletinService.getById(contactId);
//        AppCarouselDto conditionDto = new AppCarouselDto();
//        BeanUtil.copyProperties(condition, conditionDto);
        return condition;
    }


    @RequestMapping("/article/carouse/{carouselId}")
    public String article(@PathVariable("carouselId") Long contactId, Model model){
        AppCarousel condition = bulletinService.getById(contactId);
        model.addAttribute("content",condition.getContent());
        return "/modular/bulletin/article.html";
    }



    /**
     * 添加轮播图
     */
    @RequestMapping("/add")
//    @BussinessLog(value = "添加公告", key = "bulletin", dict = BulletinMap.class)
    @ResponseBody
    public ResponseData add(AppCarousel bulletin) {
        if (bulletin == null) {
            return ResponseData.error("公告不能为空");
        }

//        //对传入数据进行反转义处理
//        String tempContent = bulletin.getContent().replace("& ", "&");
//        tempContent = HtmlUtil.unescape(tempContent);
//        bulletin.setContent(tempContent);
//        bulletin.setImg(bulletin.getImg());
        this.bulletinService.save(bulletin);
        return SUCCESS_TIP;
    }


    /**
     * 关闭公告
     */
    @RequestMapping("/close")
//    @BussinessLog(value = "关闭公告", key = "bulletinId", dict = BulletinMap.class)
    @ResponseBody
    public ResponseData close(@RequestParam Long bulletinId) {
        if (ToolUtil.isEmpty(bulletinId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
//        this.bulletinService.closeBulletin(bulletinId);
        return SUCCESS_TIP;
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
//    @BussinessLog(value = "编辑公告", key = "bulletinId", dict = BulletinMap.class)
    @ResponseBody
    public ResponseData edit(AppCarousel bulletin) {
//        AppCarousel oldBulletin = bulletinService.getById(bulletin.getCarouselId());
//        oldBulletin.setTitle(bulletin.getTitle());
//        //对传入数据进行反转义处理
//        String tempContent = bulletin.getContent().replace("& ", "&");
//        tempContent = HtmlUtil.unescape(tempContent);
//        oldBulletin.setContent(tempContent);
//        oldBulletin.setImg(bulletin.getImg());
//        oldBulletin.setLink(bulletin.getLink());
        bulletinService.updateById(bulletin);
        return SUCCESS_TIP;
    }

    /**
     * 删除轮播图
     */
    @RequestMapping("/delete")
//    @BussinessLog(value = "删除轮播图", key = "carouselId", dict = CarouselMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long carouselId) {
        if (ToolUtil.isEmpty(carouselId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        AppCarousel carousel = this.bulletinService.getById(carouselId);
        carousel.setDel("Y");
        bulletinService.updateById(carousel);
        return SUCCESS_TIP;
    }
}
