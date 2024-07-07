package cn.stylefeng.guns.modular.bulletin.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HtmlUtil;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.bulletin.entity.News;
import cn.stylefeng.guns.modular.bulletin.model.AppCarouselDto;
import cn.stylefeng.guns.modular.bulletin.service.NewsService;
import cn.stylefeng.guns.modular.bulletin.wrapper.CarouselWrapper;
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

import java.util.Date;
import java.util.Map;


/**
 * 公告管理
 */
@Controller
@RequestMapping({"/news"})
public class NewsBackController extends BaseController {


    private static String PREFIX = "/modular/bulletin/news/";

    @Autowired
    private NewsService newsService;

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
    public String bulEdit() {
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
        Page<Map<String, Object>> result = newsService.selectByCondition(title);
        Page wrapped = new CarouselWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 修改状态
     * @param status
     * @param id
     * @return
     */
    @RequestMapping("/status/{status}")
    @ResponseBody
    public ResponseData status(@PathVariable("status") String status,@RequestParam Long id) {
        News entity = newsService.getById(id);
        entity.setStatus(status);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(Constant.SYS_PLATFORM);
        newsService.updateById(entity);
        return SUCCESS_TIP;
    }

    /**
     * 查询公告内容详情
     */
    @RequestMapping("/content/{carouselId}")
    @ResponseBody
    public Object content(@PathVariable("carouselId") Long id) {
        News bulletin = newsService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(bulletin);
        return super.warpObject(new CarouselWrapper(stringObjectMap));
    }

    /**
     * 公告所有数据详情
     */
    @RequestMapping(value = "/detail/{carouselId}")
    @ResponseBody
    public Object detail(@PathVariable("carouselId") Long contactId) {
        News condition = newsService.getById(contactId);
        AppCarouselDto conditionDto = new AppCarouselDto();
        BeanUtil.copyProperties(condition, conditionDto);
        return conditionDto;
    }


    @RequestMapping("/article/carouse/{carouselId}")
    public String article(@PathVariable("carouselId") Long contactId, Model model){
        News condition = newsService.getById(contactId);
        model.addAttribute("content",condition.getContent());
        return "/modular/bulletin/article.html";
    }



    /**
     * 添加公告
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseData add(News bulletin) {
        if (bulletin == null) {
            return ResponseData.error("公告不能为空");
        }

        //对传入数据进行反转义处理
        String tempContent = bulletin.getContent().replace("& ", "&");
        tempContent = HtmlUtil.unescape(tempContent);
        bulletin.setContent(tempContent);
        bulletin.setImg(bulletin.getImg());
        this.newsService.save(bulletin);
        return SUCCESS_TIP;
    }


    /**
     * 关闭公告
     */
    @RequestMapping("/close")
    @ResponseBody
    public ResponseData close(@RequestParam Long bulletinId) {
        if (ToolUtil.isEmpty(bulletinId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
//        this.newsService.closeBulletin(bulletinId);
        return SUCCESS_TIP;
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(News bulletin) {
        News oldBulletin = newsService.getById(bulletin.getCarouselId());
        oldBulletin.setTitle(bulletin.getTitle());
        if(bulletin.getContent()!=null)
        {
            //对传入数据进行反转义处理
            String tempContent = bulletin.getContent().replace("& ", "&");
            tempContent = HtmlUtil.unescape(tempContent);
            oldBulletin.setContent(tempContent);
        }
        oldBulletin.setCreateTime(bulletin.getCreateTime());
        oldBulletin.setUpdateUser(Constant.SYS_PLATFORM);
        oldBulletin.setUpdateTime(new Date());
        oldBulletin.setImg(bulletin.getImg());
        oldBulletin.setLanguage(bulletin.getLanguage());
        oldBulletin.setLink(bulletin.getLink());
        newsService.updateById(oldBulletin);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestParam Long carouselId) {
        if (ToolUtil.isEmpty(carouselId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        News carousel = this.newsService.getById(carouselId);
        newsService.removeById(carouselId);
        return SUCCESS_TIP;
    }
}
