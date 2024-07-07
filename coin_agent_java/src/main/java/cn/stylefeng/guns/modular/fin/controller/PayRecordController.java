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
import cn.stylefeng.guns.modular.fin.service.PayRecordService;
import cn.stylefeng.guns.modular.fin.wrapper.PayRecordWrapper;
import cn.stylefeng.guns.modular.fin.entity.PayRecord;
import cn.stylefeng.guns.modular.fin.constant.PayRecordMap;

/**
 * 第三方接口记录表控制器
 *
 * @author yaying.liu
 * @Date 2019-12-23 14:15:58
 */
@Controller
@RequestMapping("/payRecord")
public class PayRecordController extends BaseController {

    private String PREFIX = "/modular/fin/payRecord/";

     @Autowired
     private PayRecordService payRecordService;

    /**
     * 跳转到第三方接口记录表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "payRecord.html";
    }

    /**
     * 跳转到添加第三方接口记录表
     */
    @RequestMapping("/payRecord_add")
    public String payRecordAdd() {
        return PREFIX + "payRecord_add.html";
    }

    /**
     * 跳转到修改第三方接口记录表
     */
    @RequestMapping("/payRecord_edit")
    public String payRecordEdit(Long payRecordId, Model model) {
        PayRecord condition = this.payRecordService.getById(payRecordId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "payRecord_edit.html";
    }

    /**
     * 第三方接口记录表详情
     */
    @RequestMapping(value = "/detail/{payRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("payRecordId") Long payRecordId) {
        PayRecord entity = payRecordService.getById(payRecordId);
      //  PayRecordDto conditionDto = new PayRecordDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询第三方接口记录表列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = payRecordService.selectByCondition(condition);
        Page wrapped = new PayRecordWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑第三方接口记录表
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "payRecordId", dict = PayRecordMap.class)
    @ResponseBody
    public ResponseData edit(PayRecord payRecord) {
        payRecordService.updateById(payRecord);
        return SUCCESS_TIP;
    }

    /**
     * 添加第三方接口记录表
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加第三方接口记录表", key = "name", dict = PayRecordMap.class)
    @ResponseBody
    public ResponseData add(PayRecord payRecord, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (payRecord == null) {
            return ResponseData.error("参数不能为空");
        }
        this.payRecordService.addPayRecord(payRecord);
        return SUCCESS_TIP;
    }

    /**
     * 删除第三方接口记录表
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除第三方接口记录表", key = "payRecordId", dict = PayRecordMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long payRecordId) {
        if (ToolUtil.isEmpty(payRecordId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.payRecordService.deletePayRecord(payRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{payRecordId}")
    @ResponseBody
    public Object content(@PathVariable("payRecordId") Long id) {
        PayRecord payRecord = payRecordService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(payRecord);
        return super.warpObject(new PayRecordWrapper(stringObjectMap));
    }
}