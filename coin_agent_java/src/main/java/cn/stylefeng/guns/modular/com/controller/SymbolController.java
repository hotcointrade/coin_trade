package cn.stylefeng.guns.modular.com.controller;

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
import cn.stylefeng.guns.modular.com.service.SymbolService;
import cn.stylefeng.guns.modular.com.wrapper.SymbolWrapper;
import cn.stylefeng.guns.modular.com.entity.Symbol;
import cn.stylefeng.guns.modular.com.constant.SymbolMap;

/**
 * 币种控制器
 *
 * @author yaying.liu
 * @Date 2020-03-09 11:03:53
 */
@Controller
@RequestMapping("/symbol")
public class SymbolController extends BaseController {

    private String PREFIX = "/modular/com/symbol/";

     @Autowired
     private SymbolService symbolService;

    /**
     * 跳转到币种首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "symbol.html";
    }

    /**
     * 跳转到添加币种
     */
    @RequestMapping("/symbol_add")
    public String symbolAdd() {
        return PREFIX + "symbol_add.html";
    }

    /**
     * 跳转到修改币种
     */
    @RequestMapping("/symbol_edit")
    public String symbolEdit(Long symbolId, Model model) {
        Symbol condition = this.symbolService.getById(symbolId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "symbol_edit.html";
    }

    /**
     * 币种详情
     */
    @RequestMapping(value = "/detail/{symbolId}")
    @ResponseBody
    public Object detail(@PathVariable("symbolId") Long symbolId) {
        Symbol entity = symbolService.getById(symbolId);
      //  SymbolDto conditionDto = new SymbolDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询币种列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = symbolService.selectByCondition(condition);
        Page wrapped = new SymbolWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑币种
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "symbolId", dict = SymbolMap.class)
    @ResponseBody
    public ResponseData edit(Symbol symbol) {
        symbolService.updateById(symbol);
        return SUCCESS_TIP;
    }

    /**
     * 添加币种
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加币种", key = "name", dict = SymbolMap.class)
    @ResponseBody
    public ResponseData add(Symbol symbol, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (symbol == null) {
            return ResponseData.error("参数不能为空");
        }
        this.symbolService.addSymbol(symbol);
        return SUCCESS_TIP;
    }

    /**
     * 删除币种
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除币种", key = "symbolId", dict = SymbolMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long symbolId) {
        if (ToolUtil.isEmpty(symbolId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.symbolService.deleteSymbol(symbolId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{symbolId}")
    @ResponseBody
    public Object content(@PathVariable("symbolId") Long id) {
        Symbol symbol = symbolService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(symbol);
        return super.warpObject(new SymbolWrapper(stringObjectMap));
    }
}