package cn.stylefeng.guns.modular.chain.controller;

import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.roses.core.base.controller.BaseController;
import org.apache.commons.collections.map.HashedMap;
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

import java.math.BigDecimal;
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.chain.service.RechargeService;
import cn.stylefeng.guns.modular.chain.wrapper.RechargeWrapper;
import cn.stylefeng.guns.modular.chain.entity.Recharge;
import cn.stylefeng.guns.modular.chain.constant.RechargeMap;

/**
 * 充币记录控制器
 *
 * @author yaying.liu
 * @Date 2020-01-14 16:00:54
 */
@Controller
@RequestMapping("/recharge")
public class RechargeController extends BaseController {

    private String PREFIX = "/modular/chain/recharge/";

     @Autowired
     private RechargeService rechargeService;

    /**
     * 跳转到充币记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "recharge.html";
    }

    /**
     * 跳转到添加充币记录
     */
    @RequestMapping("/recharge_add")
    public String rechargeAdd() {
        return PREFIX + "recharge_add.html";
    }

    /**
     * 跳转到修改充币记录
     */
    @RequestMapping("/recharge_edit")
    public String rechargeEdit(Long rechargeId, Model model) {
        Recharge condition = this.rechargeService.getById(rechargeId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "recharge_edit.html";
    }

    /**
     * 充币记录详情
     */
    @RequestMapping(value = "/detail/{rechargeId}")
    @ResponseBody
    public Object detail(@PathVariable("rechargeId") Long rechargeId) {
        Recharge entity = rechargeService.getById(rechargeId);
      //  RechargeDto conditionDto = new RechargeDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询充币记录列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition
                        , @RequestParam(required = false) String txHash,
                       @RequestParam(required = false) String type,
                       @RequestParam(required = false) String timeLimit) {

        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Map<String,Object> map=new HashedMap();
        map.put("condition",condition);
        map.put("txHash",txHash);
        map.put("type",type);
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        map.put("memberId",memberId);
        map.put("recommendIds",recommendIds);
        //根据条件查询
        Page<Map<String, Object>> result = rechargeService.selectByCondition(map);
        Page wrapped = new RechargeWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    @RequestMapping("/total")
    @ResponseBody
    public Object total(@RequestParam(required = false) String condition
                        , @RequestParam(required = false) String txHash,
                       @RequestParam(required = false) String type,
                       @RequestParam(required = false) String timeLimit) {

        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Map<String,Object> map=new HashedMap();
        map.put("condition",condition);
        map.put("txHash",txHash);
        map.put("type",type);
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        map.put("memberId",memberId);
        map.put("recommendIds",recommendIds);
        return rechargeService.getBaseMapper().total(map);
    }


    /**
     * 编辑充币记录
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "rechargeId", dict = RechargeMap.class)
    @ResponseBody
    public ResponseData edit(Recharge recharge) {
        rechargeService.updateById(recharge);
        return SUCCESS_TIP;
    }

    /**
     * 添加充币记录
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加充币记录", key = "name", dict = RechargeMap.class)
    @ResponseBody
    public ResponseData add(Recharge recharge, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (recharge == null) {
            return ResponseData.error("参数不能为空");
        }
        this.rechargeService.addRecharge(recharge);
        return SUCCESS_TIP;
    }

    /**
     * 删除充币记录
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除充币记录", key = "rechargeId", dict = RechargeMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long rechargeId) {
        if (ToolUtil.isEmpty(rechargeId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.rechargeService.deleteRecharge(rechargeId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{rechargeId}")
    @ResponseBody
    public Object content(@PathVariable("rechargeId") Long id) {
        Recharge recharge = rechargeService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(recharge);
        return super.warpObject(new RechargeWrapper(stringObjectMap));
    }

    /**
     * 审核通过
     *
     * @param id
     * @return
     */
    @RequestMapping("/pass")
    @ResponseBody
    public Object pass(Long id, BigDecimal actualPrice) {
        if (id == null || id<0){
            return ResponseData.error("审核失败");
        }
        if (actualPrice == null || actualPrice.compareTo(BigDecimal.ZERO) <=0){
            return ResponseData.error("实付金额不能小于等于0");
        }
        Recharge entity = rechargeService.getById(id);
        //审核中
        if (entity.getStatus().equals(ProConst.WithdrawStatusEnum.CHECK.getCode()))
        {
            entity.setActualPrice(actualPrice);
          return   rechargeService.pass(entity);
        }

        return SUCCESS_TIP;
    }



    /**
     * 审核失败
     *
     * @param id
     * @return
     */
    @RequestMapping("/fail")
    @ResponseBody
    public Object fail(Long id) throws Exception
    {
        Recharge entity = rechargeService.getById(id);
        //审核中
        if (entity.getStatus().equals(ProConst.WithdrawStatusEnum.CHECK.getCode()))
        {
            return  rechargeService.fail(entity);
        }
        return SUCCESS_TIP;
    }



}