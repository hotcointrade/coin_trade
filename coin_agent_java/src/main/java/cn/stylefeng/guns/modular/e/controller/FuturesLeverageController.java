package cn.stylefeng.guns.modular.e.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.e.constant.LeverageMap;
import cn.stylefeng.guns.modular.e.entity.FuturesLeverage;
import cn.stylefeng.guns.modular.e.entity.Leverage;
import cn.stylefeng.guns.modular.e.service.FuturesLeverageService;
import cn.stylefeng.guns.modular.e.service.LeverageService;
import cn.stylefeng.guns.modular.e.wrapper.LeverageWrapper;
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
 * 杠杆倍率控制器
 *
 * @author yaying.liu
 * @Date 2020-03-23 09:19:33
 */
@Controller
@RequestMapping("/e_futures_leverage")
public class FuturesLeverageController extends BaseController {

    private String PREFIX = "/modular/e/futures/";

     @Autowired
     private FuturesLeverageService leverageService;

    /**
     * 跳转到杠杆倍率首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "leverage.html";
    }

    /**
     * 跳转到添加杠杆倍率
     */
    @RequestMapping("/leverage_add")
    public String leverageAdd() {
        return PREFIX + "leverage_add.html";
    }

    /**
     * 跳转到修改杠杆倍率
     */
    @RequestMapping("/leverage_edit")
    public String leverageEdit(Long leverageId, Model model) {
        FuturesLeverage condition = this.leverageService.getById(leverageId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "leverage_edit.html";
    }

    /**
     * 杠杆倍率详情
     */
    @RequestMapping(value = "/detail/{leverageId}")
    @ResponseBody
    public Object detail(@PathVariable("leverageId") Long leverageId) {
        FuturesLeverage entity = leverageService.getById(leverageId);
      //  LeverageDto conditionDto = new LeverageDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询杠杆倍率列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = leverageService.selectByCondition(condition);
        Page wrapped = new LeverageWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑杠杆倍率
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "leverageId", dict = LeverageMap.class)
    @ResponseBody
    public ResponseData edit(FuturesLeverage leverage) {
        leverageService.updateById(leverage);
        return SUCCESS_TIP;
    }

    /**
     * 添加杠杆倍率
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加杠杆倍率", key = "name", dict = LeverageMap.class)
    @ResponseBody
    public ResponseData add(FuturesLeverage leverage, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (leverage == null) {
            return ResponseData.error("参数不能为空");
        }
        this.leverageService.addLeverage(leverage);
        return SUCCESS_TIP;
    }

    /**
     * 删除杠杆倍率
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除杠杆倍率", key = "leverageId", dict = LeverageMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long leverageId) {
        if (ToolUtil.isEmpty(leverageId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.leverageService.deleteLeverage(leverageId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{leverageId}")
    @ResponseBody
    public Object content(@PathVariable("leverageId") Long id) {
        FuturesLeverage leverage = leverageService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(leverage);
        return super.warpObject(new LeverageWrapper(stringObjectMap));
    }
}