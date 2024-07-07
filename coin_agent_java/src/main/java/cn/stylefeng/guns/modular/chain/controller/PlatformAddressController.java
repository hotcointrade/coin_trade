package cn.stylefeng.guns.modular.chain.controller;

import cn.stylefeng.guns.modular.base.state.FailResponseData;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import cn.stylefeng.roses.core.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
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
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.chain.service.PlatformAddressService;
import cn.stylefeng.guns.modular.chain.wrapper.PlatformAddressWrapper;
import cn.stylefeng.guns.modular.chain.entity.PlatformAddress;
import cn.stylefeng.guns.modular.chain.constant.PlatformAddressMap;

/**
 * 平台地址控制器
 *
 * @author yaying.liu
 * @Date 2020-03-16 09:31:12
 */
@Controller
@RequestMapping("/platformAddress")
public class PlatformAddressController extends BaseController
{

    private String PREFIX = "/modular/chain/platformAddress/";

    @Autowired
    private PlatformAddressService platformAddressService;

    /**
     * 跳转到平台地址首页
     */
    @RequestMapping("")
    public String index()
    {
        return PREFIX + "platformAddress.html";
    }

    /**
     * 跳转到添加平台地址
     */
    @RequestMapping("/platformAddress_add")
    public String platformAddressAdd()
    {
        return PREFIX + "platformAddress_add.html";
    }

    /**
     * 跳转到修改平台地址
     */
    @RequestMapping("/platformAddress_edit")
    public String platformAddressEdit(Long platformAddressId, Model model)
    {
        PlatformAddress condition = this.platformAddressService.getById(platformAddressId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "platformAddress_edit.html";
    }

    /**
     * 平台地址详情
     */
    @RequestMapping(value = "/detail/{platformAddressId}")
    @ResponseBody
    public Object detail(@PathVariable("platformAddressId") Long platformAddressId)
    {
        PlatformAddress entity = platformAddressService.getById(platformAddressId);
        //  PlatformAddressDto conditionDto = new PlatformAddressDto();
        //  BeanUtil.copyProperties(entity, conditionDto);
        //  return conditionDto;
        return entity;
    }


    /**
     * 查询平台地址列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition)
    {
        //根据条件查询
        Page<Map<String, Object>> result = platformAddressService.selectByCondition(condition);
        Page wrapped = new PlatformAddressWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑平台地址
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(PlatformAddress platformAddress)
    {
        if ("EOS".equals(platformAddress.getType()) && StringUtils.isBlank(platformAddress.getRemark())){
            return ResponseData.error("请填写Memo值");
        }
        platformAddressService.updateById(platformAddress);
        return new SuccessResponseData(200,"修改成功",null);
    }

    /**
     * 添加平台地址
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加平台地址", key = "name", dict = PlatformAddressMap.class)
    @ResponseBody
    public ResponseData add(PlatformAddress platformAddress, BindingResult result)
    {
        if (result.hasErrors())
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (platformAddress == null)
        {
            return ResponseData.error("参数不能为空");
        }
        this.platformAddressService.addPlatformAddress(platformAddress);
        return SUCCESS_TIP;
    }

    /**
     * 删除平台地址
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除平台地址", key = "platformAddressId", dict = PlatformAddressMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long platformAddressId)
    {
        if (ToolUtil.isEmpty(platformAddressId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.platformAddressService.deletePlatformAddress(platformAddressId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{platformAddressId}")
    @ResponseBody
    public Object content(@PathVariable("platformAddressId") Long id)
    {
        PlatformAddress platformAddress = platformAddressService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(platformAddress);
        return super.warpObject(new PlatformAddressWrapper(stringObjectMap));
    }
}