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
import cn.stylefeng.guns.modular.chain.service.ConvertService;
import cn.stylefeng.guns.modular.chain.wrapper.ConvertWrapper;
import cn.stylefeng.guns.modular.chain.entity.Convert;
import cn.stylefeng.guns.modular.chain.constant.ConvertMap;

/**
 * 豆兑换信息控制器
 *
 * @author yaying.liu
 * @Date 2020-01-17 14:46:22
 */
@Controller
@RequestMapping("/convert")
public class ConvertController extends BaseController {

    private String PREFIX = "/modular/chain/convert/";

     @Autowired
     private ConvertService convertService;

    /**
     * 跳转到豆兑换信息首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "convert.html";
    }

    /**
     * 跳转到添加豆兑换信息
     */
    @RequestMapping("/convert_add")
    public String convertAdd() {
        return PREFIX + "convert_add.html";
    }

    /**
     * 跳转到修改豆兑换信息
     */
    @RequestMapping("/convert_edit")
    public String convertEdit(Long convertId, Model model) {
        Convert condition = this.convertService.getById(convertId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "convert_edit.html";
    }

    /**
     * 豆兑换信息详情
     */
    @RequestMapping(value = "/detail/{convertId}")
    @ResponseBody
    public Object detail(@PathVariable("convertId") Long convertId) {
        Convert entity = convertService.getById(convertId);
      //  ConvertDto conditionDto = new ConvertDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询豆兑换信息列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = convertService.selectByCondition(condition);
        Page wrapped = new ConvertWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑豆兑换信息
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "convertId", dict = ConvertMap.class)
    @ResponseBody
    public ResponseData edit(Convert convert) {
        convertService.updateById(convert);
        return SUCCESS_TIP;
    }

    /**
     * 添加豆兑换信息
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加豆兑换信息", key = "name", dict = ConvertMap.class)
    @ResponseBody
    public ResponseData add(Convert convert, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (convert == null) {
            return ResponseData.error("参数不能为空");
        }
        this.convertService.addConvert(convert);
        return SUCCESS_TIP;
    }

    /**
     * 删除豆兑换信息
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除豆兑换信息", key = "convertId", dict = ConvertMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long convertId) {
        if (ToolUtil.isEmpty(convertId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.convertService.deleteConvert(convertId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{convertId}")
    @ResponseBody
    public Object content(@PathVariable("convertId") Long id) {
        Convert convert = convertService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(convert);
        return super.warpObject(new ConvertWrapper(stringObjectMap));
    }
}