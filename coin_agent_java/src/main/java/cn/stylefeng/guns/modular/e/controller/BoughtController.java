package cn.stylefeng.guns.modular.e.controller;

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
import cn.stylefeng.guns.modular.e.service.BoughtService;
import cn.stylefeng.guns.modular.e.wrapper.BoughtWrapper;
import cn.stylefeng.guns.modular.e.entity.Bought;
import cn.stylefeng.guns.modular.e.constant.BoughtMap;

/**
 * 法币交易控制器
 *
 * @author yaying.liu
 * @Date 2020-04-03 09:48:13
 */
@Controller
@RequestMapping("/bought")
public class BoughtController extends BaseController {

    private String PREFIX = "/modular/e/bought/";

     @Autowired
     private BoughtService boughtService;

    /**
     * 跳转到法币交易首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bought.html";
    }

    /**
     * 跳转到添加法币交易
     */
    @RequestMapping("/bought_add")
    public String boughtAdd() {
        return PREFIX + "bought_add.html";
    }

    /**
     * 跳转到修改法币交易
     */
    @RequestMapping("/bought_edit")
    public String boughtEdit(Long boughtId, Model model) {
        Bought condition = this.boughtService.getById(boughtId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "bought_edit.html";
    }

    /**
     * 法币交易详情
     */
    @RequestMapping(value = "/detail/{boughtId}")
    @ResponseBody
    public Object detail(@PathVariable("boughtId") Long boughtId) {
        Bought entity = boughtService.getById(boughtId);
      //  BoughtDto conditionDto = new BoughtDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询法币交易列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = boughtService.selectByCondition(condition);
        Page wrapped = new BoughtWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑法币交易
     */
    @RequestMapping("/edit")
   // @BussinessLog(value = "编辑参数", key = "boughtId", dict = BoughtMap.class)
    @ResponseBody
    public ResponseData edit(Bought bought) {
        boughtService.updateById(bought);
        return SUCCESS_TIP;
    }

    /**
     * 添加法币交易
     */
    @RequestMapping("/add")
   // @BussinessLog(value = "添加法币交易", key = "name", dict = BoughtMap.class)
    @ResponseBody
    public ResponseData add(Bought bought, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (bought == null) {
            return ResponseData.error("参数不能为空");
        }
        this.boughtService.addBought(bought);
        return SUCCESS_TIP;
    }

    /**
     * 删除法币交易
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除法币交易", key = "boughtId", dict = BoughtMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long boughtId) {
        if (ToolUtil.isEmpty(boughtId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.boughtService.deleteBought(boughtId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{boughtId}")
    @ResponseBody
    public Object content(@PathVariable("boughtId") Long id) {
        Bought bought = boughtService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(bought);
        return super.warpObject(new BoughtWrapper(stringObjectMap));
    }
}