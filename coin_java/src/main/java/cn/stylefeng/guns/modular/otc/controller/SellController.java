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
import cn.stylefeng.guns.modular.otc.service.SellService;
import cn.stylefeng.guns.modular.otc.wrapper.SellWrapper;
import cn.stylefeng.guns.modular.otc.entity.Sell;
import cn.stylefeng.guns.modular.otc.constant.SellMap;

/**
 * 出售挂单订单控制器
 *
 * @author yaying.liu
 * @since 2020-07-13 14:53:27
 */
@Controller
@RequestMapping("/sell")
public class SellController extends BaseController {

    private String PREFIX = "/modular/otc/sell/";

     @Autowired
     private SellService sellService;

    /**
     * 跳转到出售挂单订单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "sell.html";
    }

    /**
     * 跳转到添加出售挂单订单
     */
    @RequestMapping("/sell_add")
    public String sellAdd() {
        return PREFIX + "sell_add.html";
    }

    /**
     * 跳转到修改出售挂单订单
     */
    @RequestMapping("/sell_edit")
    public String sellEdit(Long sellId, Model model) {
        Sell condition = this.sellService.getById(sellId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "sell_edit.html";
    }

    /**
     * 出售挂单订单详情
     */
    @RequestMapping(value = "/detail/{sellId}")
    @ResponseBody
    public Object detail(@PathVariable("sellId") Long sellId) {
        Sell entity = sellService.getById(sellId);
      //  SellDto conditionDto = new SellDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }

    @RequestMapping("/cancel")
    @ResponseBody
    public ResponseData cancel(@RequestParam Long sellId)
    {
        if (ToolUtil.isEmpty(sellId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.sellService.cancel(sellId);
        return SUCCESS_TIP;
    }

    /**
     * 查询出售挂单订单列表
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
    ) {
        //根据条件查询
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
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
        Page<Map<String, Object>> result = sellService.selectByCondition(condition, beginTime, endTime,
                nickName, orderNo, coin, payMethod, status,memberId,recommendIds);
        Page wrapped = new SellWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑出售挂单订单
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "sellId", dict = SellMap.class)
    @ResponseBody
    public ResponseData edit(Sell sell) {
        sellService.updateById(sell);
        return SUCCESS_TIP;
    }

    /**
     * 添加出售挂单订单
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加出售挂单订单", key = "name", dict = SellMap.class)
    @ResponseBody
    public ResponseData add(Sell sell, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (sell == null) {
            return ResponseData.error("参数不能为空");
        }
        this.sellService.addSell(sell);
        return SUCCESS_TIP;
    }

    /**
     * 删除出售挂单订单
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除出售挂单订单", key = "sellId", dict = SellMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long sellId) {
        if (ToolUtil.isEmpty(sellId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.sellService.deleteSell(sellId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{sellId}")
    @ResponseBody
    public Object content(@PathVariable("sellId") Long id) {
        Sell sell = sellService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(sell);
        return super.warpObject(new SellWrapper(stringObjectMap));
    }
}