package cn.stylefeng.guns.modular.app.controller;

import cn.stylefeng.guns.core.bipay.entity.Trade;
import cn.stylefeng.guns.core.bipay.http.client.BiPayClient;
import cn.stylefeng.guns.modular.app.controller.market.dto.KlineDto;
import cn.stylefeng.guns.modular.app.service.*;
import cn.stylefeng.guns.modular.app.vo.VersionVo;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.bulletin.entity.Article;
import cn.stylefeng.guns.modular.bulletin.entity.News;
import cn.stylefeng.guns.modular.bulletin.service.ArticleService;
import cn.stylefeng.guns.modular.bulletin.service.NewsService;
import cn.stylefeng.guns.modular.chain.service.RechargeService;
import cn.stylefeng.guns.modular.com.entity.Business;
import cn.stylefeng.guns.modular.com.service.BusinessService;
import cn.stylefeng.guns.modular.fin.service.ContractService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 公共API
 * 无需token
 */
@CrossOrigin
@RequestMapping("/api/common")
@RestController
public class CommonApi {

    @Resource
    private HomeService homeService;

    @Resource
    private AppCarouselService appCarouselService;

    @Resource
    private NewsService newsService;

    @Resource
    private ArticleService articleService;

    @Resource
    private BusinessService businessService;

    @Resource
    private CommonService commonService;

    @Resource
    private BiPayClient biPayClient;

    @Resource
    private MemberRechargeAddressService memberRechargeAddressService;

    @Resource
    private RechargeService rechargeService;

    @Resource
    private ContractService contractService;

    @Resource
    RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(CommonApi.class);

    /***
     * 用户协议
     */
    @PostMapping("/declares")
    public Result declares(@RequestParam(defaultValue = "zh") String language) {
        return homeService.declares(language);
    }

    /**
     * 系统时间戳
     *
     * @return
     */
    @PostMapping("/sysTime")
    public Result sysTime() {
        return Result.success(System.currentTimeMillis());
    }

    /**
     * 版本信息
     */
    @PostMapping("/version")
    public Result version(VersionVo versionVo) {
        return homeService.version(versionVo);
    }

    @PostMapping("/areaList_")
    public Result areaList_() {
        return homeService.areaList_();
    }

    /**
     * 获取轮播图列表
     *
     * @param type 轮播图类型
     * @return Result
     */
    @PostMapping("/carousel")
    public Result list(@RequestParam(required = false, defaultValue = "HOME_TOP") String type,
                       @RequestParam(required = false, defaultValue = "PHONE") String showType) {
        return appCarouselService.getList(type, showType);
    }

    /**
     * 系统公告
     */
    @PostMapping("/newsList")
    public Result newsList(@RequestParam(defaultValue = "HOME") String status, Page page, @RequestParam(defaultValue = "zh") String language) {
        News news = new News();
        news.setLanguage(language);
        news.setDel("N").setStatus(status.trim().equals("Y") ? status.trim() : null);
        return Result.success(newsService.page(page, new QueryWrapper<>(news).orderByDesc(Constant.CREATE_TIME)));
    }

    @PostMapping("/newsDetail")
    public Result newsDetail(@RequestParam(defaultValue = "id") Long id) {

        return Result.success(newsService.getById(id));
    }

    /**
     * 行业资讯
     */
    @PostMapping("/businessList")
    public Result businessList(Page page) {
        Business article = new Business();
        article.setDel("N");
        return Result.success(businessService.page(page, new QueryWrapper<>(article).orderByDesc(Constant.CREATE_TIME)));
    }

    /**
     * 联系客服
     */
    @PostMapping("/contact")
    public Result contact() {
        return commonService.contact();
    }

    /**
     * 业务文章
     */
    @PostMapping("/article")
    public Result article(@RequestParam(defaultValue = "ABOUT_US") String type, @RequestParam(defaultValue = "zh") String language) {
        LambdaQueryWrapper<Article> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(Article::getArticleType, type);
        objectLambdaQueryWrapper.eq(Article::getDel, "N");
        objectLambdaQueryWrapper.eq(Article::getLanguage, language);
        Article one = articleService.getOne(objectLambdaQueryWrapper);
        System.out.println(one);
        return Result.success(one);
    }

    /**
     * 多媒体信息列表
     */
    @PostMapping("/media")
    public Result media(Page page, @RequestParam(defaultValue = "VIDEO") String type) {
        return homeService.media(page, type);
    }

    //
//    /**
//     *电话区号表
//     */
    @PostMapping("/phoneCode")
    public Result phoneCode() {
        return homeService.phoneCode();
    }

