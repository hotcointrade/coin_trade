package cn.stylefeng.guns.modular.chain.controller;

import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.fin.service.WalletService;
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

import java.util.Map;

import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.chain.service.WithdrawService;
import cn.stylefeng.guns.modular.chain.wrapper.WithdrawWrapper;
import cn.stylefeng.guns.modular.chain.entity.Withdraw;
import cn.stylefeng.guns.modular.chain.constant.WithdrawMap;

/**
 * 提币信息控制器
 *
 * @author yaying.liu
 * @Date 2019-12-10 20:05:21
 */
@Controller
@RequestMapping("/withdraw")
public class WithdrawController extends BaseController
{

    private String PREFIX = "/modular/chain/withdraw/";



    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private WalletService walletService;





    /**
     * 跳转到提币信息首页
     */
    @RequestMapping("")
    public String index()
    {
        return PREFIX + "withdraw.html";
    }

    /**
     * 跳转到添加提币信息
     */
    @RequestMapping("/withdraw_add")
    public String withdrawAdd()
    {
        return PREFIX + "withdraw_add.html";
    }

    /**
     * 跳转到修改提币信息
     */
    @RequestMapping("/withdraw_edit")
    public String withdrawEdit(Long withdrawId, Model model)
    {
        Withdraw condition = this.withdrawService.getById(withdrawId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "withdraw_edit.html";
    }

    /**
     * 提币信息详情
     */
    @RequestMapping(value = "/detail/{withdrawId}")
    @ResponseBody
    public Object detail(@PathVariable("withdrawId") Long withdrawId)
    {
        Withdraw entity = withdrawService.getById(withdrawId);
        //  WithdrawDto conditionDto = new WithdrawDto();
        //  BeanUtil.copyProperties(entity, conditionDto);
        //  return conditionDto;
        return entity;
    }


    /**
     * 查询提币信息列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition
            ,@RequestParam(required = false) String timeLimit
            ,@RequestParam(required = false) String txHash
            ,@RequestParam(required = false) String type
            ,@RequestParam(required = false) String toAddress
            ,@RequestParam(required = false) String status)
    {
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
        map.put("toAddress",toAddress);
        map.put("status",status);
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
        Page<Map<String, Object>> result = withdrawService.selectByCondition(map);
        Page wrapped = new WithdrawWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

     /**
     * 查询提币信息列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/total")
    @ResponseBody
    public Object total(@RequestParam(required = false) String condition
                        ,@RequestParam(required = false) String timeLimit
                        ,@RequestParam(required = false) String txHash
                        ,@RequestParam(required = false) String type
                        ,@RequestParam(required = false) String toAddress
                        ,@RequestParam(required = false) String status
    )
    {

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
        map.put("toAddress",toAddress);
        map.put("status",status);
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
        return withdrawService.getBaseMapper().total(map);
    }





    /**
     * 审核通过
     *
     * @param id
     * @return
     */
    @RequestMapping("/pass")
    @ResponseBody
    public Object pass(Long id) {
        Withdraw entity = withdrawService.getById(id);

        //审核中
        if (entity.getStatus().equals(ProConst.WithdrawStatusEnum.CHECK.getCode()))
        {
            return   withdrawService.pass(entity);
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
        Withdraw entity = withdrawService.getById(id);
        //审核中
        if (entity.getStatus().equals(ProConst.WithdrawStatusEnum.CHECK.getCode()))
        {
            return  withdrawService.fail(entity);
        }
        return SUCCESS_TIP;
    }




    /**
     * 编辑提币信息
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "withdrawId", dict = WithdrawMap.class)
    @ResponseBody
    public ResponseData edit(Withdraw withdraw)
    {
        withdrawService.updateById(withdraw);
        return SUCCESS_TIP;
    }

    /**
     * 添加提币信息
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加提币信息", key = "name", dict = WithdrawMap.class)
    @ResponseBody
    public ResponseData add(Withdraw withdraw, BindingResult result)
    {
        if (result.hasErrors())
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (withdraw == null)
        {
            return ResponseData.error("参数不能为空");
        }
        this.withdrawService.addWithdraw(withdraw);
        return SUCCESS_TIP;
    }

    /**
     * 删除提币信息
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除提币信息", key = "withdrawId", dict = WithdrawMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long withdrawId)
    {
        if (ToolUtil.isEmpty(withdrawId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.withdrawService.deleteWithdraw(withdrawId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{withdrawId}")
    @ResponseBody
    public Object content(@PathVariable("withdrawId") Long id)
    {
        Withdraw withdraw = withdrawService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(withdraw);
        return super.warpObject(new WithdrawWrapper(stringObjectMap));
    }
}