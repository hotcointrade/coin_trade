package cn.stylefeng.guns.modular.fin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.fin.constant.CurrencyMap;
import cn.stylefeng.guns.modular.fin.entity.Currency;
import cn.stylefeng.guns.modular.fin.entity.FinOption;
import cn.stylefeng.guns.modular.fin.service.CurrencyService;
import cn.stylefeng.guns.modular.fin.service.FinOptionService;
import cn.stylefeng.guns.modular.fin.wrapper.CurrencyWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 币币账户控制器
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:10:50
 */
@Controller
@RequestMapping("/finOption")
public class FinOptionController extends BaseController {

    private String PREFIX = "/modular/fin/option/";

     @Autowired
     //private CurrencyService currencyService;
     private FinOptionService finOptionService;

    /**
     * 跳转到币币账户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "currency.html";
    }

    /**
     * 跳转到添加币币账户
     */
    @RequestMapping("/currency_add")
    public String currencyAdd() {
        return PREFIX + "currency_add.html";
    }

    /**
     * 跳转到修改币币账户
     */
    @RequestMapping("/currency_edit")
    public String currencyEdit(Long currencyId, Model model) {
        FinOption condition = this.finOptionService.getById(currencyId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "currency_edit.html";
    }

    /**
     * 币币账户详情
     */
    @RequestMapping(value = "/detail/{currencyId}")
    @ResponseBody
    public Object detail(@PathVariable("currencyId") Long currencyId) {
        FinOption entity = finOptionService.getById(currencyId);
      //  CurrencyDto conditionDto = new CurrencyDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询币币账户列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        Page<Map<String, Object>> result = finOptionService.selectByCondition(condition,memberId,recommendIds);
        Page wrapped = new CurrencyWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑币币账户
     */
    @RequestMapping("/edit")
//    @BussinessLog(value = "编辑参数", key = "currencyId", dict = CurrencyMap.class)
    @ResponseBody
    public ResponseData edit(FinOption currency) {
        finOptionService.edit(currency);
        return SUCCESS_TIP;
    }

    /**
     * 添加币币账户
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加币币账户", key = "name", dict = CurrencyMap.class)
    @ResponseBody
    public ResponseData add(FinOption currency, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (currency == null) {
            return ResponseData.error("参数不能为空");
        }
        this.finOptionService.addCurrency(currency);
        return SUCCESS_TIP;
    }

    /**
     * 删除币币账户
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除币币账户", key = "currencyId", dict = CurrencyMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long currencyId) {
        if (ToolUtil.isEmpty(currencyId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.finOptionService.deleteCurrency(currencyId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{currencyId}")
    @ResponseBody
    public Object content(@PathVariable("currencyId") Long id) {
        FinOption currency = finOptionService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(currency);
        return super.warpObject(new CurrencyWrapper(stringObjectMap));
    }
}