    //查询邀请码
    @PostMapping("/checkInviteCode")
    public Result checkInviteCode(String code) {
        return homeService.checkInviteCode(code);
    }

    /**
     * 获取验证码
     *
     * @param code  区号
     * @param phone 电话
     * @param type  类型
     * @return
     */
    @PostMapping("/getMsg")
    public Result msg(@RequestHeader(value = "token", required = false, defaultValue = "-1") String token,
                      @RequestParam(defaultValue = "001") String code,
                      @RequestParam(defaultValue = "1") String phone,
                      @RequestParam(defaultValue = "1") String email,
                      @RequestParam(value = "type", required = false, defaultValue = "1") Long type) {
        return homeService.getMsg(code, phone, type, email);
    }

    /**
     * 新手指南和帮助中心
     *
     * @param type
     * @param page
     * @return
     */
    @PostMapping("/problem")
    public Result problem(@RequestParam(defaultValue = "NEW") String type, Page page, @RequestParam(defaultValue = "zh") String language) {
        return homeService.problem(type, page, language);
    }

    /**
     * 白书皮列表数据
     *
     * @param
     * @return
     */
    @PostMapping("/whiteBook")
    public Result whiteBook(@RequestParam(defaultValue = "zh") String language) {
        return homeService.whiteBook(language);
    }

    @PostMapping("/whiteBookDetail")
    public Result whiteBookDetail(@RequestParam(value = "id", required = false, defaultValue = "1") Long id) {
        return homeService.whiteBookDetail(id);
    }

    /**
     * 提供给非小号的现货行情数据
     */
    @GetMapping("/markTicket")
    public Object markTicket() {
        return commonService.markTicket();
    }

    @GetMapping("/swapTicket")
    public Object swapTicket() {
        return commonService.swapTicket();
    }

    /**
     * 火币行情
     *
     * @param type 现货-0  期货（永续-1  当周-2 次周-3 季度-4）
     * @return
     */
    @PostMapping("/huobiTicket")
    public Result huobiTicket(@RequestParam(defaultValue = "0") int type) {
        return commonService.huobiTicket(type);
    }

    /**
     * 币种k线数据
     *
     * @return
     */
    @PostMapping("/kline")
    public Result kline(@Valid KlineDto dto) {
        return commonService.kline(dto);
    }

    /**
     * 币种交易信息
     *
     * @param symbol 交易对
     * @param type   现货-0  期货（永续-1  当周-2 次周-3 季度-4）
     * @return
     */
    @PostMapping(value = "/tradeList")
    public Result tradeList(String symbol, @RequestParam(defaultValue = "0") int type) {
        return commonService.tradeList(symbol, type);
    }

    /**
     * 币种交易信息
     *
     * @param symbol 交易对
     * @param type   现货-0  期货（永续-1  当周-2 次周-3 季度-4）
     * @return
     */
    @PostMapping(value = "/realList")
    public Result realList(String symbol, @RequestParam(defaultValue = "0") int type) {
        return commonService.realList(symbol, type);
    }

    /**
     * 合约最新成交
     *
     * @param symbol
     * @return
     */
    @PostMapping(value = "/currentTrade")
    public Result currentTrade(String symbol) {
        return commonService.currentTrade(symbol);
    }

    /**
     * 合约简介
     *
     * @param symbol
     * @return
     */
    @PostMapping(value = "/currentInfo")
    public Result currentInfo(String symbol) {
        return commonService.currentInfo(symbol);
    }

    /**
     * 期货永续黄金简介
     *
     * @param symbol
     * @return
     */
    @PostMapping(value = "/futuresInfo")
    public Result futuresInfo(String symbol) {
        return commonService.futuresInfo(symbol);
    }

    /**
     * 期货永续黄金最新成交
     *
     * @param symbol
     * @return
     */
    @PostMapping(value = "/futuresTrade")
    public Result futuresTrade(String symbol) {
        return commonService.currentTrade(symbol);
    }

    /**
     * 最近24小时行情数据
     */
    @Deprecated
    public Result tfTicket() {
        return commonService.tfTicket();
    }

    /**
     * 平台所有币种
     *
     * @return Result
     */
    @PostMapping(value = "/symbolsList")
    public Result symbolsList(@RequestParam(defaultValue = "FLOW", value = "type", required = false) String type) {
        return commonService.symbolsList(type);
    }

    /**
     * 充币币种列表
     *
     * @return
     */
    @PostMapping(value = "/changerSymbolsList")
    public Result changerSymbolsList() {
        return commonService.changerSymbolsList();
    }

