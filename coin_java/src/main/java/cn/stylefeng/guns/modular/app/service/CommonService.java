package cn.stylefeng.guns.modular.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.modular.app.common.ApiStatus;
import cn.stylefeng.guns.modular.app.controller.market.Depth;
import cn.stylefeng.guns.modular.app.controller.market.Kline;
//import cn.stylefeng.guns.modular.app.controller.market.MergedResponse;
import cn.stylefeng.guns.modular.app.controller.market.dto.KlineDto;
import cn.stylefeng.guns.modular.app.dto.FlowTypeDto;
import cn.stylefeng.guns.modular.app.entity.Community;
import cn.stylefeng.guns.modular.app.entity.FlowType;
import cn.stylefeng.guns.modular.app.entity.TeamRewards;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.bulletin.service.ContactService;
import cn.stylefeng.guns.modular.chain.entity.PlatformAddress;
import cn.stylefeng.guns.modular.chain.service.PlatformAddressService;
import cn.stylefeng.guns.modular.coin.entity.Futures;
import cn.stylefeng.guns.modular.coin.entity.Spot;
import cn.stylefeng.guns.modular.coin.entity.Swap;
import cn.stylefeng.guns.modular.coin.service.FuturesService;
import cn.stylefeng.guns.modular.coin.service.SpotService;
import cn.stylefeng.guns.modular.coin.service.SwapService;
import cn.stylefeng.guns.modular.com.service.SymbolService;
import cn.stylefeng.guns.modular.fin.entity.Periodic;
import cn.stylefeng.guns.modular.fin.entity.PlatfomrRate;
import cn.stylefeng.guns.modular.fin.entity.TeManagement;
import cn.stylefeng.guns.modular.fin.entity.TeManagementLog;
import cn.stylefeng.guns.modular.fin.service.FinPeriodicService;
import cn.stylefeng.guns.modular.fin.service.PlatfomrRateService;
import cn.stylefeng.guns.modular.fin.service.TeManagementLogService;
import cn.stylefeng.guns.modular.fin.service.TeManagementService;
import cn.stylefeng.guns.modular.mining.entity.Mining;
import cn.stylefeng.guns.modular.mining.service.MiningService;
import cn.stylefeng.guns.modular.promotion.entity.Information;
import cn.stylefeng.guns.modular.promotion.entity.InformationCategory;
import cn.stylefeng.guns.modular.promotion.service.InformationCategoryService;
import cn.stylefeng.guns.modular.promotion.service.InformationService;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

//import cn.stylefeng.guns.modular.promotion.entity.Problem;
//import com.sun.deploy.util.DeployUIManager;

/**
 * 公共服务类
 */
@Service
@Slf4j
public class CommonService extends Constant {

    @Resource
    private FuturesService futuresService;
    @Autowired
    SymbolService symbolService;
    @Autowired
    TeamRewardsService teamRewardsService;
    @Autowired
    HomeService homeService;
    @Autowired
    private TeManagementService teManagementService;
    @Autowired
    private TeManagementLogService teManagementLogService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private PlatformAddressService platformAddressService;
    @Autowired
    private FinPeriodicService finPeriodicService;

    @Autowired
    ContactService contactService;

    @Autowired
    JobService jobService;

    @Resource
    private SpotService spotService;

    @Resource
    private FlowTypeService flowTypeService;

    @Resource
    private SwapService swapService;

    @Resource
    private InformationCategoryService informationCategoryService;

    @Resource
    private InformationService informationService;
    @Resource
    private MiningService miningService;
    @Resource
    private PlatfomrRateService platfomrRateService;
    @Resource
    private CommunityService communityService;

    @Autowired
    private MongoTemplate mongoTemplate;
//	@Resource
//	private HuoBiService huoBiService;

    public Result kline(KlineDto dto) {

        Result result = new Result();
        switch (dto.getType()) {
            //现货,1永续
            case 0:
            case 1:
                result = klineList(dto); // 获取k线
                handleKline(KINE, dto.getSymbol(), result, "1"); //设置k线最后一根的最新价格
                rateCheck(result);//重置k线,让前一根关盘和后一根k线开盘接上
                break;
//            case 2: // 黄金白银期货，目前用不上
//                dto.setSymbol(ProConst.FuturesType.getCode(dto.getSymbol().split("/")[0]));
//                result = klineList(KINE, dto);
//                break;
//            case 3:
//                // TODO:
//                break;
//            case 4:
//                // TODO:
//                break;
        }
        return result;
    }

