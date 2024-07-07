package cn.stylefeng.guns.modular.app.controller;

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
import cn.stylefeng.guns.modular.app.service.PaymentService;
import cn.stylefeng.guns.modular.app.wrapper.PaymentWrapper;
import cn.stylefeng.guns.modular.app.entity.Payment;
import cn.stylefeng.guns.modular.app.constant.PaymentMap;

/**
 * 收款方式控制器
 *
 * @author yaying.liu
 * @Date 2020-03-12 11:23:03
 */
@Controller
@RequestMapping("/payment")
public class PaymentController extends BaseController {

    private String PREFIX = "/modular/app/payment/";

     @Autowired
     private PaymentService paymentService;

    /**
     * 跳转到收款方式首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "payment.html";
    }

    /**
     * 跳转到添加收款方式
     */
    @RequestMapping("/payment_add")
    public String paymentAdd() {
        return PREFIX + "payment_add.html";
    }

    /**
     * 跳转到修改收款方式
     */
    @RequestMapping("/payment_edit")
    public String paymentEdit(Long paymentId, Model model) {
        Payment condition = this.paymentService.getById(paymentId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "payment_edit.html";
    }

    /**
     * 收款方式详情
     */
    @RequestMapping(value = "/detail/{paymentId}")
    @ResponseBody
    public Object detail(@PathVariable("paymentId") Long paymentId) {
        Payment entity = paymentService.getById(paymentId);
      //  PaymentDto conditionDto = new PaymentDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询收款方式列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = paymentService.selectByCondition(condition);
        Page wrapped = new PaymentWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑收款方式
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "paymentId", dict = PaymentMap.class)
    @ResponseBody
    public ResponseData edit(Payment payment) {
        paymentService.updateById(payment);
        return SUCCESS_TIP;
    }

    /**
     * 添加收款方式
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加收款方式", key = "name", dict = PaymentMap.class)
    @ResponseBody
    public ResponseData add(Payment payment, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (payment == null) {
            return ResponseData.error("参数不能为空");
        }
        this.paymentService.addPayment(payment);
        return SUCCESS_TIP;
    }

    /**
     * 删除收款方式
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除收款方式", key = "paymentId", dict = PaymentMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long paymentId) {
        if (ToolUtil.isEmpty(paymentId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.paymentService.deletePayment(paymentId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{paymentId}")
    @ResponseBody
    public Object content(@PathVariable("paymentId") Long id) {
        Payment payment = paymentService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(payment);
        return super.warpObject(new PaymentWrapper(stringObjectMap));
    }
}