    /**
     * 流水类型
     *
     * @param type         ： WALLET-钱包 | CONTRACT-合约 | CURRENCY-币币 | LEGAL-法币
     * @param languageType 1:简体，2表示繁体，3表示韩语，4表示日语，5表示英文
     * @return Result
     */
    @PostMapping(value = "/flowTypeList")
    public Result flowTypeList(@RequestParam(defaultValue = "WALLET", value = "type") String type
            , @RequestParam(defaultValue = "1", value = "languageType", required = false) String languageType) {
        return commonService.flowTypeList(type, languageType);
    }

    /**
     * 处理币付网关回调信息，包括充币
     *
     * @param timestamp
     * @param nonce
     * @param body
     * @param sign
     * @return
     * @throws Exception
     */
    @RequestMapping("/bipay/notify")
    public String tradeCallback(@RequestParam("timestamp") String timestamp,
                                @RequestParam("nonce") String nonce,
                                @RequestParam("body") String body,
                                @RequestParam("sign") String sign) throws Exception {
        logger.info("timestamp:{},nonce:{},sign:{},body:{}", timestamp, nonce, sign, body);
//		if (!HttpUtil.checkSign(biPayClient.getMerchantKey(), timestamp, nonce, body, sign)) {
//			logger.error("签名校验失败");
//			return "error";
//		}
        Trade trade = JSONObject.parseObject(body, Trade.class);
        logger.info("trade:{}", trade);
        //TODO 业务处理
        if (trade.getTradeType() == 1) {
            logger.info("=====收到充币通知======");
            logger.info("address:{},amount:{},mainCoinType:{},fee:{}", trade.getAddress(), trade.getAmount(), trade.getMainCoinType(), trade.getFee());
            homeService.updateWalletMoney(trade);
        } else if (trade.getTradeType() == 2) {
            logger.info("=====收到提币处理通知=====");
            logger.info("address:{},amount:{},mainCoinType:{},businessId:{}", trade.getAddress(), trade.getAmount(), trade.getMainCoinType(), trade.getBusinessId());
            homeService.updateWithdraw(trade);
        }
        return "success";
    }

    @GetMapping(value = "/updateMemberAddress")
    public Result updateMemberAddress() {
        return homeService.updateMemberAddress();
    }

    /**
     * 资讯类别数据flowTypeList
     *
     * @return Result
     */
    @PostMapping(value = "/getCategoryList")
    public Result getCategoryList() {
        return commonService.getCategoryList();
    }

    /**
     * 资讯列表数据
     *
     * @param categoryId 类别id
     * @param page       分页
     * @return Result
     */
    @PostMapping("/informationList")
    public Result informationList(@RequestParam(value = "categoryId") Long categoryId, Page page) {
        return commonService.informationList(categoryId, page);
    }

    /**
     * 资讯详情页面数据
     *
     * @param id
     * @return
     */
    @PostMapping("/informationDetail")
    public Result informationDetail(@RequestParam(value = "id") Long id) {
        return commonService.informationDetail(id);
    }

    @RequestMapping("/finPeriodicList")
    public Result finPeriodicList() {
        return commonService.finPeriodicList();
    }

    //列表
    @RequestMapping("/teManagementList")
    public Result teManagementList(Page page) {
        return commonService.teManagementList(page);
    }

    @RequestMapping("teManagementListAll")
    public Result teManagementListAll() {
        return commonService.teManagementListAll();
    }

    //获取已购份数
    @RequestMapping("/knowTeManagementList")
    public Result knowTeManagementList(Long id) {
        return commonService.knowTeManagementList(id);
    }

    @RequestMapping("/miningList")
    public Result miningList(Page page) {
        return commonService.miningList(page);
    }

    @RequestMapping("getChatConfigs")
    public Result getChatConfigs() {
        return homeService.getChatConfigs();
    }

    @RequestMapping("checkSmsSend")
    public Result checkSmsSend() {
        return Result.success(F.me().cfg(Constant.SMSSENDOPEN));
    }

    @RequestMapping("checkEmailSend")
    public Result checkEmailSend() {
        return Result.success(F.me().cfg(Constant.EMAILSENDOPEN));
    }

    @RequestMapping("teamRewardsList")
    public Result teamRewardsList() {
        return Result.success(commonService.teamRewardsList());
    }

    @RequestMapping("getContractConfig")
    public Result getContractConfig() {
        return homeService.getContractConfig();
    }

    @RequestMapping("getInviteConfig")
    public Result getInviteConfig() {
        return homeService.getInviteConfig();
    }

    @RequestMapping("platformRate")
    public Result platformRate(@RequestParam(value = "code", required = false) String code) {
        return commonService.platformRate(code);
    }

    @RequestMapping("community")
    public Result community(@RequestParam(value = "language", defaultValue = "zh") String language) {
        return commonService.community(language);
    }
}
