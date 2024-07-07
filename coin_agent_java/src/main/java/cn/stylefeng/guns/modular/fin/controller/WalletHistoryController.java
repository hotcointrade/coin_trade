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
import cn.stylefeng.guns.modular.fin.service.WalletHistoryService;
import cn.stylefeng.guns.modular.fin.wrapper.WalletHistoryWrapper;
import cn.stylefeng.guns.modular.fin.entity.WalletHistory;
import cn.stylefeng.guns.modular.fin.constant.WalletHistoryMap;

/**
 * 钱包历史变更控制器
 *
 * @author yaying.liu
 * @Date 2019-12-09 17:06:57
 */
@Controller
@RequestMapping("/walletHistory")
public class WalletHistoryController extends BaseController {

    private String PREFIX = "/modular/fin/walletHistory/";

     @Autowired
     private WalletHistoryService walletHistoryService;

    /**
     * 跳转到钱包历史变更首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "walletHistory.html";
    }

    /**
     * 跳转到添加钱包历史变更
     */
    @RequestMapping("/walletHistory_add")
    public String walletHistoryAdd() {
        return PREFIX + "walletHistory_add.html";
    }

    /**
     * 跳转到修改钱包历史变更
     */
    @RequestMapping("/walletHistory_edit")
    public String walletHistoryEdit(Long walletHistoryId, Model model) {
        WalletHistory condition = this.walletHistoryService.getById(walletHistoryId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "walletHistory_edit.html";
    }

    /**
     * 钱包历史变更详情
     */
    @RequestMapping(value = "/detail/{walletHistoryId}")
    @ResponseBody
    public Object detail(@PathVariable("walletHistoryId") Long walletHistoryId) {
        WalletHistory entity = walletHistoryService.getById(walletHistoryId);
      //  WalletHistoryDto conditionDto = new WalletHistoryDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询钱包历史变更列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = walletHistoryService.selectByCondition(condition);
        Page wrapped = new WalletHistoryWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑钱包历史变更
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "walletHistoryId", dict = WalletHistoryMap.class)
    @ResponseBody
    public ResponseData edit(WalletHistory walletHistory) {
        walletHistoryService.updateById(walletHistory);
        return SUCCESS_TIP;
    }

    /**
     * 添加钱包历史变更
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加钱包历史变更", key = "name", dict = WalletHistoryMap.class)
    @ResponseBody
    public ResponseData add(WalletHistory walletHistory, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (walletHistory == null) {
            return ResponseData.error("参数不能为空");
        }
        this.walletHistoryService.addWalletHistory(walletHistory);
        return SUCCESS_TIP;
    }

    /**
     * 删除钱包历史变更
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除钱包历史变更", key = "walletHistoryId", dict = WalletHistoryMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long walletHistoryId) {
        if (ToolUtil.isEmpty(walletHistoryId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.walletHistoryService.deleteWalletHistory(walletHistoryId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{walletHistoryId}")
    @ResponseBody
    public Object content(@PathVariable("walletHistoryId") Long id) {
        WalletHistory walletHistory = walletHistoryService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(walletHistory);
        return super.warpObject(new WalletHistoryWrapper(stringObjectMap));
    }
}