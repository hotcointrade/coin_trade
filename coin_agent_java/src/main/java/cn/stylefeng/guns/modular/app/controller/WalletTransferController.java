package cn.stylefeng.guns.modular.app.controller;

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
import cn.stylefeng.guns.modular.app.service.WalletTransferService;
import cn.stylefeng.guns.modular.app.wrapper.WalletTransferWrapper;
import cn.stylefeng.guns.modular.app.entity.WalletTransfer;
import cn.stylefeng.guns.modular.app.constant.WalletTransferMap;

/**
 * 佣金划转控制器
 *
 * @author yaying.liu
 * @since 2022-02-18 20:54:40
 */
@Controller
@RequestMapping("/walletTransfer")
public class WalletTransferController extends BaseController {

    private String PREFIX = "/modular/app/walletTransfer/";

     @Autowired
     private WalletTransferService walletTransferService;

    /**
     * 跳转到佣金划转首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "walletTransfer.html";
    }

    /**
     * 跳转到添加佣金划转
     */
    @RequestMapping("/walletTransfer_add")
    public String walletTransferAdd() {
        return PREFIX + "walletTransfer_add.html";
    }

    /**
     * 跳转到修改佣金划转
     */
    @RequestMapping("/walletTransfer_edit")
    public String walletTransferEdit(Long walletTransferId, Model model) {
        WalletTransfer condition = this.walletTransferService.getById(walletTransferId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "walletTransfer_edit.html";
    }

    /**
     * 佣金划转详情
     */
    @RequestMapping(value = "/detail/{walletTransferId}")
    @ResponseBody
    public Object detail(@PathVariable("walletTransferId") Long walletTransferId) {
        WalletTransfer entity = walletTransferService.getById(walletTransferId);
      //  WalletTransferDto conditionDto = new WalletTransferDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询佣金划转列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition, @RequestParam(required = false) String timeLimit) {
        //根据条件查询
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Page<Map<String, Object>> result = walletTransferService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new WalletTransferWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑佣金划转
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "walletTransferId", dict = WalletTransferMap.class)
    @ResponseBody
    public ResponseData edit(WalletTransfer walletTransfer) {
        walletTransferService.updateById(walletTransfer);
        return SUCCESS_TIP;
    }

    /**
     * 添加佣金划转
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加佣金划转", key = "name", dict = WalletTransferMap.class)
    @ResponseBody
    public ResponseData add(WalletTransfer walletTransfer, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (walletTransfer == null) {
            return ResponseData.error("参数不能为空");
        }
        this.walletTransferService.addWalletTransfer(walletTransfer);
        return SUCCESS_TIP;
    }

    /**
     * 删除佣金划转
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除佣金划转", key = "walletTransferId", dict = WalletTransferMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long walletTransferId) {
        if (ToolUtil.isEmpty(walletTransferId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.walletTransferService.deleteWalletTransfer(walletTransferId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{walletTransferId}")
    @ResponseBody
    public Object content(@PathVariable("walletTransferId") Long id) {
        WalletTransfer walletTransfer = walletTransferService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(walletTransfer);
        return super.warpObject(new WalletTransferWrapper(stringObjectMap));
    }
}