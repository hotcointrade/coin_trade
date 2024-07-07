package cn.stylefeng.guns.modular.app.controller;

import cn.stylefeng.guns.modular.app.dto.*;
import cn.stylefeng.guns.modular.app.dto.e.*;
import cn.stylefeng.guns.modular.app.entity.CoinApply;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.AsyncService;
import cn.stylefeng.guns.modular.app.service.HomeService;
import cn.stylefeng.guns.modular.app.vo.*;
import cn.stylefeng.guns.modular.app.vo.e.ApiWalletVo;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.coin.constant.ContractOptionStatus;
import cn.stylefeng.guns.modular.coin.entity.ContractOption;
import cn.stylefeng.guns.modular.coin.entity.ContractOptionCoin;
import cn.stylefeng.guns.modular.coin.entity.ContractOptionOrder;
import cn.stylefeng.guns.modular.fin.entity.Loan;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * app入口
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class HomeApi {

    @Resource
    private HomeService homeService;

    @Resource
    private AsyncService asyncService;

    /**
     * 注册
     *
     * @param regVo regVo
     * @return Result
     */
    @PostMapping("/register")
    public Result register(@Valid RegVo regVo) {
        return homeService.register(regVo);
    }

    /**
     * 登录
     *
     * @param loginVo loginVo
     * @return Result
     * @return Result
     */
    @PostMapping("/login")
    public Result login(@Valid LoginVo loginVo) {
        return homeService.login(loginVo);
    }

    /**
     * 检查验证码是否正确
     *
     * @param account 账号
     * @param code    验证码
     * @return Result
     */
    @PostMapping("/checkSmsCode")
    public Result checkSmsCode(String account, String code, @RequestHeader("token") String token) {
        return homeService.checkSmsCode(account, code, token);
    }

    /**
     * 忘记密码
     *
     * @return Result
     */
    @RequestMapping("/forgetPwd")
    public Result forgetPwd(@Valid ForgetVo forgetVo) {
        return homeService.forgetPwd(forgetVo);
    }


    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result logout(@RequestHeader("token") String token) {
        return homeService.logout(token);
    }

    /**
     * 获取验证码
     *
     * @param code  区号
     * @param phone 电话
     * @param type  类型
     * @return Result
     */
    @PostMapping("/getMsg")
    public Result msg(@RequestHeader(value = "token", required = false, defaultValue = "-1") String token,
                      @RequestParam(required = false) String code,
                      @RequestParam(required = false) String phone,
                      @RequestParam(required = false) String account,
                      @RequestParam(value = "type", required = false, defaultValue = "1") Long type) {
        return homeService.getMsg(token, code, phone, type, account);
    }

    /**
     * 获取验证码
     *
     * @return Result
     */
    @PostMapping("/sysMsg")
    public Result sysMsg() {
        return homeService.sysMsg();
    }


    /**
     * 刷新token
     */
    @PostMapping("/refreshToken")
    public Result refreshToken(@RequestHeader("token") String token) {
        return homeService.refreshToken(token);
    }


    /**
     * 安全中心
     */
    @RequestMapping("/pwd")
    public Result pwd(@RequestHeader("token") String token, @Valid ApiPayPwdVo apiPayPwdVo) {
        return homeService.pwd(token, apiPayPwdVo);
    }

    /**
     * 修改法币交易昵称
     *
     * @param token
     * @param nickName 昵称
     * @return Result
     */
    @PostMapping("/nickName")
    public Result nickName(@RequestHeader("token") String token, String nickName) {
        return homeService.nickName(token, nickName);
    }


    /**
     * 签到
     */
    @PostMapping("/checkIn")
    public Result checkIn(@RequestHeader("token") String token) {
        return homeService.checkIn(token);
    }

    /**
     * 签到信息
     */
    @PostMapping("/checkInfo")
    public Result checkInfo(@RequestHeader("token") String token) {
        return homeService.checkInfo(token);
    }


    /**
     * 用户信息
     *
     * @param token token
     * @return Result
     */
    @PostMapping("/info")
    public Result info(@RequestHeader("token") String token) {
        return homeService.info(token);
    }

    /**
     * 手机绑定或邮箱绑定
     *
     * @param token token 用户唯一标识
     * @param dto   dto
     * @return Result
     */
    @PostMapping("/bindAccount")
    public Result bindAccount(@RequestHeader("token") String token, ApiBindAccountDto dto) {
        return homeService.bindAccount(token, dto);
    }

    /**
     * 实名认证
     *
     * @param token token
     * @param dto   dto
     * @return Result
     */
    @RequestMapping("/verify")
    public Result verify(@RequestHeader("token") String token, @Valid ApiVerifyDto dto) {
        return homeService.verify(token, dto);
    }

    /*
     * ========================================================================================================
     *
     *                                         项目业务接口
     *
     *
     * ========================================================================================================
     */

    /**
     * 收款方式列表
     *
     * @param token token
     * @param page  page
     * @return Result
     */
    @PostMapping("/cnyPayList")
    public Result cnyPayList(@RequestHeader("token") String token, Page page) {
        return homeService.cnyPayList(token, page);
    }


    /**
     * 添加收款方式
     *
     * @param token token
     * @param dto   dto
     * @return Result
     */
    @PostMapping("/cnyPayAdd")
    public Result cnyPayAdd(@RequestHeader("token") String token, @Valid ApiPayDto dto) {
        return homeService.cnyPayAdd(token, dto);
    }

    /**
     * /**
     * 删除收款方式
     *
     * @param token     token
     * @param paymentId paymentId
     * @return Result
     */
    @PostMapping("/cnyPayDel")
    public Result cnyPayDel(@RequestHeader("token") String token, Long paymentId) {
        return homeService.cnyPayDel(token, paymentId);
    }

    /**
     *  =============
     *      资产
     *  =============
     */
    /**
     * 资产信息
     *
     * @param token token
     * @param vo    vo
     * @return Result
     */
    @RequestMapping("/wallet")
    public Result wallet(@RequestHeader("token") String token, @Valid ApiWalletVo vo) {//
        return homeService.wallet(token, vo);
    }

    /**
     * 账户详情
     *
     * @return Result
     */
    @PostMapping("/walletDetail")
    public Result walletDetail(@RequestHeader("token") String token, Long id, String type) {
        return homeService.walletDetail(token, id, type);
    }


    /**
     * 充币地址 用户地址
     */
    @RequestMapping("/platformAddress")
    public Result platformAddress(@RequestHeader("token") String token, String symbol) {
        return homeService.platformAddress(token, symbol);
    }

    /**
     * 提交充值
     *
     * @param token
     * @param dto
     * @return
     */
    @RequestMapping("/recharge")
    public Result recharge(@RequestHeader("token") String token, ApiRechargeDto dto) {
        return homeService.recharge(token, dto);
    }


    /**
     * 提币页面信息
     */
    @RequestMapping("/withdrawPage")
    public Result withdrawPage(@RequestHeader("token") String token, String type) {
        return homeService.withdrawPage(token, type);
    }


    /**
     * 提币
     *
     * @param token token
     * @param vo    vo
     * @return Result
     * @throws Exception Exception
     */
    @RequestMapping("/withdrawCoin")
    public Result withdrawCoin(@RequestHeader("token") String token, @Valid ApiWithdrawCoinVo vo) throws Exception {
        return homeService.withdrawCoin(token, vo);
    }

    /**
     * 提币
     *
     * @param token token
     * @param vo    vo
     * @return Result
     * @throws Exception Exception
     */
    @RequestMapping("/withdrawCoinApp")
    public Result withdrawCoinApp(@RequestHeader("token") String token, @Valid ApiWithdrawCoinVo vo) throws Exception {
        return homeService.withdrawCoinApp(token, vo);
    }

    /**
     * 充币/提币 记录
     */
    @PostMapping("/coinRecord")
    public Result coinRecord(@RequestHeader("token") String token, String type, Page page
            , @RequestParam(required = false, value = "symbol") String symbol) {
        return homeService.coinRecord(token, type, page, symbol);
    }

    /**
     * 添加/删除用户钱包地址
     *
     * @param token token
     * @param dto   dto
     * @return Result
     */
    @PostMapping("/coin")
    public Result coin(@RequestHeader("token") String token, ApiCoinDto dto) {
        return homeService.coin(token, dto);
    }


    /**
     * 钱包地址列表
     *
     * @param token token
     * @param page  page
     * @return Result
     */
    @PostMapping("/coinList")
    public Result coinList(@RequestHeader("token") String token, String coin, Page page) {
        return homeService.coinList(token, coin, page);
    }


    /**
     * 内部转账页面信息
     *
     * @param token token
     * @return Result
     */
    @PostMapping("/transferPage")
    public Result transferPage(@RequestHeader("token") String token) {
        return homeService.transferPage(token);
    }

    /**
     * 内部转账
     *
     * @param token token
     * @return Result
     */
    @PostMapping("/transfer")
    public Result transfer(@RequestHeader("token") String token, ApiTransferDto apiTransfer) {
        return homeService.transfer(token, apiTransfer);
    }

    /**
     * 内部转账记录
     *
     * @param token token
     * @param page  page
     * @return Result
     */
    @PostMapping("/transferRecordFlow")
    public Result transferRecordFlow(@RequestHeader("token") String token, Page page) {
        return homeService.transferRecordFlow(token, page);
    }

    /**
     * 资产划转页面信息
     *
     * @param token token
     * @return Result
     */
    @RequestMapping("/convertPage")
    public Result convertPage(@RequestHeader("token") String token, @Valid ApiConvertPageDto apiConvertPageDto) {
        return homeService.convertPage(token, apiConvertPageDto);
    }

    /**
     * 资产划转
     *
     * @param token         token
     * @param apiConvertDto apiConvertDto
     * @return Result
     */
    @RequestMapping("/convert")
    public Result convert(@RequestHeader("token") String token, @Valid ApiConvertDto apiConvertDto) {
        return homeService.convert(token, apiConvertDto);
    }


    /**
     * 流水记录
     *
     * @param token        token
     * @param languageType 1:简体，2表示繁体，3表示韩语，4表示日语，5表示英文
     * @return Result
     */
    @RequestMapping("/cashflow")
    public Result cashflow(@RequestHeader("token") String token, Page page,
                           @RequestParam(required = false, value = "symbols") String symbols,
                           @RequestParam(required = false, value = "flowType") String flowType,
                           @RequestParam(defaultValue = "WALLET") String type,
                           @RequestParam(defaultValue = "1", required = false) String languageType) {
        return homeService.cashflow(token, page, type, symbols, flowType, languageType);
    }


    /**
     *              币币   start
     */

    /**
     * 币币交易 (原接口）
     */
//    @PostMapping("/currency")
//    public Result currency(@RequestHeader("token") String token, @Valid ApiCurrencyDto dto)
//    {
//        return homeService.currency(token, dto);
//    }

    /**
     * 币币交易
     */
    @RequestMapping("/currency")
    public Result currency(@RequestHeader("token") String token, @Valid ApiCurrencyDto dto) throws Exception {
        return asyncService.currencyV2(token, dto).get();

    }


    /**
     * 币币交易页面信息
     *
     * @param token token
     * @param type  type
     * @return Result
     */
    @PostMapping("/currencyPage")
    public Result currencyPage(@RequestHeader("token") String token, @RequestParam(defaultValue = "BTC/USDT") String type, @RequestParam(defaultValue = "BUY") String matchType) {
        return homeService.currencyPage(token, type, matchType);
    }

    /**
     * 撤销币币交易委托单
     */
    @PostMapping("/cancelCurrency")
    public Result cancelCurrency(@RequestHeader("token") String token, Long matchId) {
        return homeService.cancelCurrency(token, matchId);
    }

    /**
     * 币币委托 记录
     *
     * @param type 1-当前委托 2-买入  3-卖出 4-撤销
     */
    @RequestMapping("/currencyRecord")
    public Result currencyRecord(@RequestHeader("token") String token, @RequestParam(defaultValue = "1") int type, Page page, @RequestParam(required = false, value = "symbols") String symbols) {
        return homeService.currencyRecord(token, type, page, symbols);
    }

    /**
     *              币币   end
     */

    /**
     *              合约   start
     */

    /**
     * 合约页面信息
     *
     * @param token   token
     * @param symbols 交易对
     * @param coin    币种 USDT
     * @return Result
     */
    @RequestMapping("/contractPage")
    public Result contractPage(@RequestHeader("token") String token,
                               @RequestParam(defaultValue = "BTC/USDT") String symbols,
                               @RequestParam(defaultValue = "USDT", required = false, value = "coin") String coin,
                               @RequestParam(defaultValue = "CONTRACT") String handlerType) {
        return homeService.contractPage(token, symbols, coin, handlerType);
    }

    /**
     * 杠杆倍率
     *
     * @param token token
     * @return Result
     */
    @PostMapping("/leverage")
    public Result leverage(@RequestHeader("token") String token) {
        return homeService.leverage(token);
    }

    /**
     * 合约交易
     */
    @RequestMapping("/contract")
    public Result contract(@RequestHeader("token") String token, @Valid ApiContractDto dto) throws Exception {
        return homeService.contract(token, dto);
    }


    /**
     * 合约订单列表
     *
     * @param token
     * @param type       页面类型 N-持仓 IN-当前委托 Y-平仓订单
     * @param showMethod 持仓或当前委托需传递显示的数据：ALL显示全部，CURRENT 当前的币种
     * @param symbols    币种
     * @param buyMethod  下单方式：BUY或者SELL
     * @param status     状态类型：手动平仓：HANDLE，FIXED：强制平仓，PROFIT： 止盈平仓，LOSS：止损平仓
     * @param page
     * @return Result
     */
    @RequestMapping("/contractList")
    public Result contractList(@RequestHeader("token") String token,
                               @RequestParam(defaultValue = "N") String type,
                               @RequestParam(defaultValue = "ALL", required = false, value = "showMethod") String showMethod,
                               @RequestParam(required = false, value = "symbols") String symbols,
                               @RequestParam(required = false, value = "buyMethod") String buyMethod,
                               @RequestParam(required = false, value = "status") String status,
                               @RequestParam(required = false, value = "handlerType") String handlerType,
                               Page page) {
        return homeService.contractList(token, type, page, showMethod, symbols, buyMethod, status, handlerType);
    }

    /**
     * 取消合约委托
     *
     * @param token     token
     * @param compactId compactId
     * @return Result
     */
    @RequestMapping("/cancelContract")
    public Result cancelContract(@RequestHeader("token") String token,
                                 @RequestParam(required = false, value = "compactId") Long compactId,
                                 @RequestParam(defaultValue = "1", value = "type") Integer type,
                                 @RequestParam(required = false, value = "handlerType") String handlerType) {
        return homeService.cancelContract(token, compactId, type, handlerType);
    }

    /**
     * 止盈止损设置
     */
    @RequestMapping("/contractPl")
    public Result contractPl(@RequestHeader("token") String token, @Valid ApiPlDto dto) {
        return homeService.contractPl(token, dto);
    }

    /*
     * 合约-平仓
     */
    @RequestMapping("/outContract")
    public Result outContract(@RequestHeader("token") String token,
                              @RequestParam(required = false, value = "compactId") Long compactId,
                              @RequestParam(required = false, value = "number") BigDecimal number,
                              @RequestParam(defaultValue = "1", value = "type") Integer type,
                              @RequestParam(defaultValue = "CONTRACT", value = "handlerType") String handlerType) {
        return homeService.outContract(token, compactId, type, number, handlerType);
    }


    /**
     *    合约   end
     */

    /**
     *              期货   start
     */
    /**
     * 期货页面信息
     *
     * @param token   token
     * @param symbols 交易对
     * @param coin    币种 USDT
     * @return Result
     */
    @PostMapping("/futuresContractPage")
    public Result futuresContractPage(@RequestHeader("token") String token,
                                      @RequestParam(defaultValue = "BTC/USDT") String symbols,
                                      @RequestParam(defaultValue = "USDT", required = false, value = "coin") String coin) {
        return homeService.futuresContractPage(token, symbols, coin);
    }

    /**
     * 杠杆倍率
     *
     * @param token token
     * @return Result
     */
    @PostMapping("/futuresLeverage")
    public Result futuresLeverage(@RequestHeader("token") String token) {
        return homeService.futuresLeverage(token);
    }

    /**
     * 期货交易
     */
    @PostMapping("/futures")
    public Result futures(@RequestHeader("token") String token, @Valid ApiFuturesDto dto) throws Exception {
        return homeService.futures(token, dto);
    }

    /**
     * 期货订单列表
     *
     * @param token
     * @param type       页面类型 N-持仓 IN-当前委托 Y-平仓订单
     * @param showMethod 持仓或当前委托需传递显示的数据：ALL显示全部，CURRENT 当前的币种
     * @param symbols    币种
     * @param buyMethod  下单方式：BUY或者SELL
     * @param status     状态类型：手动平仓：HANDLE，FIXED：强制平仓，PROFIT： 止盈平仓，LOSS：止损平仓
     * @param page
     * @return Result
     */
    @PostMapping("/futuresContractList")
    public Result futuresContractList(@RequestHeader("token") String token,
                                      @RequestParam(defaultValue = "N") String type,
                                      @RequestParam(defaultValue = "ALL", required = false, value = "showMethod") String showMethod,
                                      @RequestParam(required = false, value = "symbols") String symbols,
                                      @RequestParam(required = false, value = "buyMethod") String buyMethod,
                                      @RequestParam(required = false, value = "status") String status,
                                      Page page) {
        return homeService.futuresContractList(token, type, page, showMethod, symbols, buyMethod, status);
    }

    /**
     * 取消期货委托
     *
     * @param token     token
     * @param compactId compactId
     * @return Result
     */
    @PostMapping("/cancelFuturesContract")
    public Result cancelFuturesContract(@RequestHeader("token") String token,
                                        @RequestParam(required = false, value = "compactId") Long compactId,
                                        @RequestParam(defaultValue = "1", value = "type") Integer type) {
        return homeService.cancelFuturesContract(token, compactId, type);
    }

    /**
     * 止盈止损设置
     */
    @PostMapping("/futuresContractPl")
    public Result futuresContractPl(@RequestHeader("token") String token, @Valid ApiPlDto dto) {
        return homeService.futuresContractPl(token, dto);
    }

    /*
     * 期货-平仓
     */
    @PostMapping("/outFuturesContract")
    public Result outFuturesContract(@RequestHeader("token") String token,
                                     @RequestParam(required = false, value = "compactId") Long compactId,
                                     @RequestParam(required = false, value = "number") BigDecimal number,
                                     @RequestParam(defaultValue = "1", value = "type") Integer type) {
        return homeService.outFuturesContract(token, compactId, type, number);
    }

    /**
     * 期货   end
     */
    /*
     * 期权合约接口start
     */

// 获取历史期数
    @RequestMapping("/timeCount")
    public Result timeCount(String symbol) {
        return Result.success(homeService.timeCount(symbol));
    }


    @RequestMapping("/contractOption-history")
    public Result history(String symbol, int count) {
        return homeService.history(symbol, count);
    }

    // 获取正在投注中的合约
    @RequestMapping("contractOption-starting")
    public Result starting(String symbol) {
        return Result.success(homeService.starting(symbol));
    }

    // 获取正在开奖中的合约
    @RequestMapping("contractOption-opening")
    public Result opening(String symbol) {
        return homeService.opening(symbol);
    }


    /**
     * 查询现货交易对列表
     */

    @RequestMapping("/contractOptionCoin-coin-list")
    public Result coinlist() {

        return Result.success(homeService.coinlist());
    }

    // 获取期权合约交易对信息
    @RequestMapping("contractOptionCoin-coin-info")
    public ResponseData coinInfo(String symbol) {

        return ResponseData.success(homeService.coinInfo(symbol));
    }

    @RequestMapping("contractOptionOrder-current")
    public Result current(@RequestHeader("token") String token, String symbol, /* 交易对符号*/
                          Long optionId /* 参与对象*/) {
        return Result.success(homeService.ordercurrent(token, symbol, optionId));
    }

    @RequestMapping("contractOptionOrder-history")
    public Result history(@RequestHeader("token") String token,
                          @RequestParam(value = "symbol", required = false) String symbol,
                          @RequestParam(value = "status", required = false) Integer status,
                          Page page
    ) {
        return homeService.orderhistory(token, symbol, status, page);
    }

    @RequestMapping("contractOptionOrder-add")
    public Result orderAdd(@RequestHeader("token") String token,
                           String symbol, // 交易对符号
                           Integer direction, // 1：看涨  2：看跌
                           Long optionId, // 参与对象
                           BigDecimal amount // 参与金额
    ) {

        return homeService.orderAdd(token, symbol, direction, optionId, amount);
    }

    /**
     * 生成新的合约和新的合约订单
     *
     * @param symbol    币种(TRX/USDT)
     * @param amount    下注数量
     * @param direction 看涨/看跌(0/1)
     * @param seconds   开奖时间
     * @param winRatio     赢了的收益比例
     * @param loseRatio    输的比例
     * @return
     */
    @RequestMapping("createContactAndOrder")
    public Result createContactAndOrder(@RequestHeader("token") String token, String symbol, String openPrice, String amount, Integer direction, Integer seconds, Double winRatio, Double loseRatio) {
        return homeService.createContactAndOrder(token, symbol, openPrice, amount, direction, seconds, winRatio, loseRatio);
    }

    /**
     * 结算合约和合约订单
     *
     * @param token
     * @param contractOptionOrderId
     * @param closePrice
     * @return
     */
    @RequestMapping("optionsContractSettlement")
    public Result optionsContractSettlement(@RequestHeader("token") String token, Long contractOptionOrderId, String closePrice) {
        return homeService.optionsContractSettlement(token, contractOptionOrderId, closePrice);
    }

    /**
     * 获取结算时间-收益比率List
     */
    @RequestMapping("contractOptionOrder-secondsYield")
    public Result secondsYieldList(String symbol) {
        return Result.success(homeService.secondsYieldList(symbol));
    }

    /*  期权合约接口end   */

    /*理财start */
    @RequestMapping("finPeriodicList")
    public Result finPeriodicList() {
        return homeService.finPeriodicList();
    }

    @RequestMapping("finPeriodicMyOrderList")
    public Result finPeriodicMyOrderList(@RequestHeader("token") String token) {
        return homeService.finPeriodicMyOrderList(token);
    }

    @RequestMapping("finPeriodicOrderAdd")
    public Result finPeriodicOrderAdd(@RequestHeader("token") String token, Long periodicId, BigDecimal amount) {
        return homeService.finPeriodicOrderAdd(token, periodicId, amount);
    }

    /**
     * 理财end
     */

    //认购
    @RequestMapping("teManagementOrderAdd") //getMyLogList0Api
    public Result teManagementOrderAdd(@RequestHeader("token") String token, Long teManagementId, BigDecimal amount) {
        return homeService.teManagementLogAdd(token, teManagementId, amount);
    }

    //参与 进行
    @RequestMapping("teManagementOrderList0")
    public Result teManagementOrderList0(@RequestHeader("token") String token, Page page) {
        return homeService.teManagementLogList(token, page);
    }

    //参与 结束
    @RequestMapping("teManagementOrderList1")
    public Result teManagementOrderList1(@RequestHeader("token") String token, Page page) {
        return homeService.teManagementLogList1(token, page);
    }

    //释放记录
    @RequestMapping("teManagementReleaseList")
    public Result teManagementReleaseList(@RequestHeader("token") String token) {
        return homeService.teManagementReleaseList(token);
    }

    /*     黄金交易start   */
    // 获取历史期数
    @RequestMapping("/contractGold-history")
    public Result historyGold(String symbol, int count) {
        return homeService.historyGold(symbol, count);
    }

    // 获取正在投注中的合约
    @RequestMapping("contractGold-starting")
    public Result startingGold(String symbol) {
        return homeService.startingGold(symbol);
    }

    // 获取正在开奖中的合约
    @RequestMapping("contractGold-opening")
    public Result openingGold(String symbol) {
        return homeService.openingGold(symbol);
    }

    /**
     * 查询现货交易对列表
     */

    @RequestMapping("/contractGoldCoin-coin-list")
    public Result coinlistGold() {

        return Result.success(homeService.coinlistGold());
    }

    // 获取期权合约交易对信息
    @RequestMapping("contractGoldCoin-coin-info")
    public ResponseData coinInfoGold(String symbol) {

        return ResponseData.success(homeService.coinInfoGold(symbol));
    }

    @RequestMapping("contractGoldOrder-current")
    public Result currentGold(@RequestHeader("token") String token, String symbol, /* 交易对符号*/
                              Long optionId /* 参与对象*/) {
        return Result.success(homeService.ordercurrentGold(token, symbol, optionId));
    }

    @RequestMapping("contractGoldOrder-history")
    public Result historyGold(@RequestHeader("token") String token,
                              @RequestParam(value = "symbol", required = false) String symbol,
                              Page page
    ) {
        return homeService.orderhistoryGold(token, symbol, page);
    }

    @RequestMapping("contractGoldOrder-add")
    public Result orderAddGold(@RequestHeader("token") String token,
                               String symbol, // 交易对符号
                               Integer direction, // 1：看涨  2：看跌
                               Long optionId, // 参与对象
                               BigDecimal amount // 参与金额
    ) {

        return homeService.orderAddGold(token, symbol, direction, optionId, amount);
    }

    /*     黄金交易end   */

    /*团队邀请 start*/
    @RequestMapping("myearnings")
    public Result myearnings(@RequestHeader("token") String token
    ) {

        return homeService.myearnings(token);
    }

    @RequestMapping("earningsInfo")
    public Result earningsInfo(@RequestHeader("token") String token
    ) {

        return homeService.earningsInfo(token);
    }

    @RequestMapping("earningsPage")
    public Result earningsPage(@RequestHeader("token") String token, Page page
    ) {

        return homeService.earningsPage(page, token);
    }

    @RequestMapping("teamDetail")
    public Result teamDetail(@RequestHeader("token") String token, Page page
    ) {

        return homeService.teamDetail(token, page);
    }

    @RequestMapping("freeTransferForWallet")
    public Result freeTransferForWallet(@RequestHeader("token") String token, BigDecimal price
    ) {

        return homeService.freeTransferForWallet(token, price);
    }

    @RequestMapping("freeTransferForWalletLog")
    public Result freeTransferForWalletLog(@RequestHeader("token") String token, Page page
    ) {

        return homeService.freeTransferForWalletLog(token, page);
    }
    //团队邀请 end

    @RequestMapping("addCoinApply")
    public Result addCoinApply(@RequestHeader("token") String token, CoinApply coinApply, String code) {

        return homeService.addCoinApply(token, coinApply, code);
    }


    @RequestMapping("addMiningOrder")
    public Result addMiningOrder(@RequestHeader("token") String token, Long id) {

        return homeService.addMiningOrder(token, id);
    }

    @RequestMapping("miningOrder")
    public Result miningOrder(@RequestHeader("token") String token, Page page) {
        return homeService.miningOrder(token, page);
    }

    @RequestMapping("miningOrderDetail")
    public Result miningOrderDetail(@RequestHeader("token") String token, Page page) {
        return homeService.miningOrderDetail(token, page);
    }

    @RequestMapping("startUpMiningOrder")
    public Result startUpMiningOrder(@RequestHeader("token") String token, Long id) {
        return homeService.startUpMiningOrder(token, id);
    }

    @RequestMapping("pickMiningOrder")
    public Result pickMiningOrder(@RequestHeader("token") String token, Long id, String type) {
        return homeService.pickMiningOrder(token, id, type);
    }

    @RequestMapping("chenkDayConfig")
    public Result chenkDayConfig(@RequestHeader("token") String token) {
        return homeService.chenkDayConfig(token);
    }

    @RequestMapping("getWithdrawMinNum")
    public Result getWithdrawMinNum() {
        return homeService.getWithdrawMinNum();
    }

    @RequestMapping("getChatList")
    public Result getChatList(@RequestHeader("token") String token) {
        return homeService.getChatList(token);
    }

    @RequestMapping("checkChatPwd")
    public Result checkChatPwd(@RequestHeader("token") String token, String pwd) {
        return homeService.checkChatPwd(token, pwd);
    }

    @RequestMapping("coinLoan")
    public Result coinLoan(@RequestHeader("token") String token, Loan loan, String code) {
        return homeService.coinLoan(token, loan, code);
    }

    @RequestMapping("coinLoanList")
    public Result coinLoanList(@RequestHeader("token") String token) {
        return homeService.coinLoanList(token);
    }


}