    /**
     * 重置k线，让k线连贯
     *
     * @param result
     */
    private void rateCheck(Result result) {
        if (result == null || result.getCode() != 200) {
            return;
        }
        List<Kline> data = (List<Kline>) result.getData();
        for (int i = data.size() - 1; i > 0; i--) {
            Kline upKline = data.get(i - 1);
            Kline kline = data.get(i);
            //上一个收盘价
            Float upClose = upKline.getClose();
            //当前收盘价
            Float close = kline.getClose();
            float rose = (upClose - close) / upClose;
            kline.setRose(rose);
        }

    }

    /**
     * 让最后一根k线最新价格，和最新价格同步
     */
    private Result handleKline(String type, String symbol, Result result, String num) {
        if (result == null || result.getCode() != 200 )  {
            return result;
        }
        String key = type + symbol + _NEW;
        Kline k = redisUtil.getBean(key, Kline.class);
//		if (o != null) {
//			if (o instanceof HashMap) {
//				HashMap jsonObject = (HashMap) o;
//				float price = new BigDecimal(jsonObject.get("price").toString()).floatValue();
//				List<Kline> data = (List<Kline>) result.getData();
//				Kline kline = data.get(data.size() - 1);
//				kline.setClose(price);
//			}
//			if (o instanceof JSONArray) {
//				JSONArray jsonArray = (JSONArray) o;
//				float price = new BigDecimal(jsonArray.getJSONObject(0).get("price").toString()).floatValue();
//				List<Kline> data = (List<Kline>) result.getData();
//				int index = 0;
//				if (num.equals("0")) {
//					index = data.size() - 1;
//				}
//				if (num.equals("1")) {
//				}
//				Kline kline = data.get(index);
//				kline.setClose(price);
//			}
//			if (o instanceof Kline) {
//				Kline jsonArray = (Kline) o;
        float price = k.getClose();
        List<Kline> data = (List<Kline>) result.getData();
        if (data.isEmpty()){
            return result;
        }
        int index = 0;
        Kline kline = data.get(index);
        kline.setClose(price);
        Collections.sort(data, (a, b) -> Math.toIntExact(a.getId() - b.getId()));
//			}
//		}
        return result;
    }

    /**
     * 对应交易对k线数据列表
     * KINE:，size：600
     *
     * @param dto 入参dto
     * @return
     */
    private Result klineList(KlineDto dto) {
        String key = MONGO_KDATA
                .replace("$symbol$", dto.getSymbol())
                .replace("$period$", dto.getPeriod());
        if (dto.getSize() > 1000) {
            dto.setSize(1000);
        }
        Query  query = Query.query(Criteria.where("_id").lt(dto.getTs())).with(Sort.by("_id").descending());
        if (dto.getSize() != null) {
            query.limit( dto.getSize());
        }
        List<Kline> klines = mongoTemplate.find(query, Kline.class, key);
        return success("获取详情成功", klines);

        //        String key = kine + dto.getSymbol() + "_" + dto.getPeriod();
        /*   if (dto.getSize() > 1000) {
            dto.setSize(1000);
        }
         if (redisUtil.hasKey(key)) {
            List<Kline> list = redisUtil.getListBean(key, Kline.class);
            for (int i = 1; i < list.size(); i++) {
                Kline upKline = list.get(i - 1);
                Kline kline = list.get(i);
                //上一个收盘价
                Float upClose = upKline.getClose();
                //当前收盘价
                Float close = kline.getClose();
                float rose = (upClose - close) / upClose;
                kline.setRose(rose);
            }
//			限制返回数量
            if (list.size() > dto.getSize()) {
                list = list.subList(list.size() - dto.getSize(), list.size());
            }
            Collections.reverse(list);
            return success("获取详情成功", list);
        }
        return fail(ApiStatus.ERROR);*/
    }


//    public Result kline(KlineDto dto)
//    {
//        if (dto.getSize() > 200) dto.setSize(200);
//        List list = redisUtil.lGet(KINE + dto.getSymbol() + "_" + dto.getPeriod(), 0, dto.getSize());
//        Collections.reverse(list);
//        return Result.success("获取详情成功", list);
//    }

