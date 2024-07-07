package cn.stylefeng.guns.modular.fin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.fin.constant.ContractMap;
import cn.stylefeng.guns.modular.fin.entity.FinFutures;
import cn.stylefeng.guns.modular.fin.service.ContractService;
import cn.stylefeng.guns.modular.fin.service.FinFuturesService;
import cn.stylefeng.guns.modular.fin.wrapper.ContractWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 黄金账户控制器
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:09:49
 */
@Controller
@RequestMapping("/finFutures")
public class FinFuturesController extends BaseController {

    private String PREFIX = "/modular/fin/finfutures/";

     @Autowired
     private FinFuturesService contractService;

    /**
     * 跳转到黄金账户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "contract.html";
    }

    /**
     * 跳转到添加黄金账户
     */
    @RequestMapping("/contract_add")
    public String contractAdd() {
        return PREFIX + "contract_add.html";
    }

    /**
     * 跳转到修改黄金账户
     */
    @RequestMapping("/contract_edit")
    public String contractEdit(Long contractId, Model model) {
        FinFutures condition = this.contractService.getById(contractId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "contract_edit.html";
    }

    /**
     * 黄金账户详情
     */
    @RequestMapping(value = "/detail/{contractId}")
    @ResponseBody
    public Object detail(@PathVariable("contractId") Long contractId) {
        FinFutures entity = contractService.getById(contractId);
      //  ContractDto conditionDto = new ContractDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询黄金账户列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        Page<Map<String, Object>> result = contractService.selectByCondition(condition,memberId,recommendIds);
        Page wrapped = new ContractWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑黄金账户
     */
    @RequestMapping("/edit")
//    @BussinessLog(value = "编辑参数", key = "contractId", dict = ContractMap.class)
    @ResponseBody
    public ResponseData edit(FinFutures contract) {
       return contractService.edit(contract);
//        return SUCCESS_TIP;
    }

    /**
     * 添加黄金账户
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加黄金账户", key = "name", dict = ContractMap.class)
    @ResponseBody
    public ResponseData add(FinFutures contract, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (contract == null) {
            return ResponseData.error("参数不能为空");
        }
        this.contractService.addContract(contract);
        return SUCCESS_TIP;
    }

    /**
     * 删除黄金账户
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除黄金账户", key = "contractId", dict = ContractMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long contractId) {
        if (ToolUtil.isEmpty(contractId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.contractService.deleteContract(contractId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{contractId}")
    @ResponseBody
    public Object content(@PathVariable("contractId") Long id) {
        FinFutures contract = contractService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(contract);
        return super.warpObject(new ContractWrapper(stringObjectMap));
    }
}
