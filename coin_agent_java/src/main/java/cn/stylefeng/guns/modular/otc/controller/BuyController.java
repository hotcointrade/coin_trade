package cn.stylefeng.guns.modular.otc.controller;

import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.F;
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
import cn.stylefeng.guns.modular.otc.service.BuyService;
import cn.stylefeng.guns.modular.otc.wrapper.BuyWrapper;
import cn.stylefeng.guns.modular.otc.entity.Buy;
import cn.stylefeng.guns.modular.otc.constant.BuyMap;

/**
 * 购买挂单订单控制器
 *
 * @author yaying.liu
 * @since 2020-07-13 14:52:41
 */
@Controller
@RequestMapping("/buy")
public class BuyController extends BaseController
{

    private String PREFIX = "/modular/otc/buy/";

    @Autowired
    private BuyService buyService;

    /**
     * 跳转到购买挂单订单首页
     */
    @RequestMapping("")
    public String index()
    {
        return PREFIX + "buy.html";
    }

    /**
     * 跳转到添加购买挂单订单
     */
    @RequestMapping("/buy_add")
    public String buyAdd()
    {
        return PREFIX + "buy_add.html";
    }

    /**
     * 跳转到修改购买挂单订单
     */
    @RequestMapping("/buy_edit")
    public String buyEdit(Long buyId, Model model)
    {
        Buy condition = this.buyService.getById(buyId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "buy_edit.html";
    }

    /**
     * 购买挂单订单详情
     */
    @RequestMapping(value = "/detail/{buyId}")
    @ResponseBody
    public Object detail(@PathVariable("buyId") Long buyId)
    {
        Buy entity = buyService.getById(buyId);
        //  BuyDto conditionDto = new BuyDto();
        //  BeanUtil.copyProperties(entity, conditionDto);
        //  return conditionDto;
        return entity;
    }


    /**
     * 查询购买挂单订单列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition
            , @RequestParam(required = false) String timeLimit
            , @RequestParam(required = false) String nickName
            , @RequestParam(required = false) String orderNo
            , @RequestParam(required = false) String coin
            , @RequestParam(required = false) String payMethod
            , @RequestParam(required = false) String status

    )
    {
        //根据条件查询
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit))
        {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        Page<Map<String, Object>> result = buyService.selectByCondition(condition, beginTime, endTime,
                nickName, orderNo, coin, payMethod, status,memberId,recommendIds);
        Page wrapped = new BuyWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑购买挂单订单
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "buyId", dict = BuyMap.class)
    @ResponseBody
    public ResponseData edit(Buy buy)
    {
        buyService.updateById(buy);
        return SUCCESS_TIP;
    }

    /**
     * 添加购买挂单订单
     */
    @RequestMapping("/add")
    //  @BussinessLog(value = "添加购买挂单订单", key = "name", dict = BuyMap.class)
    @ResponseBody
    public ResponseData add(Buy buy, BindingResult result)
    {
        if (result.hasErrors())
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (buy == null)
        {
            return ResponseData.error("参数不能为空");
        }
        this.buyService.addBuy(buy);
        return SUCCESS_TIP;
    }

    /**
     * 删除购买挂单订单
     */
    @RequestMapping("/delete")
    // @BussinessLog(value = "删除购买挂单订单", key = "buyId", dict = BuyMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long buyId)
    {
        if (ToolUtil.isEmpty(buyId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.buyService.deleteBuy(buyId);
        return SUCCESS_TIP;
    }

    @RequestMapping("/cancel")
    // @BussinessLog(value = "删除购买挂单订单", key = "buyId", dict = BuyMap.class)
    @ResponseBody
    public ResponseData cancel(@RequestParam Long buyId)
    {
        if (ToolUtil.isEmpty(buyId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.buyService.cancel(buyId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{buyId}")
    @ResponseBody
    public Object content(@PathVariable("buyId") Long id)
    {
        Buy buy = buyService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(buy);
        return super.warpObject(new BuyWrapper(stringObjectMap));
    }
}