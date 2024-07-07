package cn.stylefeng.guns.modular.fin.controller;

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
import cn.stylefeng.guns.modular.fin.service.PayOrderService;
import cn.stylefeng.guns.modular.fin.wrapper.PayOrderWrapper;
import cn.stylefeng.guns.modular.fin.entity.PayOrder;
import cn.stylefeng.guns.modular.fin.constant.PayOrderMap;

/**
 * 支付订单控制器
 *
 * @author yaying.liu
 * @Date 2019-12-23 14:14:16
 */
@Controller
@RequestMapping("/payOrder")
public class PayOrderController extends BaseController {

    private String PREFIX = "/modular/fin/payOrder/";

     @Autowired
     private PayOrderService payOrderService;

    /**
     * 跳转到支付订单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "payOrder.html";
    }

    /**
     * 跳转到添加支付订单
     */
    @RequestMapping("/payOrder_add")
    public String payOrderAdd() {
        return PREFIX + "payOrder_add.html";
    }

    /**
     * 跳转到修改支付订单
     */
    @RequestMapping("/payOrder_edit")
    public String payOrderEdit(Long payOrderId, Model model) {
        PayOrder condition = this.payOrderService.getById(payOrderId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "payOrder_edit.html";
    }

    /**
     * 支付订单详情
     */
    @RequestMapping(value = "/detail/{payOrderId}")
    @ResponseBody
    public Object detail(@PathVariable("payOrderId") Long payOrderId) {
        PayOrder entity = payOrderService.getById(payOrderId);
      //  PayOrderDto conditionDto = new PayOrderDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询支付订单列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = payOrderService.selectByCondition(condition);
        Page wrapped = new PayOrderWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑支付订单
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "payOrderId", dict = PayOrderMap.class)
    @ResponseBody
    public ResponseData edit(PayOrder payOrder) {
        payOrderService.updateById(payOrder);
        return SUCCESS_TIP;
    }

    /**
     * 添加支付订单
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加支付订单", key = "name", dict = PayOrderMap.class)
    @ResponseBody
    public ResponseData add(PayOrder payOrder, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (payOrder == null) {
            return ResponseData.error("参数不能为空");
        }
        this.payOrderService.addPayOrder(payOrder);
        return SUCCESS_TIP;
    }

    /**
     * 删除支付订单
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除支付订单", key = "payOrderId", dict = PayOrderMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long payOrderId) {
        if (ToolUtil.isEmpty(payOrderId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.payOrderService.deletePayOrder(payOrderId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{payOrderId}")
    @ResponseBody
    public Object content(@PathVariable("payOrderId") Long id) {
        PayOrder payOrder = payOrderService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(payOrder);
        return super.warpObject(new PayOrderWrapper(stringObjectMap));
    }
}