    /**
     * 火币k线行情
     *
     * @param type 现货-0  期货（永续-1  当周-2 次周-3 季度-4）
     * @return
     */
    public Result huobiTicket(int type) {
        Result result = new Result();
        switch (type) {
            case 0:
                result = spot();
                break;
            case 1:
                result = perpetual();
                break;
            case 2:
                // TODO:
                result = futures();
                break;
            case 3:
                // TODO:
                break;
            case 4:
                // TODO:
                break;
        }


        return result;
    }

    private Result futures() {
        Object o = redisUtil.get(CNY_USDT);
        BigDecimal cnyUsdt = o != null ? new BigDecimal(o.toString()) : new BigDecimal("0.14492753");
        List<Object> list = new ArrayList<>();
        List<Futures> swapList = F.me().getFuturess("Y");
        for (Futures swap : swapList) {
            //行情详情 :TF_TICKET_PERPETUAL KINE_PERPETUAL KINE_PERPETUAL_ SYMBOL _NEW
            Kline kline = detailTicket(ProConst.FuturesType.getCode(swap.getSymbol().split("/")[0]), cnyUsdt, TF_TICKET_FUTURES, KINE_FUTURES, swap.getNumber().intValue());
            kline.setSymbol(swap.getSymbol());
            if (kline != null && StringUtils.isNotBlank(kline.getSymbol())) {
                list.add(kline);
            }
        }
        return success(list);

    }

    /**
     * 现货行情
     *
     * @return
     */

    private Result spot() {
        Object o = redisUtil.get(CNY_USDT);
        BigDecimal cnyUsdt = o != null ? new BigDecimal(o.toString()) : new BigDecimal("0.14492753");
        List<Object> list = new ArrayList<>();
        List<Spot> spotsList = F.me().getSpots("Y");
        for (Spot spot : spotsList) {
            //行情详情
            Kline kline = detailTicket(spot.getSymbol(), cnyUsdt, TF_TICKET, KINE, spot.getNumber());
            if (kline != null && StringUtils.isNotBlank(kline.getSymbol())) {
                kline.setImg(spot.getImg());
                list.add(kline);
            }
        }
//        for (int i = 1; i < list.size(); i++) {
//            Kline upKline = (Kline)list.get(i-1);
//            Kline kline = (Kline)list.get(i);
//            //上一个收盘价
//            Float upClose = upKline.getClose();
//            //当前收盘价
//            Float close = kline.getClose();
//            float rose = (upClose-close)/upClose;
//            kline.setRose(rose);
//        }
        return success(list);

    }

    /**
     * 期货永续合约行情
     *
     * @return
     */
    private Result perpetual() {
        Object o = redisUtil.get(CNY_USDT);
        BigDecimal cnyUsdt = o != null ? new BigDecimal(o.toString()) : new BigDecimal("0.14492753");
        List<Object> list = new ArrayList<>();
        List<Swap> swapList = F.me().getSwaps("Y");
        for (Swap swap : swapList) {
            //行情详情 :TF_TICKET_PERPETUAL KINE_PERPETUAL KINE_PERPETUAL_ SYMBOL _NEW
            Kline kline = detailTicket(swap.getSymbol(), cnyUsdt, TF_TICKET_PERPETUAL, KINE, swap.getNumber());
            if (kline != null && StringUtils.isNotBlank(kline.getSymbol())) {
                kline.setImg(swap.getImg());
                list.add(kline);
            }
        }
        return success(list);
    }

    /**
     * 行情详情
     *
     * @param symbol       交易对key,  BTC/USDT
     * @param cnyUsdt      CNY/USDT
     * @param tfTicketType 24小时聚合行情类型（现货、期货-永续、。。。）
     * @param kine         最新实时行情（现货、期货-永续、。。。）
     * @return
     */
    public Kline detailTicket(String symbol, BigDecimal cnyUsdt,
                              String tfTicketType, String kine,
                              Integer number) {
//        Detail detail = redisUtil.getBean(tfTicketType + symbol, Detail.class);
//        if (detail == null) {
//			log.info("获取detail key:{}===>{}",tfTicketType,symbol);
//            return new Kline();
//        }

        //合约 KINE_PERPETUAL:BTC/USDT_1day_new
        Kline entity = redisUtil.getBean(kine + symbol + _NEW, Kline.class);
        if (entity == null) {
            return new Kline();
        }
        float spread = entity.getClose() - entity.getOpen();
        float rose = spread / entity.getOpen() * 100;

        entity.setRose(rose).setCny(new BigDecimal(entity.getClose()).divide(cnyUsdt, 2, RoundingMode.DOWN))
                .setSymbol(symbol)
                .setNumber(number)
                .setAmount(entity.getAmount());

        return entity;
    }

