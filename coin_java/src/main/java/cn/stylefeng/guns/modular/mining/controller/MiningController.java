package cn.stylefeng.guns.modular.mining.controller;

import cn.stylefeng.guns.modular.mining.entity.MiningOrder;
import cn.stylefeng.guns.modular.mining.entity.MiningOrderDetail;
import cn.stylefeng.guns.modular.mining.service.MiningOrderDetailService;
import cn.stylefeng.guns.modular.mining.service.MiningOrderService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.List;
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.mining.service.MiningService;
import cn.stylefeng.guns.modular.mining.wrapper.MiningWrapper;
import cn.stylefeng.guns.modular.mining.entity.Mining;
import cn.stylefeng.guns.modular.mining.constant.MiningMap;

/**
 * 矿机控制器
 *
 * @author yaying.liu
 * @since 2022-06-07 13:24:40
 */
@Controller
@RequestMapping("/mining")
public class MiningController extends BaseController {

    private String PREFIX = "/modular/mining/mining/";

     @Autowired
     private MiningService miningService;
     @Autowired
     private MiningOrderService miningOrderService;
     @Autowired
     private MiningOrderDetailService miningOrderDetailService;

    /**
     * 跳转到矿机首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "mining.html";
    }

    /**
     * 跳转到添加矿机
     */
    @RequestMapping("/mining_add")
    public String miningAdd() {
        return PREFIX + "mining_add.html";
    }

    /**
     * 跳转到修改矿机
     */
    @RequestMapping("/mining_edit")
    public String miningEdit(Long miningId, Model model) {
        Mining condition = this.miningService.getById(miningId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "mining_edit.html";
    }

    /**
     * 矿机详情
     */
    @RequestMapping(value = "/detail/{miningId}")
    @ResponseBody
    public Object detail(@PathVariable("miningId") Long miningId) {
        Mining entity = miningService.getById(miningId);
      //  MiningDto conditionDto = new MiningDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询矿机列表
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
        Page<Map<String, Object>> result = miningService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new MiningWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑矿机
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "miningId", dict = MiningMap.class)
    @ResponseBody
    public ResponseData edit(Mining mining) {
        miningService.updateById(mining);
        return SUCCESS_TIP;
    }

    /**
     * 添加矿机
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加矿机", key = "name", dict = MiningMap.class)
    @ResponseBody
    public ResponseData add(Mining mining, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (mining == null) {
            return ResponseData.error("参数不能为空");
        }
        this.miningService.addMining(mining);
        return SUCCESS_TIP;
    }

    /**
     * 删除矿机
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除矿机", key = "miningId", dict = MiningMap.class)
    @ResponseBody
    @Transactional
    public ResponseData delete(@RequestParam Long miningId) {
        if (ToolUtil.isEmpty(miningId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        List<MiningOrder> miningOrders = miningOrderService.list(new QueryWrapper<>(new MiningOrder()).eq("mining_id", miningId));
        if(miningOrders!=null && miningOrders.size()>0){
            for (MiningOrder miningOrder : miningOrders) {
                miningOrderDetailService.remove(new UpdateWrapper<>(new MiningOrderDetail()).eq("mining_order_id",miningOrder.getMiningOrderId()));
            }
        }
        miningOrderService.remove(new UpdateWrapper<>(new MiningOrder()).eq("mining_id",miningId));

        this.miningService.deleteMining(miningId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{miningId}")
    @ResponseBody
    public Object content(@PathVariable("miningId") Long id) {
        Mining mining = miningService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(mining);
        return super.warpObject(new MiningWrapper(stringObjectMap));
    }
}
