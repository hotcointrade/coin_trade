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
import cn.stylefeng.guns.modular.promotion.service.GatewayDefineService;
import cn.stylefeng.guns.modular.promotion.wrapper.GatewayDefineWrapper;
import cn.stylefeng.guns.modular.promotion.entity.GatewayDefine;
import cn.stylefeng.guns.modular.promotion.constant.GatewayDefineMap;

/**
 * 网关定义控制器
 *
 * @author yaying.liu
 * @Date 2019-10-11 11:20:42
 */
@Controller
@RequestMapping("/gatewayDefine")
public class GatewayDefineController extends BaseController {

    private String PREFIX = "/modular/promotion/gatewayDefine/";

     @Autowired
     private GatewayDefineService gatewayDefineService;

    /**
     * 跳转到网关定义首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "gatewayDefine.html";
    }

    /**
     * 跳转到添加网关定义
     */
    @RequestMapping("/gatewayDefine_add")
    public String gatewayDefineAdd() {
        return PREFIX + "gatewayDefine_add.html";
    }

    /**
     * 跳转到修改网关定义
     */
    @RequestMapping("/gatewayDefine_edit")
    public String gatewayDefineEdit(Long gatewayDefineId, Model model) {
        GatewayDefine condition = this.gatewayDefineService.getById(gatewayDefineId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "gatewayDefine_edit.html";
    }

    /**
     * 网关定义详情
     */
    @RequestMapping(value = "/detail/{gatewayDefineId}")
    @ResponseBody
    public Object detail(@PathVariable("gatewayDefineId") Long gatewayDefineId) {
        GatewayDefine entity = gatewayDefineService.getById(gatewayDefineId);
      //  GatewayDefineDto conditionDto = new GatewayDefineDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询网关定义列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = gatewayDefineService.selectByCondition(condition);
        Page wrapped = new GatewayDefineWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑网关定义
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "gatewayDefineId", dict = GatewayDefineMap.class)
    @ResponseBody
    public ResponseData edit(GatewayDefine gatewayDefine) {
        gatewayDefineService.updateById(gatewayDefine);
        return SUCCESS_TIP;
    }

    /**
     * 添加网关定义
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加网关定义", key = "name", dict = GatewayDefineMap.class)
    @ResponseBody
    public ResponseData add(GatewayDefine gatewayDefine, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (gatewayDefine == null) {
            return ResponseData.error("参数不能为空");
        }
        this.gatewayDefineService.addGatewayDefine(gatewayDefine);
        return SUCCESS_TIP;
    }

    /**
     * 删除网关定义
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除网关定义", key = "gatewayDefineId", dict = GatewayDefineMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long gatewayDefineId) {
        if (ToolUtil.isEmpty(gatewayDefineId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.gatewayDefineService.deleteGatewayDefine(gatewayDefineId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{gatewayDefineId}")
    @ResponseBody
    public Object content(@PathVariable("gatewayDefineId") Long id) {
        GatewayDefine gatewayDefine = gatewayDefineService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(gatewayDefine);
        return super.warpObject(new GatewayDefineWrapper(stringObjectMap));
    }
}