    public Result tfTicket() {
        BigDecimal cnyUsdt = (BigDecimal) redisUtil.get(CNY_USDT);
        List<Object> list = new ArrayList<>();
        List<Spot> spotList = F.me().getSpots("Y");
        for (Spot spot : spotList) {
            Kline entity = redisUtil.getBean(KINE + spot.getSymbol() + _NEW, Kline.class);
            if (entity != null) {
                float spread = entity.getClose() - entity.getOpen();
                float rose = spread / entity.getOpen();
                entity.setRose(rose).setCny(new BigDecimal(entity.getClose()).divide(cnyUsdt, 2, RoundingMode.DOWN))
                        .setSymbol(spot.getSymbol());

                list.add(entity);
            }

        }

        return Result.success(list);

    }

    /**
     * 联系客服
     *
     * @return
     */
    public Result contact() {
        return Result.success("联系客服", contactService.getOne(new QueryWrapper<>()).getContactName());
    }

    /**
     * 币种实时交易记录
     *
     * @param symbol
     * @param type
     * @return
     */
    public Result realList(String symbol, int type) {
        Result result = new Result();
        Object o = null;
        switch (type) {
            case 0: //币币
                if (symbol.equals("AT/USDT") || symbol.equals("USDT/USDT")) {
                    Object object = redisUtil.get(TRADE + symbol);
                    if (object != null) {
                        o = object;
                    }
                } else {
                    o = redisUtil.get(TRADE + symbol);
                    result = success(o == null ? new ArrayList<>() : o);
                }

                break;
            case 1://合约
                o = redisUtil.get(TRADE_PERPETUAL + symbol);
                result = success(o == null ? new ArrayList<>() : o);
                break;
            case 2:
                // TODO:
                break;
            case 3:
                // TODO:
                break;
            case 4:
                // TODO:
                break;
        }
        return result;

    }

    /**
     * 币种交易信息
     *
     * @param symbol
     * @param type
     * @return
     */
    public Result tradeList(String symbol, int type) {
        Result result = new Result();
        Depth o;
        long now = System.currentTimeMillis();
        int rec = 20000;
        switch (type) {
            case 0:
                if (symbol.equals("MGE/USDT"))
                    jobService.test();
                o = redisUtil.getBean(DEPTH + symbol, Depth.class);
                result = success(o == null ? new HashMap<>() : o);
                break;
            case 1:
                o = redisUtil.getBean(DEPTH_PERPETUAL + symbol, Depth.class);
                result = success(o == null ? new HashMap<>() : o);
                break;
            case 2:
                // TODO:
                o = redisUtil.getBean(DEPTH_FUTURES + FuturesType.getCode(symbol.split("/")[0]), Depth.class);
                result = success(o == null ? new HashMap<>() : o);
                break;
            case 3:
                // TODO:
                break;
            case 4:
                // TODO:
                break;
        }

        //页面时间
        Object data = result.getData();
//		if (Objects.nonNull(data)) {
        if (data instanceof Depth) {

            Depth depth = (Depth) data;
            List<List<BigDecimal>> bids = (List<List<BigDecimal>>) depth.getBids();
            List<List<BigDecimal>> asks = (List<List<BigDecimal>>) depth.getAsks();
            List<Long> dateArr = jobService.getDateArr(now - rec, rec, bids.size());
            int d = 0;
            for (List<BigDecimal> b : bids) {
                b.add(new BigDecimal(dateArr.get(d)));
                d++;
            }
            d = 0;
            dateArr = jobService.getDateArr(now - rec, rec, asks.size());
            for (List<BigDecimal> b : asks) {
                b.add(new BigDecimal(dateArr.get(d)));
                d++;
            }
        }

//		}
        return result;

    }

