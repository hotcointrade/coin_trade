package cn.stylefeng.guns.modular.bulletin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.bulletin.constant.BankMap;
import cn.stylefeng.guns.modular.bulletin.entity.Bank;
import cn.stylefeng.guns.modular.bulletin.service.BankService;
import cn.stylefeng.guns.modular.bulletin.wrapper.BankWrapper;
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
 * 银行列表控制器
 *
 * @author yaying.liu
 * @Date 2019-09-11 14:24:35
 */
@Controller
@RequestMapping("/bank")
public class BankController extends BaseController {

    private String PREFIX = "/modular/bulletin/bank/";

    @Autowired
    private BankService bankService;

    /**
     * 跳转到银行列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bank.html";
    }

    /**
     * 跳转到添加银行列表
     */
    @RequestMapping("/bank_add")
    public String bankAdd() {
        return PREFIX + "bank_add.html";
    }

    /**
     * 跳转到修改银行列表
     */
    @RequestMapping("/bank_edit")
    public String bankEdit(Long bankId, Model model) {
        Bank condition = this.bankService.getById(bankId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "bank_edit.html";
    }

    /**
     * 银行列表详情
     */
    @RequestMapping(value = "/detail/{bankId}")
    @ResponseBody
    public Object detail(@PathVariable("bankId") Long bankId) {
        Bank entity = bankService.getById(bankId);
//        BankDto conditionDto = new BankDto();
//        BeanUtil.copyProperties(entity, conditionDto);
        return entity;
    }


    /**
     * 查询银行列表列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = bankService.selectByCondition(condition);
        Page wrapped = new BankWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑银行列表
     */
    @RequestMapping("/edit")
//    @BussinessLog(value = "编辑参数", key = "bankId", dict = BankMap.class)
    @ResponseBody
    public ResponseData edit(Bank bank) {
        bankService.updateById(bank);
        return SUCCESS_TIP;
    }

    /**
     * 添加银行列表
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加银行列表", key = "name", dict = BankMap.class)
    @ResponseBody
    public ResponseData add(Bank bank, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (bank == null) {
            return ResponseData.error("参数不能为空");
        }
        this.bankService.addBank(bank);
        return SUCCESS_TIP;
    }

    /**
     * 删除银行列表
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除银行列表", key = "bankId", dict = BankMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long bankId) {
        if (ToolUtil.isEmpty(bankId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.bankService.deleteBank(bankId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{bankId}")
    @ResponseBody
    public Object content(@PathVariable("bankId") Long id) {
        Bank bank = bankService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(bank);
        return super.warpObject(new BankWrapper(stringObjectMap));
    }
}