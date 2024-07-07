package cn.stylefeng.guns.modular.otc.controller;

import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
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
import cn.stylefeng.guns.modular.otc.service.BillService;
import cn.stylefeng.guns.modular.otc.wrapper.BillWrapper;
import cn.stylefeng.guns.modular.otc.entity.Bill;
import cn.stylefeng.guns.modular.otc.constant.BillMap;

/**
 * 交易结算订单控制器
 *
 * @author yaying.liu
 * @since 2020-07-13 15:03:39
 */
@Controller
@RequestMapping("/bill")
public class BillController extends BaseController
{

    private String PREFIX = "/modular/otc/bill/";

    @Autowired
    private BillService billService;

    /**
     * 跳转到交易结算订单首页
     */
    @RequestMapping("")
    public String index()
    {
        return PREFIX + "bill.html";
    }

    /**
     * 跳转到申诉订单首页
     */
    @RequestMapping("/toPass")
    public String toPass(Long billId, Model model)
    {
        Bill condition = this.billService.getById(billId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "toPass.html";
    }


    /**
     * 跳转到申诉订单首页
     */
    @RequestMapping("/1")
    public String appeal()
    {
        return PREFIX + "appeal.html";
    }

    /**
     * 跳转到添加交易结算订单
     */
    @RequestMapping("/bill_add")
    public String billAdd()
    {
        return PREFIX + "bill_add.html";
    }

    /**
     * 跳转到修改交易结算订单
     */
    @RequestMapping("/bill_edit")
    public String billEdit(Long billId, Model model)
    {
        Bill condition = this.billService.getById(billId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "bill_edit.html";
    }

    /**
     * 交易结算订单详情
     */
    @RequestMapping(value = "/detail/{billId}")
    @ResponseBody
    public Object detail(@PathVariable("billId") Long billId)
    {
        Bill entity = billService.getById(billId);
        //  BillDto conditionDto = new BillDto();
        //  BeanUtil.copyProperties(entity, conditionDto);
        //  return conditionDto;
        return entity;
    }


    /**
     * 查询交易结算订单列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition, @RequestParam(required = false) String timeLimit
            , @RequestParam(required = false) String buyAccount
            , @RequestParam(required = false) String buyName
            , @RequestParam(required = false) String sellAccount
            , @RequestParam(required = false) String sellName
            , @RequestParam(required = false) String payMethod
            , @RequestParam(required = false) String orderNo
            , @RequestParam(required = false) String markNo
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
        Page<Map<String, Object>> result = billService.selectByCondition(condition, beginTime,
                endTime, status,buyAccount,buyName,sellAccount,sellName,payMethod,orderNo,markNo,memberId,recommendIds);
        Page wrapped = new BillWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }


    /**
     * 查询交易结算订单列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list1")
    @ResponseBody
    public Object list1(@RequestParam(required = false) String condition
            , @RequestParam(required = false) String timeLimit
            , @RequestParam(required = false) String buyAccount
            , @RequestParam(required = false) String buyName
            , @RequestParam(required = false) String sellAccount
            , @RequestParam(required = false) String sellName
            , @RequestParam(required = false) String payMethod
            , @RequestParam(required = false) String orderNo
            , @RequestParam(required = false) String markNo
            , @RequestParam(required = false) String appealStatus
            , @RequestParam(required = false) String duty

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
        String status = ProConst.BillStatus.APPEAL.code;
        Page<Map<String, Object>> result = billService.selectAppeal(condition, beginTime, endTime, status,buyAccount,buyName,sellAccount,sellName,payMethod,orderNo,markNo,appealStatus,duty,memberId,recommendIds);
        Page wrapped = new BillWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }


    /**
     * 编辑交易结算订单
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "billId", dict = BillMap.class)
    @ResponseBody
    public ResponseData edit(Bill bill)
    {
        billService.updateById(bill);
        return SUCCESS_TIP;
    }

    /**
     * 添加交易结算订单
     */
    @RequestMapping("/add")
    //  @BussinessLog(value = "添加交易结算订单", key = "name", dict = BillMap.class)
    @ResponseBody
    public ResponseData add(Bill bill, BindingResult result)
    {
        if (result.hasErrors())
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (bill == null)
        {
            return ResponseData.error("参数不能为空");
        }
        this.billService.addBill(bill);
        return SUCCESS_TIP;
    }

    /**
     * 删除交易结算订单
     */
    @RequestMapping("/delete")
    // @BussinessLog(value = "删除交易结算订单", key = "billId", dict = BillMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long billId)
    {
        if (ToolUtil.isEmpty(billId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.billService.deleteBill(billId);
        return SUCCESS_TIP;
    }

    /**
     * 买方责任
     */
    @RequestMapping("/dutyBuy")
    @ResponseBody
    public ResponseData dutyBuy(@RequestParam Long billId)
    {
        if (ToolUtil.isEmpty(billId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.billService.dutyBuy(billId);
        return SUCCESS_TIP;
    }

    /**
     * 卖方责任
     */
    @RequestMapping("/dutySell")
    @ResponseBody
    public ResponseData dutySell(@RequestParam Long billId)
    {
        if (ToolUtil.isEmpty(billId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.billService.dutySell(billId);
        return SUCCESS_TIP;
    }


    /**
     * 双方无责
     */
    @RequestMapping("/dutyNull")
    @ResponseBody
    public ResponseData dutyNull(@RequestParam Long billId)
    {
        if (ToolUtil.isEmpty(billId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.billService.dutyNull(billId);
        return SUCCESS_TIP;
    }

    /**
     * 放币
     */
    @RequestMapping("/pass")
    @ResponseBody
    public ResponseData pass(@RequestParam Long billId)
    {
        if (ToolUtil.isEmpty(billId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        return this.billService.pass(billId);
    }
    /**
     * 放币 属于特殊放币
     */
    @RequestMapping("/pass2")
    @ResponseBody
    public ResponseData pass2(@RequestParam Long billId)
    {
        if (ToolUtil.isEmpty(billId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        return this.billService.pass2(billId);
    }


    /**
     * 取消
     */
    @RequestMapping("/fail")
    @ResponseBody
    public ResponseData fail(@RequestParam Long billId)
    {
        if (ToolUtil.isEmpty(billId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        return this.billService.fail(billId);
    }
    /**
     * 取消
     */
    @RequestMapping("/fail2")
    @ResponseBody
    public ResponseData fail2(@RequestParam Long billId)
    {
        if (ToolUtil.isEmpty(billId))
        {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        return this.billService.fail2(billId);
    }


    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{billId}")
    @ResponseBody
    public Object content(@PathVariable("billId") Long id)
    {
        Bill bill = billService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(bill);
        return super.warpObject(new BillWrapper(stringObjectMap));
    }
}