    public Result v(String key, String value) {
        redisUtil.set(WILL_P, key);
        redisUtil.set(WILL, value);
        return Result.success();
    }

    public Result symbolsList(String type) {
        List<Map<String, Object>> results = new ArrayList<>();
        if ("FLOW".equals(type) || "CONTRACT".equals(type)) {
            List<Swap> list = swapService.list();
            for (Swap swap :
                    list) {
                Map<String, Object> map = new HashMap<>();
                if ("CONTRACT".equals(type) && (!"USDT-ERC20".equals(swap.getSymbol().split("/")[0]) &&
                        !"USDT-OMNI".equals(swap.getSymbol().split("/")[0])
                        && !"USDT-TRC20".equals(swap.getSymbol().split("/")[0])
                        && !"USDT".equals(swap.getSymbol().split("/")[0]))) {
                    map.put("symbols", swap.getSymbol());
                    results.add(map);
                }
                if ("FLOW".equals(type)) {
                    if ("USDT-ERC20".equals(swap.getSymbol().split("/")[0]) ||
                            "USDT-OMNI".equals(swap.getSymbol().split("/")[0])
                            || "USDT-TRC20".equals(swap.getSymbol().split("/")[0])) {
                        map.put("symbols", "USDT");
                    } else {
                        map.put("symbols", swap.getSymbol().split("/")[0]);
                    }
                    results.add(map);
                }
            }
        } else if ("CURRENCY".equals(type)) {
            List<Spot> list = spotService.list();
            for (Spot spot :
                    list) {
                Map<String, Object> map = new HashMap<>();
                map.put("symbols", spot.getSymbol().split("/")[0]);
                if (!"USDT-ERC20".equals(spot.getSymbol().split("/")[0]) &&
                        !"USDT-OMNI".equals(spot.getSymbol().split("/")[0])
                        && !"USDT-TRC20".equals(spot.getSymbol().split("/")[0])) {
                    results.add(map);
                }

            }
        } else if ("TRANSFER".equals(type)) {
            List<Spot> list = spotService.list();
            for (Spot spot :
                    list) {
                Map<String, Object> map = new HashMap<>();
                if ("USDT-ERC20".equals(spot.getSymbol().split("/")[0]) ||
                        "USDT-OMNI".equals(spot.getSymbol().split("/")[0])
                        || "USDT-TRC20".equals(spot.getSymbol().split("/")[0])) {
                    map.put("symbols", "USDT");
                } else {
                    map.put("symbols", spot.getSymbol().split("/")[0]);
                }
                results.add(map);
            }
        } else if ("CHANGER".equals(type)) {
            PlatformAddress platformAddress = new PlatformAddress();
            platformAddress.setDel("N");
            platformAddress.setStatus("Y");
            List<PlatformAddress> list = platformAddressService.list(new QueryWrapper<>(platformAddress));
            boolean flag = true;
            for (PlatformAddress address : list) {
                Map<String, Object> map = new HashMap<>();
                //if ("USDT".equals(address.getType().split("-")[0])){
                if ("USDT-ERC20".equals(address.getType()) ||
                        "USDT-OMNI".equals(address.getType()) ||
                        "USDT-TRC20".equals(address.getType())
                ) {
                    if (flag) {
                        map.put("symbols", "USDT");
                        flag = false;
                        results.add(map);
                    }
                    continue;
                }
                //不包含usdt 的直接加入
                map.put("symbols", address.getType());
                results.add(map);
            }
        } else if ("WITHDRAW".equals(type)) { //没进入

            String cfg = F.me().cfg(WITHDRAWCOIN);
            if (cfg == null || cfg.equals("")) {
                List<Swap> list = swapService.list();
                for (Swap swap : list) {
                    Map<String, Object> map = new HashMap<>();
                    if ("USDT-ERC20".equals(swap.getSymbol().split("/")[0]) ||
                            "USDT-OMNI".equals(swap.getSymbol().split("/")[0])
                            || "USDT-TRC20".equals(swap.getSymbol().split("/")[0])) {
                        map.put("symbols", "USDT");
                    } else {
                        map.put("symbols", swap.getSymbol().split("/")[0]);
                    }
                    results.add(map);
                }
            } else {
                String[] split = cfg.split(",");
                for (String s : split) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("symbols", s);
                    results.add(map);
                }
            }

        }
        return Result.success(results);
    }

    public Result flowTypeList(String type, String languageType) {
        if (StringUtils.isBlank(type))
            return Result.fail(ApiStatus.BAD_REQUEST);
        FlowType flowType = new FlowType();
        flowType.setType(type);
        List<FlowType> list = this.flowTypeService.list(new QueryWrapper<>(flowType));
        List<FlowTypeDto> results = new ArrayList<>();
        for (FlowType flowType1 : list) {
            FlowTypeDto flowTypeDto = new FlowTypeDto();
            BeanUtil.copyProperties(flowType1, flowTypeDto);
            switch (languageType) {
                case "1":
                    flowTypeDto.setSources(flowType1.getSources());
                    break;
                case "2":
                    flowTypeDto.setSources(flowType1.getHkSources());
                    break;
                case "3":
                    flowTypeDto.setSources(flowType1.getHgSources());
                    break;
                case "4":
                    flowTypeDto.setSources(flowType1.getJpSources());
                    break;
                default:
                    flowTypeDto.setSources(flowType1.getUsSource());
                    break;
            }
            results.add(flowTypeDto);
        }
        return Result.success(results);
    }

    public Result currentTrade(String symbol) {
        if (StringUtils.isBlank(symbol)) {
            return Result.fail(ApiStatus.BAD_REQUEST);
        }
        return Result.success(redisUtil.get(Constant.TRADE_PERPETUAL + symbol));
    }

    public Result currentInfo(String symbol) {
        if (StringUtils.isBlank(symbol)) {
            return Result.fail(ApiStatus.BAD_REQUEST);
        }
        Swap swap = new Swap();
        swap.setSymbol(symbol);
        swap.setDel("N");
        swap = swapService.getOne(new QueryWrapper<>(swap));
        if (swap == null) return Result.fail(ApiStatus.BAD_REQUEST);
        Map<String, Object> map = new HashMap<>();
        map.put("handNumber", swap.getHandNumber());
        map.put("price", 1 / (Math.pow(10, swap.getNumber())));
        return Result.success(map);
    }

    public Result futuresTrade(String symbol) {
        if (StringUtils.isBlank(symbol)) {
            return Result.fail(ApiStatus.BAD_REQUEST);
        }
        return Result.success(redisUtil.get(Constant.TRADE_PERPETUAL + symbol));
    }

    public Result futuresInfo(String symbol) {
        if (StringUtils.isBlank(symbol)) {
            return Result.fail(ApiStatus.BAD_REQUEST);
        }
        Futures swap = homeService.getFuturesOne(symbol);
        if (swap == null) return Result.fail(ApiStatus.BAD_REQUEST);
        Map<String, Object> map = new HashMap<>();
        map.put("handNumber", swap.getHandNumber());
        map.put("price", 1 / (Math.pow(10, swap.getNumber().intValue())));
        return Result.success(map);
    }

    public Object markTicket() {
        Map<String, Object> map = new HashMap<>();
        map.put("date", new Date().getTime() / 1000);
        List<Map<String, Object>> ticket = new ArrayList<>();
        map.put("ticker", ticket);
        if (redisUtil.get("MARKET_TICKET_LIST") != null) {
            map.put("ticker", redisUtil.get("MARKET_TICKET_LIST"));
        }
        return map;
    }

    public Object swapTicket() {
        Map<String, Object> map = new HashMap<>();
        map.put("date", new Date().getTime() / 1000);
        List<Map<String, Object>> ticket = new ArrayList<>();
        map.put("ticker", ticket);
        if (redisUtil.get("SWAP_TICKET_LIST") != null) {
            map.put("ticker", redisUtil.get("SWAP_TICKET_LIST"));
        }
        return map;
    }

    public Result changerSymbolsList() {
        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("symbols", "USDT-ERC20");
        results.add(map);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("symbols", "USDT-TRC20");
        results.add(map1);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("symbols", "USDT-OMNI");
        results.add(map2);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("symbols", "BTC");
        results.add(map3);
        Map<String, Object> map4 = new HashMap<>();
        map4.put("symbols", "ETH");
        results.add(map4);
        return Result.success(results);
    }

    public Result getCategoryList() {
        InformationCategory informationCategory = new InformationCategory();
        informationCategory.setDel("N");
        informationCategory.setStatus("Y");
        List<InformationCategory> categoryList = this.informationCategoryService.list(new QueryWrapper<>(informationCategory).orderByAsc("sort"));
        List<Map<String, Object>> list = new ArrayList<>(categoryList.size());
        for (InformationCategory category : categoryList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", category.getId());
            map.put("title", category.getTitle());
            map.put("sort", category.getSort());
            list.add(map);
        }
        return Result.success(list);
    }

    public Result informationList(Long categoryId, Page page) {
        Information information = new Information();
        information.setCategoryId(categoryId).setDel(N);
        return success(informationService.page(page, new QueryWrapper<>(information).orderByDesc(CREATE_TIME)));
    }

    public Result informationDetail(Long id) {
        Information information = this.informationService.getById(id);
        return Result.success(information);
    }

    public Result finPeriodicList() {
        Periodic periodic = new Periodic();
        periodic.setIsDelete("N");
        List<Periodic> list = finPeriodicService.list(new QueryWrapper<>(periodic));
        return Result.success(list);
    }

    public Result teManagementList(Page page) {
        Date date = new Date();
        //page.setSize(3L);
        TeManagement teManagement = new TeManagement();
        teManagement.setStatus("1");
        QueryWrapper<TeManagement> tWrapper = new QueryWrapper<>(teManagement);
        tWrapper.gt("end_time", date);
        tWrapper.lt("start_time", date);
        IPage page1 = teManagementService.page(page, tWrapper);
        //teManagementService.list(new QueryWrapper<>(teManagement))
        return Result.success(page1);
    }

    public Result teManagementListAll() {
        TeManagement teManagement = new TeManagement();
        Date date = new Date();
        teManagement.setStatus("1");
        QueryWrapper<TeManagement> teManagementQueryWrapper = new QueryWrapper<>(teManagement);
        teManagementQueryWrapper.gt("end_time", date);
        teManagementQueryWrapper.lt("start_time", date);
        return Result.success(teManagementService.list(teManagementQueryWrapper));
    }

    public Result knowTeManagementList(Long id) {
        TeManagementLog teManagementLQ = new TeManagementLog();
        teManagementLQ.setStatus("0");
        teManagementLQ.setSalesmanId(id);
        BigDecimal sums = BigDecimal.ZERO;
        List<TeManagementLog> teMaListLog = teManagementLogService.list(new QueryWrapper<>(teManagementLQ));
        if (ToolUtil.isNotEmpty(teMaListLog)) {
            for (TeManagementLog teManagementLog : teMaListLog) {
                sums = sums.add(new BigDecimal(teManagementLog.getSum()));
            }
        }
        return Result.success(sums);
    }

    public Result miningList(Page page) {
        Date date = new Date();
        //page.setSize(3L);
        Mining mining = new Mining();
        mining.setStep("1");
        mining.setStatus("Y");
        mining.setDel("N");

        IPage page1 = miningService.page(page, new QueryWrapper<>(mining));
        //teManagementService.list(new QueryWrapper<>(teManagement))
        return Result.success(page1);
    }

    public List teamRewardsList() {
        TeamRewards teamRewards = new TeamRewards();
        teamRewards.setDel("N");
        List<TeamRewards> list = teamRewardsService.list(new QueryWrapper<>(teamRewards).eq("del", "N").orderByAsc("number"));
        return list;
    }

    public Result platformRate(String code) {
        PlatfomrRate platfomrRate = new PlatfomrRate();
        platfomrRate.setDel("N");
        if (StringUtils.isNotBlank(code)) {
            platfomrRate.setRateCode(code);
            return Result.success(platfomrRateService.getOne(new QueryWrapper<>(platfomrRate)));
        }
        QueryWrapper queryWrapper = new QueryWrapper<>(platfomrRate);
        queryWrapper.orderByAsc("sort");
        return Result.success(platfomrRateService.list(queryWrapper));

    }

    public Result community(String language) {
        Community community = new Community();
        community.setDel("N");
        community.setLanguage(language);
        Community one = communityService.getOne(new QueryWrapper<>(community));
        return Result.success(one);
    }
}
