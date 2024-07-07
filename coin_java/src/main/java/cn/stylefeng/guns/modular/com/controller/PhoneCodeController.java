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
import cn.stylefeng.guns.modular.com.service.PhoneCodeService;
import cn.stylefeng.guns.modular.com.wrapper.PhoneCodeWrapper;
import cn.stylefeng.guns.modular.com.entity.PhoneCode;
import cn.stylefeng.guns.modular.com.constant.PhoneCodeMap;

/**
 * 电话区号控制器
 *
 * @author yaying.liu
 * @Date 2019-12-20 17:59:48
 */
@Controller
@RequestMapping("/phoneCode")
public class PhoneCodeController extends BaseController {

    private String PREFIX = "/modular/com/phoneCode/";

     @Autowired
     private PhoneCodeService phoneCodeService;

    /**
     * 跳转到电话区号首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "phoneCode.html";
    }

    /**
     * 跳转到添加电话区号
     */
    @RequestMapping("/phoneCode_add")
    public String phoneCodeAdd() {
        return PREFIX + "phoneCode_add.html";
    }

    /**
     * 跳转到修改电话区号
     */
    @RequestMapping("/phoneCode_edit")
    public String phoneCodeEdit(Long phoneCodeId, Model model) {
        PhoneCode condition = this.phoneCodeService.getById(phoneCodeId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "phoneCode_edit.html";
    }

    /**
     * 电话区号详情
     */
    @RequestMapping(value = "/detail/{phoneCodeId}")
    @ResponseBody
    public Object detail(@PathVariable("phoneCodeId") Long phoneCodeId) {
        PhoneCode entity = phoneCodeService.getById(phoneCodeId);
      //  PhoneCodeDto conditionDto = new PhoneCodeDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询电话区号列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = phoneCodeService.selectByCondition(condition);
        Page wrapped = new PhoneCodeWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑电话区号
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "phoneCodeId", dict = PhoneCodeMap.class)
    @ResponseBody
    public ResponseData edit(PhoneCode phoneCode) {
        phoneCodeService.updateById(phoneCode);
        return SUCCESS_TIP;
    }

    /**
     * 添加电话区号
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加电话区号", key = "name", dict = PhoneCodeMap.class)
    @ResponseBody
    public ResponseData add(PhoneCode phoneCode, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (phoneCode == null) {
            return ResponseData.error("参数不能为空");
        }
        this.phoneCodeService.addPhoneCode(phoneCode);
        return SUCCESS_TIP;
    }

    /**
     * 删除电话区号
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除电话区号", key = "phoneCodeId", dict = PhoneCodeMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long phoneCodeId) {
        if (ToolUtil.isEmpty(phoneCodeId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.phoneCodeService.deletePhoneCode(phoneCodeId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{phoneCodeId}")
    @ResponseBody
    public Object content(@PathVariable("phoneCodeId") Long id) {
        PhoneCode phoneCode = phoneCodeService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(phoneCode);
        return super.warpObject(new PhoneCodeWrapper(stringObjectMap));
    }
}