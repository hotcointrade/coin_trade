package cn.stylefeng.guns.modular.chain.controller;

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
import cn.stylefeng.guns.modular.chain.service.TransferService;
import cn.stylefeng.guns.modular.chain.wrapper.TransferWrapper;
import cn.stylefeng.guns.modular.chain.entity.Transfer;
import cn.stylefeng.guns.modular.chain.constant.TransferMap;

/**
 * 转账信息控制器
 *
 * @author yaying.liu
 * @Date 2020-01-14 17:08:46
 */
@Controller
@RequestMapping("/transfer")
public class TransferController extends BaseController {

    private String PREFIX = "/modular/chain/transfer/";

     @Autowired
     private TransferService transferService;

    /**
     * 跳转到转账信息首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "transfer.html";
    }

    /**
     * 跳转到添加转账信息
     */
    @RequestMapping("/transfer_add")
    public String transferAdd() {
        return PREFIX + "transfer_add.html";
    }

    /**
     * 跳转到修改转账信息
     */
    @RequestMapping("/transfer_edit")
    public String transferEdit(Long transferId, Model model) {
        Transfer condition = this.transferService.getById(transferId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "transfer_edit.html";
    }

    /**
     * 转账信息详情
     */
    @RequestMapping(value = "/detail/{transferId}")
    @ResponseBody
    public Object detail(@PathVariable("transferId") Long transferId) {
        Transfer entity = transferService.getById(transferId);
      //  TransferDto conditionDto = new TransferDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询转账信息列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = transferService.selectByCondition(condition);
        Page wrapped = new TransferWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑转账信息
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "transferId", dict = TransferMap.class)
    @ResponseBody
    public ResponseData edit(Transfer transfer) {
        transferService.updateById(transfer);
        return SUCCESS_TIP;
    }

    /**
     * 添加转账信息
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加转账信息", key = "name", dict = TransferMap.class)
    @ResponseBody
    public ResponseData add(Transfer transfer, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (transfer == null) {
            return ResponseData.error("参数不能为空");
        }
        this.transferService.addTransfer(transfer);
        return SUCCESS_TIP;
    }

    /**
     * 删除转账信息
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除转账信息", key = "transferId", dict = TransferMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long transferId) {
        if (ToolUtil.isEmpty(transferId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.transferService.deleteTransfer(transferId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{transferId}")
    @ResponseBody
    public Object content(@PathVariable("transferId") Long id) {
        Transfer transfer = transferService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(transfer);
        return super.warpObject(new TransferWrapper(stringObjectMap));
    }
}