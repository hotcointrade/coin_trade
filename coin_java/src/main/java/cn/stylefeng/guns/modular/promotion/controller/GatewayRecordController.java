package cn.stylefeng.guns.modular.promotion.controller;

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
import cn.stylefeng.guns.modular.promotion.service.GatewayRecordService;
import cn.stylefeng.guns.modular.promotion.wrapper.GatewayRecordWrapper;
import cn.stylefeng.guns.modular.promotion.entity.GatewayRecord;
import cn.stylefeng.guns.modular.promotion.constant.GatewayRecordMap;

/**
 * 网关记录控制器
 *
 * @author yaying.liu
 * @Date 2019-10-11 10:44:44
 */
@Controller
@RequestMapping("/gatewayRecord")
public class GatewayRecordController extends BaseController {

    private String PREFIX = "/modular/promotion/gatewayRecord/";

     @Autowired
     private GatewayRecordService gatewayRecordService;

    /**
     * 跳转到网关记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "gatewayRecord.html";
    }

    /**
     * 跳转到添加网关记录
     */
    @RequestMapping("/gatewayRecord_add")
    public String gatewayRecordAdd() {
        return PREFIX + "gatewayRecord_add.html";
    }

    /**
     * 跳转到修改网关记录
     */
    @RequestMapping("/gatewayRecord_edit")
    public String gatewayRecordEdit(Long gatewayRecordId, Model model) {
        GatewayRecord condition = this.gatewayRecordService.getById(gatewayRecordId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "gatewayRecord_edit.html";
    }

    /**
     * 网关记录详情
     */
    @RequestMapping(value = "/detail/{gatewayRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("gatewayRecordId") Long gatewayRecordId) {
        GatewayRecord entity = gatewayRecordService.getById(gatewayRecordId);
      //  GatewayRecordDto conditionDto = new GatewayRecordDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询网关记录列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = gatewayRecordService.selectByCondition(condition);
        Page wrapped = new GatewayRecordWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑网关记录
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "gatewayRecordId", dict = GatewayRecordMap.class)
    @ResponseBody
    public ResponseData edit(GatewayRecord gatewayRecord) {
        gatewayRecordService.updateById(gatewayRecord);
        return SUCCESS_TIP;
    }

    /**
     * 添加网关记录
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加网关记录", key = "name", dict = GatewayRecordMap.class)
    @ResponseBody
    public ResponseData add(GatewayRecord gatewayRecord, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (gatewayRecord == null) {
            return ResponseData.error("参数不能为空");
        }
        this.gatewayRecordService.addGatewayRecord(gatewayRecord);
        return SUCCESS_TIP;
    }

    /**
     * 删除网关记录
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除网关记录", key = "gatewayRecordId", dict = GatewayRecordMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long gatewayRecordId) {
        if (ToolUtil.isEmpty(gatewayRecordId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.gatewayRecordService.deleteGatewayRecord(gatewayRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{gatewayRecordId}")
    @ResponseBody
    public Object content(@PathVariable("gatewayRecordId") Long id) {
        GatewayRecord gatewayRecord = gatewayRecordService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(gatewayRecord);
        return super.warpObject(new GatewayRecordWrapper(stringObjectMap));
    }
}