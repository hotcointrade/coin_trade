package cn.stylefeng.guns.modular.app.controller;

import cn.stylefeng.guns.modular.app.service.GasService;
import cn.stylefeng.guns.modular.base.util.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.stylefeng.guns.modular.app.dto.otc.AppealDto;
import cn.stylefeng.guns.modular.app.dto.otc.BuyDto;
import cn.stylefeng.guns.modular.app.dto.otc.SellDto;
import cn.stylefeng.guns.modular.app.dto.otc.TradeDto;
import cn.stylefeng.guns.modular.app.service.OtcService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 二期接口
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class V2Api {

    private OtcService otcService;

    private GasService gasService;

    @Autowired
    public V2Api(OtcService otcService, GasService gasService) {
        this.otcService = otcService;
        this.gasService = gasService;
    }

    /**
     * 法币交易管理页面信息
     * @param token token
     * @return Result
     */
    @PostMapping("/legalMgrPage")
    public Result legalMgrPage(@RequestHeader("token") String token) {
        return otcService.legalMgrPage(token);
    }

    /**
     * 法币身份验证（进入法币模块需要使用该接口校验）
     * @return Result
     */
    @PostMapping("/verifySwitch")
    public Result verifySwitch(@RequestHeader("token") String token) {
        return otcService.verifySwitch(token);
    }

    /**
     * 押金缴纳页面
     *
     * @param token token
     * @return Result
     */
    @PostMapping("/depositPage")
    public Result depositPage(@RequestHeader("token") String token) {
        return otcService.depositPage(token);
    }


    /**
     * 提交申请承兑商，押金缴纳
     * @param token  token
     * @param type   类型 USDT 、
     * @param payPwd 交易密码
     * @return Result
     */
    @PostMapping("/deposit")
    public Result deposit(@RequestHeader("token") String token, @RequestParam String type, @RequestParam String payPwd) {
        return otcService.deposit(token, type, payPwd);
    }


  /*

        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================

                                        待同步接口



        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================

     */

    /**
     * 补缴押金页面信息
     *
     * @return Result
     */
    @PostMapping("makeUpDepositPage")
    public Result makeUpDepositPage(@RequestHeader("token") String token) {
        return otcService.makeUpDepositPage(token);
    }

    /**
     * 补缴押金
     *
     * @return Result
     */
    @PostMapping("makeUpDeposit")
    public Result makeUpDeposit(@RequestHeader("token") String token, @RequestParam String payPwd) {
        return otcService.makeUpDeposit(token, payPwd);
    }

    /**
     * 退还押金页面信息
     *
     * @param token token
     * @return Result
     */
    @PostMapping("backDepositPage")
    public Result backDepositPage(@RequestHeader("token") String token) {
        return otcService.backDepositPage(token);
    }

    /**
     * 退还押金
     *
     * @param token token
     * @return Result
     */
    @PostMapping("backDeposit")
    public Result backDeposit(@RequestHeader("token") String token) {
        return otcService.backDeposit(token);
    }


    /**
     * 挂单买卖-挂单购买
     * @param token token
     * @param dto   dto
     * @return Result
     */
    @PostMapping("otcBuy")
    public Result otcBuy(@RequestHeader("token") String token, @Valid BuyDto dto) {
        return otcService.otcBuy(token, dto);
    }


    /**
     * 挂单买卖-挂单出售
     * @param token token
     * @param dto   dto
     * @return Result
     */
    @PostMapping("otcSell")
    public Result otcSell(@RequestHeader("token") String token, @Valid SellDto dto) {
        return otcService.otcSell(token, dto);
    }

    /**
     * 挂单买卖-挂单出售-收款方式
     *
     * @param token token
     * @return Result
     */
    @PostMapping("otcPayMethod")
    public Result otcPayMethod(@RequestHeader("token") String token) {
        return otcService.otcPayMethod(token);
    }

    /**
     * 挂单买卖-挂单出售-可用资产（包含实名认证）
     *
     * @param token token
     * @param type  USDT
     * @return Result
     */
    @PostMapping("otcSellPage")
    public Result otcSellPage(@RequestHeader("token") String token, String type) {
        return otcService.otcSellPage(token, type);
    }


    /**
     * 法币交易（我要购买、我要出售）
     *
     * @param token token
     * @param dto   dto
     * @param page  page
     * @return Result
     */
    @PostMapping("tradeList")
    public Result tradeList(@RequestHeader("token") String token,
                            @Valid TradeDto dto,
                            Page page) {
        return otcService.tradeList(token, dto, page);
    }


    /**
     * 法币交易-下单购买
     *
     * @param token  token
     * @param sellId 订单id（sellId)
     * @param value  填入值
     * @param type   购买方式（按数量:0 按金额:1）
     * @return Result
     */
    @PostMapping("tradeBuy")
    public Result tradeBuy(@RequestHeader("token") String token,
                           @RequestParam Long sellId,
                           @RequestParam BigDecimal value,
                           @RequestParam int type,
                           String password) {
        return otcService.tradeBuy(token, sellId, value, type,password);
    }

    /**
     * 法币交易-下单购买-订单页面信息
     *
     * @param token   token
     * @param orderNo orderNo 订单号
     * @return Result
     */
    @PostMapping("tradeBuyWaitPage")
    public Result tradeBuyWaitPage(@RequestHeader("token") String token,
                                   @RequestParam String orderNo
    ) {
        return otcService.tradeBuyWaitPage(token, orderNo);
    }

    /**
     * 待付款页面-取消订单
     *
     * @param token   token
     * @param orderNo 订单号
     * @return Result
     */
    @PostMapping("tradeCancel")
    public Result tradeCancel(@RequestHeader("token") String token,
                              @RequestParam String orderNo
    ) {
        return otcService.tradeCancel(token, orderNo);
    }


    /**
     * 待付款页面-取消订单前调用(获取提示信息)
     *
     * @param token   token
     * @param orderNo orderNo
     * @return Result
     */
    @PostMapping("tradeCancelPre")
    public Result tradeCancelPre(@RequestHeader("token") String token, @RequestParam String orderNo) {
        return otcService.tradeCancelPre(token, orderNo);
    }


    /**
     * 待付款页面-已完成付款
     *
     * @param token   token
     * @param orderNo 订单号
     * @return Result
     */
    @PostMapping("tradePaid")
    public Result tradePaid(@RequestHeader("token") String token
            , @RequestParam String orderNo
    ) {
        return otcService.tradePaid(token, orderNo);
    }

    /**
     * 申诉/重新申诉（ 待付款页面-已完成付款-已付款-申诉）
     *
     * @param token     token
     * @param appealDto appealDto
     * @return Result
     */
    @PostMapping("tradeAppeal")
    public Result tradeAppeal(@RequestHeader("token") String token
            , @Valid AppealDto appealDto
    ) {
        return otcService.tradeAppeal(token, appealDto);
    }

    /**
     * 取消申诉
     *
     * @param token   token
     * @param orderNo orderNo
     * @return Result
     * @deprecated 不可取消申诉
     */
//    @PostMapping("tradeCancelAppeal")
    public Result tradeCancelAppeal(@RequestHeader("token") String token
            , @RequestParam String orderNo
    ) {
        return otcService.tradeCancelAppeal(token, orderNo);
    }


    /**
     * 法币交易-下单出售
     *
     * @param token     token
     * @param buyId     订单id（buyId)
     * @param value     填入值
     * @param type      出售方式（按数量:0 按金额:1）
     * @param paymentId 收款id
     * @param payPwd    交易密码
     * @return Result
     */
    @PostMapping("tradeSell")
    public Result tradeSell(@RequestHeader("token") String token,
                            @RequestParam Long buyId,
                            @RequestParam BigDecimal value,
                            @RequestParam int type,
                            @RequestParam Long paymentId,
                            @RequestParam String payPwd
    ) {
        return otcService.tradeSell(token, buyId, value, type, paymentId, payPwd);
    }


    /**
     * 我的订单-（ 购买订单 出售订单 ）
     *
     * @param token    token
     * @param pageType BUY:购买订单 SELL:出售订单
     * @param status   ("WAIT", "买家待付款") ("WAIT_COIN", "待放币、已付款") ("CANCEL", "已取消") ("FINISH", "已完成") ("APPEAL", "申述中")
     * @param page     page
     */
    @PostMapping("otcBillList")
    public Result otcBillList(@RequestHeader("token") String token, String pageType, String status, Page page) {
        return otcService.otcBillList(token, pageType, status, page);
    }

    /**
     * 我的订单- 放币
     *
     * @param token   token
     * @param orderNo 订单号
     * @param payPwd  交易密码
     * @return Result
     */
    @PostMapping("otcFinish")
    public Result otcFinish(@RequestHeader("token") String token, String orderNo, String payPwd) {
        return otcService.otcFinish(token, orderNo, payPwd);
    }


    /**
     * 我的订单(挂单 购买/出售 订单)
     *
     * @param token    token
     * @param pageType BUY:挂单购买订单 SELL:挂单出售订单
     * @param page     page
     */
    @PostMapping("otcOrderList")
    public Result otcOrderList(@RequestHeader("token") String token, @RequestParam(defaultValue = "BUY") String pageType, Page page) {
        return otcService.otcOrderList(token, pageType, page);
    }


    /**
     * 我的订单-挂单中-撤单
     *
     * @param token    token
     * @param pageType BUY:挂单购买订单 SELL:挂单出售订单
     * @param orderNo  订单号
     * @return Result
     */
    @PostMapping("cancelOrder")
    public Result cancelOrder(@RequestHeader("token") String token, @RequestParam String pageType, @RequestParam String orderNo) {
        return otcService.cancelOrder(token, pageType, orderNo);
    }

    //获取法币币种
    @RequestMapping("/contractOptionConfig")
    public Result getContractOptionConfig() {
        return otcService.getContractOptionConfig();
    }


    // ==== 锁仓挖矿接口

    /**
     * 锁仓挖矿页面
     * @param token token
     * @return Result
     */
    @PostMapping("lockPage")
    public Result lockPage(@RequestHeader("token") String token) {
        return gasService.lockPage(token);
    }


    /**
     * 我的锁仓
     * @param token  token
     * @param status status
     * @param page page
     * @return Result
     */
    @PostMapping("lockList")
    public Result lockList(@RequestHeader("token")String token,@RequestParam String status,Page page)
    {
        return gasService.lockList(token,status,page);
    }

    /**
     * 锁仓收益
     * @param token token
     * @param lockRecordId 锁仓记录id
     * @param page  page
     * @return Result
     */
    @PostMapping("lockProfit")
    public Result lockProfit(@RequestHeader("token")String token,@RequestParam Long lockRecordId,Page page)
    {
        return gasService.lockProfit(token,lockRecordId,page);
    }



     /**
     * 投入锁仓
     * @param token token
     * @param lockId 锁仓周期id
     * @param number 锁仓数量
     * @param payPwd 交易密码
     * @return Result
     */
    @PostMapping("doLock")
    public Result doLock(@RequestHeader("token")String token ,@RequestParam Long lockId,@RequestParam BigDecimal number,@RequestParam String payPwd)
    {
        return gasService.doLock(token,lockId,number,payPwd);
    }

    /**
     * 继续锁仓
     * @param token token
     * @param lockRecordId 锁仓记录id
     * @param payPwd 交易密码
     * @return Result
     */
    @PostMapping("continueLock")
    public Result continueLock(@RequestHeader("token")String token,@RequestParam Long lockRecordId,@RequestParam String payPwd){
        return gasService.continueLock(token,lockRecordId,payPwd);
    }

    /**
     * 赎回
     * @param token token
     * @param lockRecordId lockRecordId
     * @return Result
     */
    @PostMapping("unLock")
    public Result unLock(@RequestHeader("token")String token,@RequestParam Long lockRecordId){
        return gasService.unLock(token,lockRecordId);
    }

    /**
     * 赎回提示信息
     * @param token token
     * @param lockRecordId lockRecordId
     * @return Result
     */
    @PostMapping("unLockTip")
    public Result unLockTip(@RequestHeader("token")String token,@RequestParam Long lockRecordId){
        return gasService.unLockTip(token,lockRecordId);
    }




}
