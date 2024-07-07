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
import cn.stylefeng.guns.modular.chain.service.DappService;
import cn.stylefeng.guns.modular.chain.wrapper.DappWrapper;
import cn.stylefeng.guns.modular.chain.entity.Dapp;
import cn.stylefeng.guns.modular.chain.constant.DappMap;

/**
 * 钱包调用api记录控制器
 *
 * @author yaying.liu
 * @since 2020-08-18 19:46:56
 */
@Controller
@RequestMapping("/dapp")
public class DappController extends BaseController {

    private String PREFIX = "/modular/chain/dapp/";

     @Autowired
     private DappService dappService;

    /**
     * 跳转到钱包调用api记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "dapp.html";
    }

    /**
     * 跳转到添加钱包调用api记录
     */
    @RequestMapping("/dapp_add")
    public String dappAdd() {
        return PREFIX + "dapp_add.html";
    }

    /**
     * 跳转到修改钱包调用api记录
     */
    @RequestMapping("/dapp_edit")
    public String dappEdit(Long dappId, Model model) {
        Dapp condition = this.dappService.getById(dappId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "dapp_edit.html";
    }

    /**
     * 钱包调用api记录详情
     */
    @RequestMapping(value = "/detail/{dappId}")
    @ResponseBody
    public Object detail(@PathVariable("dappId") Long dappId) {
        Dapp entity = dappService.getById(dappId);
      //  DappDto conditionDto = new DappDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询钱包调用api记录列表
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
        Page<Map<String, Object>> result = dappService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new DappWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑钱包调用api记录
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "dappId", dict = DappMap.class)
    @ResponseBody
    public ResponseData edit(Dapp dapp) {
        dappService.updateById(dapp);
        return SUCCESS_TIP;
    }

    /**
     * 添加钱包调用api记录
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加钱包调用api记录", key = "name", dict = DappMap.class)
    @ResponseBody
    public ResponseData add(Dapp dapp, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (dapp == null) {
            return ResponseData.error("参数不能为空");
        }
        this.dappService.addDapp(dapp);
        return SUCCESS_TIP;
    }

    /**
     * 删除钱包调用api记录
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除钱包调用api记录", key = "dappId", dict = DappMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long dappId) {
        if (ToolUtil.isEmpty(dappId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.dappService.deleteDapp(dappId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{dappId}")
    @ResponseBody
    public Object content(@PathVariable("dappId") Long id) {
        Dapp dapp = dappService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(dapp);
        return super.warpObject(new DappWrapper(stringObjectMap));
    }
}