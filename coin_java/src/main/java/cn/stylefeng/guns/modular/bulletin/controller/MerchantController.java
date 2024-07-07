package cn.stylefeng.guns.modular.bulletin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.bulletin.constant.MerchantMap;
import cn.stylefeng.guns.modular.bulletin.entity.Merchant;
import cn.stylefeng.guns.modular.bulletin.model.MerchantDto;
import cn.stylefeng.guns.modular.bulletin.service.MerchantService;
import cn.stylefeng.guns.modular.bulletin.wrapper.MerchantWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户控制器
 *
 * @author yaying.liu
 * @Date 2019-09-02 20:11:57
 */
@Controller
@RequestMapping(value = {"/merchant", "/merchant_info"})
public class MerchantController extends BaseController {

    private String PREFIX = "/modular/bulletin/merchant/";
    private String PREFIX_BACK = "/modular/bulletin/merchant_back/";

    @Autowired
    private MerchantService merchantService;

    /**
     * 跳转到商户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "merchant.html";
    }

    /**
     * 跳转到 商户后台-我的信息
     */
    @RequestMapping("/merchant_info")
    public String merchant_info() {
        return PREFIX_BACK + "merchant_info.html";
    }

    /**
     * 跳转到 商户后台-我的记录
     */
    @RequestMapping("/merchant_record")
    public String merchant_record() {
        return PREFIX_BACK + "merchant_record.html";
    }

    /**
     * 跳转到 商户后台-我的统计
     */
    @RequestMapping("/merchant_statistics")
    public String merchant_statistics() {
        return PREFIX_BACK + "merchant_statistics.html";
    }

      /**
     * 跳转到 商户后台-我要提现
     */
    @RequestMapping("/merchant_withdraw")
    public String merchant_withdraw() {
        return PREFIX_BACK + "merchant_withdraw.html";
    }



    /**
     * 跳转到添加商户
     */
    @RequestMapping("/merchant_add")
    public String merchantAdd() {
        return PREFIX + "merchant_add.html";
    }

    /**
     * 跳转到修改商户
     */
    @RequestMapping("/merchant_edit")
    public String merchantEdit(Long merchantId, Model model) {
        Merchant condition = this.merchantService.getById(merchantId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "merchant_edit.html";
    }

    /**
     * 商户详情
     */
    @RequestMapping(value = "/detail/{merchantId}")
    @ResponseBody
    public Object detail(@PathVariable("merchantId") Long merchantId) {
        Merchant entity = merchantService.getById(merchantId);
        MerchantDto conditionDto = new MerchantDto();
        BeanUtil.copyProperties(entity, conditionDto);
        return conditionDto;
    }


    /**
     * 查询商户列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = merchantService.selectByCondition(condition, null);
        Page wrapped = new MerchantWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }


    /**
     * 商户后台-  个人信息
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/info")
    @ResponseBody
    public Object info(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = merchantService.selectByCondition(condition, ShiroKit.getUser().getId());
        Page wrapped = new MerchantWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }




    /**
     * 编辑商户
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "merchantId", dict = MerchantMap.class)
    @ResponseBody
    public ResponseData edit(Merchant merchant) {
        merchantService.updateById(merchant);
        return SUCCESS_TIP;
    }

    /**
     * 添加商户
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加商户", key = "name", dict = MerchantMap.class)
    @ResponseBody
    public ResponseData add(Merchant merchant, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (merchant == null) {
            return ResponseData.error("参数不能为空");
        }
        this.merchantService.addMerchant(merchant);
        return SUCCESS_TIP;
    }

    /**
     * 删除商户
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除商户", key = "merchantId", dict = MerchantMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long merchantId) {
        if (ToolUtil.isEmpty(merchantId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.merchantService.deleteMerchant(merchantId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{merchantId}")
    @ResponseBody
    public Object content(@PathVariable("merchantId") Long id) {
        Merchant merchant = merchantService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(merchant);
        return super.warpObject(new MerchantWrapper(stringObjectMap));
    }

    ///

    /**
     * 统计
     */
    @RequestMapping("/statistics")
    @ResponseBody
    public Object statistics() {
        return null;
    }


    /**
     * 重置商户密码
     */
    @RequestMapping("/resetPwd")
    @BussinessLog(value = "重置商户密码", key = "merchantId", dict = MerchantMap.class)
    @ResponseBody
    public ResponseData resetPwd(@RequestParam Long merchantId) {
        if (ToolUtil.isEmpty(merchantId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.merchantService.resetPwd(merchantId);
        return SUCCESS_TIP;
    }


}