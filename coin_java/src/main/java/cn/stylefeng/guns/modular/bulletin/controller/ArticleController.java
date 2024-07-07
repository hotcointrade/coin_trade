package cn.stylefeng.guns.modular.bulletin.controller;

import cn.hutool.http.HtmlUtil;
import cn.stylefeng.guns.modular.bulletin.constant.ArticleMap;
import cn.stylefeng.guns.modular.bulletin.entity.Article;
import cn.stylefeng.guns.modular.bulletin.model.ArticleDto;
import cn.stylefeng.guns.modular.bulletin.service.ArticleService;
import cn.stylefeng.guns.modular.bulletin.wrapper.ArticleWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;
import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;

/**
 * 简介文章控制器
 *
 * @author yaying.liu
 * @Date 2019-08-16 14:30:41
 */
@Controller
@RequestMapping("/article")
public class ArticleController extends BaseController {

    private String PREFIX = "/modular/bulletin/article/";

    @Autowired
    private ArticleService articleService;

    /**
     * 跳转到简介文章首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "article.html";
    }

    /**
     * 跳转到添加简介文章
     */
    @RequestMapping("/article_add")
    public String articleAdd() {
        return PREFIX + "article_add.html";
    }

    /**
     * 跳转到修改简介文章
     */
    @RequestMapping("/article_edit")
    public String articleEdit(Long articleId, Model model) {
        Article condition = this.articleService.getById(articleId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "article_edit.html";
    }

    /**
     * 简介文章详情
     */
    @RequestMapping(value = "/detail/{articleId}")
    @ResponseBody
    public Object detail(@PathVariable("articleId") Long articleId) {
        Article entity = articleService.getById(articleId);
        ArticleDto conditionDto = new ArticleDto();
        BeanUtil.copyProperties(entity, conditionDto);
        return conditionDto;
    }
    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{articleId}")
    @ResponseBody
    public Object content(@PathVariable("articleId") Long id) {
        Article bulletin = articleService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(bulletin);
        return super.warpObject(new ArticleWrapper(stringObjectMap));
    }


    /**
     * 查询简介文章列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = articleService.selectByCondition(condition);
        Page wrapped = new ArticleWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑简介文章
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(Article article) {

        Article oldBulletin = articleService.getById(article.getArticleId());
//        oldBulletin.setName(article.getName());
//        oldBulletin.setMainImg(article.getMainImg());
        //对传入数据进行反转义处理
        String tempContent = article.getContent().replace("& ", "&");
        tempContent = HtmlUtil.unescape(tempContent);
        oldBulletin.setContent(tempContent);
        articleService.updateById(oldBulletin);
        return SUCCESS_TIP;
    }

    /**
     * 添加简介文章
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseData add(Article article, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (article == null) {
            return ResponseData.error("参数不能为空");
        }

        //对传入数据进行反转义处理
        String tempContent = article.getContent().replace("& ", "&");
        tempContent = HtmlUtil.unescape(tempContent);
        article.setContent(tempContent);
        this.articleService.addArticle(article);
        return SUCCESS_TIP;
    }

    /**
     * 删除简介文章
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestParam Long articleId) {
        if (ToolUtil.isEmpty(articleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.articleService.deleteArticle(articleId);
        return SUCCESS_TIP;
    }

}