package cn.stylefeng.guns.modular.fin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.fin.entity.Periodic;
import cn.stylefeng.guns.modular.fin.entity.PeriodicOrder;
import cn.stylefeng.guns.modular.fin.service.FinPeriodicOrderService;
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
@RequestMapping("/periodicOrder")
public class FinPeriodicOrderController extends BaseController {

    private String PREFIX = "/modular/fin/periodicOrder/";

     @Autowired

     private FinPeriodicOrderService finPeriodicService;

    /**
     * 跳转到
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "currency.html";
    }

    /**
     * 跳转到
     */
    @RequestMapping("/periodic_add")
    public String currencyAdd() {
        return PREFIX + "currency_add.html";
    }

    /**
     * 跳转到
     */
    @RequestMapping("/periodic_edit")
    public String currencyEdit(Long currencyId, Model model) {
        PeriodicOrder condition = this.finPeriodicService.getById(currencyId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        model.addAttribute("pId",condition.getId().toString());
        LogObjectHolder.me().set(condition);
        return PREFIX + "currency_edit.html";
    }

    /**
     *
     */
    @RequestMapping(value = "/detail/{periodicId}")
    @ResponseBody
    public Object detail(@PathVariable("periodicId") Long periodicId) {
        PeriodicOrder entity = finPeriodicService.getById(periodicId);
      //  CurrencyDto conditionDto = new CurrencyDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询
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
        Page<Map<String, Object>> result = finPeriodicService.selectByCondition(condition);
        Page wrapped = new CurrencyWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
//    @BussinessLog(value = "编辑参数", key = "currencyId", dict = CurrencyMap.class)
    @ResponseBody
    public ResponseData edit(PeriodicOrder currency) {
        if(currency.getId() == null ){
            return ResponseData.error("参数不能为空");
        }
        PeriodicOrder periodic = finPeriodicService.getById(currency.getId());
        if(periodic!=null){
            finPeriodicService.updateById(currency);
        }else{
            return ResponseData.error("修改失败");
        }
        return SUCCESS_TIP;
    }

    /**
     * 添加
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加生息配置")
    @ResponseBody
    public ResponseData add(Periodic currency, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (currency == null) {
            return ResponseData.error("参数不能为空");
        }
        currency.setIsDelete("N");
        //this.finPeriodicService.addPeriodic(currency);
        return SUCCESS_TIP;
    }

    /**
     * 删除币币账户
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除")
    @ResponseBody
    public ResponseData delete(@RequestParam Long currencyId) {
        if (ToolUtil.isEmpty(currencyId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.finPeriodicService.deletePeriodic(currencyId);
        return SUCCESS_TIP;
    }
    /**
     * 提前结算
     */
    @RequestMapping("/advanceSettlement")
    @BussinessLog(value = "提前结算")
    @ResponseBody
    public ResponseData advanceSettlement(@RequestParam Long currencyId) {
        if (ToolUtil.isEmpty(currencyId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.finPeriodicService.advanceSettlement(currencyId);
        return SUCCESS_TIP;
    }
    /**
     * 撤销
     */
    @RequestMapping("/revoke")
    @BussinessLog(value = "撤销")
    @ResponseBody
    public ResponseData revoke(@RequestParam Long currencyId) {
        if (ToolUtil.isEmpty(currencyId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.finPeriodicService.revoke(currencyId);
        return SUCCESS_TIP;
    }


}
