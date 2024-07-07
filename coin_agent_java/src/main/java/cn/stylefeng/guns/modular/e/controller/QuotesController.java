package cn.stylefeng.guns.modular.e.controller;

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
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.Const;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Date;
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.e.service.QuotesService;
import cn.stylefeng.guns.modular.e.wrapper.QuotesWrapper;
import cn.stylefeng.guns.modular.e.entity.Quotes;
import cn.stylefeng.guns.modular.e.constant.QuotesMap;

/**
 * 行情涨跌控制控制器
 *
 * @author yaying.liu
 * @Date 2020-03-23 11:44:38
 */
@Controller
@RequestMapping("/quotes")
public class QuotesController extends BaseController {

    private String PREFIX = "/modular/e/quotes/";

     @Autowired
     private QuotesService quotesService;

    /**
     * 跳转到行情涨跌控制首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "quotes.html";
    }

    /**
     * 跳转到添加行情涨跌控制
     */
    @RequestMapping("/quotes_add")
    public String quotesAdd() {
        return PREFIX + "quotes_add.html";
    }

    /**
     * 跳转到修改行情涨跌控制
     */
    @RequestMapping("/quotes_edit")
    public String quotesEdit(Long quotesId, Model model) {
        Quotes condition = this.quotesService.getById(quotesId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "quotes_edit.html";
    }

    /**
     * 行情涨跌控制详情
     */
    @RequestMapping(value = "/detail/{quotesId}")
    @ResponseBody
    public Object detail(@PathVariable("quotesId") Long quotesId) {
        Quotes entity = quotesService.getById(quotesId);
      //  QuotesDto conditionDto = new QuotesDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }

/**
     * 修改状态
     */
    @RequestMapping(value = "/unfreeze")
    @ResponseBody
    public void unfreeze( Long quotesId) {
        Quotes entity = quotesService.getById(quotesId);
        if(entity==null){
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        entity.setStatus("");

        entity.setRemark(String.valueOf(new Date().getTime()));
        quotesService.updateById(entity);
      //  QuotesDto conditionDto = new QuotesDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;

    }
/**
     * 修改状态
     */
    @RequestMapping(value = "/freeze")
    @ResponseBody
    public void freeze( Long quotesId) {
        Quotes entity = quotesService.getById(quotesId);
        if(entity==null){
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        entity.setStatus("ENABLE");
        entity.setRemark(String.valueOf(new Date().getTime()));
        quotesService.updateById(entity);
      //  QuotesDto conditionDto = new QuotesDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;

    }


    /**
     * 查询行情涨跌控制列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = quotesService.selectByCondition(condition);
        Page wrapped = new QuotesWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑行情涨跌控制
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(Quotes quotes) {
        quotes.setRemark(String.valueOf(new Date().getTime()));
        quotesService.updateById(quotes);
        return SUCCESS_TIP;
    }

    /**
     * 添加行情涨跌控制
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加行情涨跌控制", key = "name", dict = QuotesMap.class)
    @ResponseBody
    public ResponseData add(Quotes quotes, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (quotes == null) {
            return ResponseData.error("参数不能为空");
        }
        this.quotesService.addQuotes(quotes);
        return SUCCESS_TIP;
    }

    /**
     * 删除行情涨跌控制
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除行情涨跌控制", key = "quotesId", dict = QuotesMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long quotesId) {
        if (ToolUtil.isEmpty(quotesId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.quotesService.deleteQuotes(quotesId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{quotesId}")
    @ResponseBody
    public Object content(@PathVariable("quotesId") Long id) {
        Quotes quotes = quotesService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(quotes);
        return super.warpObject(new QuotesWrapper(stringObjectMap));
    }
}