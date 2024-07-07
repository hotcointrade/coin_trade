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
import cn.stylefeng.guns.modular.fin.service.PlatformCashflowService;
import cn.stylefeng.guns.modular.fin.wrapper.PlatformCashflowWrapper;
import cn.stylefeng.guns.modular.fin.entity.PlatformCashflow;
import cn.stylefeng.guns.modular.fin.constant.PlatformCashflowMap;

/**
 * 平台流水控制器
 *
 * @author yaying.liu
 * @Date 2020-01-03 18:06:11
 */
@Controller
@RequestMapping("/platformCashflow")
public class PlatformCashflowController extends BaseController {

    private String PREFIX = "/modular/fin/platformCashflow/";

     @Autowired
     private PlatformCashflowService platformCashflowService;

    /**
     * 跳转到平台流水首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "platformCashflow.html";
    }

    /**
     * 跳转到添加平台流水
     */
    @RequestMapping("/platformCashflow_add")
    public String platformCashflowAdd() {
        return PREFIX + "platformCashflow_add.html";
    }

    /**
     * 跳转到修改平台流水
     */
    @RequestMapping("/platformCashflow_edit")
    public String platformCashflowEdit(Long platformCashflowId, Model model) {
        PlatformCashflow condition = this.platformCashflowService.getById(platformCashflowId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "platformCashflow_edit.html";
    }

    /**
     * 平台流水详情
     */
    @RequestMapping(value = "/detail/{platformCashflowId}")
    @ResponseBody
    public Object detail(@PathVariable("platformCashflowId") Long platformCashflowId) {
        PlatformCashflow entity = platformCashflowService.getById(platformCashflowId);
      //  PlatformCashflowDto conditionDto = new PlatformCashflowDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询平台流水列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = platformCashflowService.selectByCondition(condition);
        Page wrapped = new PlatformCashflowWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑平台流水
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "platformCashflowId", dict = PlatformCashflowMap.class)
    @ResponseBody
    public ResponseData edit(PlatformCashflow platformCashflow) {
        platformCashflowService.updateById(platformCashflow);
        return SUCCESS_TIP;
    }

    /**
     * 添加平台流水
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加平台流水", key = "name", dict = PlatformCashflowMap.class)
    @ResponseBody
    public ResponseData add(PlatformCashflow platformCashflow, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (platformCashflow == null) {
            return ResponseData.error("参数不能为空");
        }
        this.platformCashflowService.addPlatformCashflow(platformCashflow);
        return SUCCESS_TIP;
    }

    /**
     * 删除平台流水
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除平台流水", key = "platformCashflowId", dict = PlatformCashflowMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long platformCashflowId) {
        if (ToolUtil.isEmpty(platformCashflowId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.platformCashflowService.deletePlatformCashflow(platformCashflowId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{platformCashflowId}")
    @ResponseBody
    public Object content(@PathVariable("platformCashflowId") Long id) {
        PlatformCashflow platformCashflow = platformCashflowService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(platformCashflow);
        return super.warpObject(new PlatformCashflowWrapper(stringObjectMap));
    }
}