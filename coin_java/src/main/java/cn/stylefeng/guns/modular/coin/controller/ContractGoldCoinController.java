package cn.stylefeng.guns.modular.coin.controller;

import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.stylefeng.guns.modular.coin.entity.ContractGoldCoin;
import cn.stylefeng.guns.modular.coin.service.ContractGoldCoinService;

@Controller
@RequestMapping("/contractGoldCoin")
public class ContractGoldCoinController extends BaseController{

	private String PREFIX = "/modular/coin/gold/";
    @Autowired
    private ContractGoldCoinService contractGoldCoinService;

    /**
     * 跳转到黄金交易对首页
     */
    @RequestMapping("")
    public String index()
    {
        return PREFIX + "contractGoldCoin.html";
    }
	
	 /**
     * 跳转到添加黄金交易对
     */
    @RequestMapping("/contractGoldCoin_add")
    public String spotAdd() {
        return PREFIX + "contractGoldCoin_add.html";
    }
	 /**
     * 跳转到修改黄金交易对
     */
    @RequestMapping("/contractGoldCoin_edit")
    public String spotEdit(Long id, Model model) {
        ContractGoldCoin condition = this.contractGoldCoinService.getById(id);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "contractGoldCoin_edit.html";
    }
	
	/**
     * 黄金交易对详情
     */
    @RequestMapping(value = "/detail/{id}")
    @ResponseBody
    public Object detail(@PathVariable("id") Long id) {
        ContractGoldCoin entity = contractGoldCoinService.getById(id);
        return entity;
    }
	/**
     * 查询黄金交易对列表
     */
   
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        Page<Map<String, Object>> result = contractGoldCoinService.selectByCondition(condition);
        return LayuiPageFactory.createPageInfo(result);
    }
	
	/**
     * 编辑黄金交易对
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(ContractGoldCoin spot) {
        contractGoldCoinService.updateById(spot);
        return SUCCESS_TIP;
    }
	
	/**
     * 添加黄金交易对
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseData add(ContractGoldCoin contractGoldCoin, BindingResult result) {
        
        //spot1.setDel("N");
        //int count = this.contractGoldCoinService.count(new QueryWrapper<>(contractGoldCoin));
        //if (count >0){
        //    return ResponseData.error("该记录已存在");
        //}
        this.contractGoldCoinService.save(contractGoldCoin);
        return SUCCESS_TIP;
    }
	
	/**
     * 删除黄金交易对
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestParam Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //ContractGoldCoin entity =  this.contractGoldCoinService.getById(id);
        
        contractGoldCoinService.removeById(id);
        return SUCCESS_TIP;
    }
    
}