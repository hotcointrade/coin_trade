package cn.stylefeng.guns.modular.com.controller;

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
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.com.service.ShippingService;
import cn.stylefeng.guns.modular.com.wrapper.ShippingWrapper;
import cn.stylefeng.guns.modular.com.entity.Shipping;
import cn.stylefeng.guns.modular.com.constant.ShippingMap;

/**
 * 收货地址控制器
 *
 * @author yaying.liu
 * @Date 2019-12-10 20:11:22
 */
@Controller
@RequestMapping("/shipping")
public class ShippingController extends BaseController {

    private String PREFIX = "/modular/com/shipping/";

     @Autowired
     private ShippingService shippingService;

    /**
     * 跳转到收货地址首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "shipping.html";
    }

    /**
     * 跳转到添加收货地址
     */
    @RequestMapping("/shipping_add")
    public String shippingAdd() {
        return PREFIX + "shipping_add.html";
    }

    /**
     * 跳转到修改收货地址
     */
    @RequestMapping("/shipping_edit")
    public String shippingEdit(Long shippingId, Model model) {
        Shipping condition = this.shippingService.getById(shippingId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "shipping_edit.html";
    }

    /**
     * 收货地址详情
     */
    @RequestMapping(value = "/detail/{shippingId}")
    @ResponseBody
    public Object detail(@PathVariable("shippingId") Long shippingId) {
        Shipping entity = shippingService.getById(shippingId);
      //  ShippingDto conditionDto = new ShippingDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询收货地址列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = shippingService.selectByCondition(condition);
        Page wrapped = new ShippingWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑收货地址
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "shippingId", dict = ShippingMap.class)
    @ResponseBody
    public ResponseData edit(Shipping shipping) {
        shippingService.updateById(shipping);
        return SUCCESS_TIP;
    }

    /**
     * 添加收货地址
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加收货地址", key = "name", dict = ShippingMap.class)
    @ResponseBody
    public ResponseData add(Shipping shipping, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (shipping == null) {
            return ResponseData.error("参数不能为空");
        }
        this.shippingService.addShipping(shipping);
        return SUCCESS_TIP;
    }

    /**
     * 删除收货地址
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除收货地址", key = "shippingId", dict = ShippingMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long shippingId) {
        if (ToolUtil.isEmpty(shippingId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.shippingService.deleteShipping(shippingId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{shippingId}")
    @ResponseBody
    public Object content(@PathVariable("shippingId") Long id) {
        Shipping shipping = shippingService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(shipping);
        return super.warpObject(new ShippingWrapper(stringObjectMap));
    }
}