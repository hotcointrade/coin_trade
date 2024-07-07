package cn.stylefeng.guns.modular.fin.controller;

import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.fin.service.CashflowService;
import cn.stylefeng.guns.modular.fin.wrapper.CashflowWrapper;
import cn.stylefeng.guns.modular.fin.entity.Cashflow;
import cn.stylefeng.guns.modular.fin.constant.CashflowMap;

/**
 * 流水记录控制器
 *
 * @author yaying.liu
 * @Date 2019-12-09 17:06:14
 */
@Controller
@RequestMapping("/cashflow")
public class CashflowController extends BaseController {

    private String PREFIX = "/modular/fin/cashflow/";

     @Autowired
     private CashflowService cashflowService;

    /**
     * 跳转到流水记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "cashflow.html";
    }

    /**
     * 跳转到添加流水记录
     */
    @RequestMapping("/cashflow_add")
    public String cashflowAdd() {
        return PREFIX + "cashflow_add.html";
    }

    /**
     * 跳转到修改流水记录
     */
    @RequestMapping("/cashflow_edit")
    public String cashflowEdit(Long cashflowId, Model model) {
        Cashflow condition = this.cashflowService.getById(cashflowId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "cashflow_edit.html";
    }

    /**
     * 流水记录详情
     */
    @RequestMapping(value = "/detail/{cashflowId}")
    @ResponseBody
    public Object detail(@PathVariable("cashflowId") Long cashflowId) {
        Cashflow entity = cashflowService.getById(cashflowId);
      //  CashflowDto conditionDto = new CashflowDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询流水记录列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition
            ,@RequestParam(required = false)String flowType
            ,@RequestParam(required = false)String itemCode
    ) {
        //根据条件查询
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        Page<Map<String, Object>> result = cashflowService.selectByCondition(condition,flowType,itemCode,memberId,recommendIds);
        Page wrapped = new CashflowWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑流水记录
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "cashflowId", dict = CashflowMap.class)
    @ResponseBody
    public ResponseData edit(Cashflow cashflow) {
        cashflowService.updateById(cashflow);
        return SUCCESS_TIP;
    }

    /**
     * 添加流水记录
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加流水记录", key = "name", dict = CashflowMap.class)
    @ResponseBody
    public ResponseData add(Cashflow cashflow, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (cashflow == null) {
            return ResponseData.error("参数不能为空");
        }
        this.cashflowService.addCashflow(cashflow);
        return SUCCESS_TIP;
    }

    /**
     * 删除流水记录
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除流水记录", key = "cashflowId", dict = CashflowMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long cashflowId) {
        if (ToolUtil.isEmpty(cashflowId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.cashflowService.deleteCashflow(cashflowId);
        return SUCCESS_TIP;
    }

    @RequestMapping("/getTypeList")
    @ResponseBody
    public List<Map<String, String>> getLevel() {
        List<Map<String,String>> result = new ArrayList<>();
        for (ProConst.CashFlowTypeEnum  entity : ProConst.CashFlowTypeEnum.values()) {
            Map map = new HashMap();
            map.put("code", entity.getCode());
            map.put("value", entity.getValue());
            result.add(map);
        }
        return result;
    }
    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{cashflowId}")
    @ResponseBody
    public Object content(@PathVariable("cashflowId") Long id) {
        Cashflow cashflow = cashflowService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(cashflow);
        return super.warpObject(new CashflowWrapper(stringObjectMap));
    }
}