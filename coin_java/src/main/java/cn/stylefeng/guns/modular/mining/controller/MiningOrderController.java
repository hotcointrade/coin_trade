package cn.stylefeng.guns.modular.mining.controller;

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
import cn.stylefeng.guns.modular.mining.service.MiningOrderService;
import cn.stylefeng.guns.modular.mining.wrapper.MiningOrderWrapper;
import cn.stylefeng.guns.modular.mining.entity.MiningOrder;
import cn.stylefeng.guns.modular.mining.constant.MiningOrderMap;

/**
 * 矿机订单控制器
 *
 * @author yaying.liu
 * @since 2022-06-08 21:02:12
 */
@Controller
@RequestMapping("/miningOrder")
public class MiningOrderController extends BaseController {

    private String PREFIX = "/modular/mining/miningOrder/";

     @Autowired
     private MiningOrderService miningOrderService;

    /**
     * 跳转到矿机订单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "miningOrder.html";
    }

    /**
     * 跳转到添加矿机订单
     */
    @RequestMapping("/miningOrder_add")
    public String miningOrderAdd() {
        return PREFIX + "miningOrder_add.html";
    }

    /**
     * 跳转到修改矿机订单
     */
    @RequestMapping("/miningOrder_edit")
    public String miningOrderEdit(Long miningOrderId, Model model) {
        MiningOrder condition = this.miningOrderService.getById(miningOrderId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "miningOrder_edit.html";
    }

    /**
     * 矿机订单详情
     */
    @RequestMapping(value = "/detail/{miningOrderId}")
    @ResponseBody
    public Object detail(@PathVariable("miningOrderId") Long miningOrderId) {
        MiningOrder entity = miningOrderService.getById(miningOrderId);
      //  MiningOrderDto conditionDto = new MiningOrderDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询矿机订单列表
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
        Page<Map<String, Object>> result = miningOrderService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new MiningOrderWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑矿机订单
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "miningOrderId", dict = MiningOrderMap.class)
    @ResponseBody
    public ResponseData edit(MiningOrder miningOrder) {
        miningOrderService.updateById(miningOrder);
        return SUCCESS_TIP;
    }

    /**
     * 添加矿机订单
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加矿机订单", key = "name", dict = MiningOrderMap.class)
    @ResponseBody
    public ResponseData add(MiningOrder miningOrder, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (miningOrder == null) {
            return ResponseData.error("参数不能为空");
        }
        this.miningOrderService.addMiningOrder(miningOrder);
        return SUCCESS_TIP;
    }

    /**
     * 删除矿机订单
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除矿机订单", key = "miningOrderId", dict = MiningOrderMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long miningOrderId) {
        if (ToolUtil.isEmpty(miningOrderId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.miningOrderService.deleteMiningOrder(miningOrderId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{miningOrderId}")
    @ResponseBody
    public Object content(@PathVariable("miningOrderId") Long id) {
        MiningOrder miningOrder = miningOrderService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(miningOrder);
        return super.warpObject(new MiningOrderWrapper(stringObjectMap));
    }
}