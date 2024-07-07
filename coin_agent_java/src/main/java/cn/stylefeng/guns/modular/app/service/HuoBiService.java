package cn.stylefeng.guns.modular.app.service;

import cn.stylefeng.guns.modular.app.controller.market.*;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.util.HttpUtil;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.CookieManager;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;

/**
 * 火币服务类
 */
@Service
public class HuoBiService {


    static final String API_KEY = "514ecaf4-mn8ikls4qg-cd175d78-b628c";

    static final String API_SECRET = "801f539e-2036e1d1-bc9f4e4c-a314a";

    private static volatile ApiClient client;
    private static volatile ApiClientSwap clientSecret;

    public static ApiClient getApiClient() {
        if (client == null) {
            synchronized (ApiClient.class) {
                if (client == null) {
                    client = new ApiClient(API_KEY, API_SECRET);
                }
            }
        }
        return client;
    }
 public static ApiClientSwap getApiClientSwap() {
        if (clientSecret == null) {
            synchronized (ApiClientSwap.class) {
                if (clientSecret == null) {
                    clientSecret = new ApiClientSwap(API_KEY, API_SECRET);
                }
            }
        }
        return clientSecret;
    }

    int number = 0;
    int count = 0;
    @Autowired
    RedisUtil redisUtils;


    /**
     * 获取行情
     *
     * @return
     */
    public MergedResponse getTicket(String symbol) {
        return getApiClient().merged(symbol);
    }

    /**
     * 最近24小时行情数据
     * @return
     */
    public DetailResponse getDetail(String symbol){return getApiClient().detail(symbol);}
    /**
     * 最近24小时行情数据-合约
     * @return
     */
    public DetailResponse getDetailForContract(String symbol){return getApiClientSwap().detail(symbol);}

    /**
     * 现货行情
     * @param symbol
     * @param period
     * @param size
     * @return
     */
    public KlineResponse<Kline> kline(String symbol, String period, String size)
    {
        return getApiClient().kline(symbol,period,size);
    }


    /**
     * 永续行情
     * @param symbol
     * @param period
     * @param size
     * @return
     */
    public KlineResponse<Kline> klineSwapEx(String symbol, String period, String size)
    {
        return getApiClientSwap().klineSwapEx(symbol,period,size);
    }




    public HistoryTradeResponse historyTrade(String symbol, String size){
        return getApiClient().historyTrade(symbol,size);
    }

//    public static void main(String[] args) throws IOException {
//
//        String str=  HttpUtil.get("https://api.huobi.pro/market/detail/merged?symbol=ethusdt");
//        System.out.println(str);
//
//    }

//
//    public Result klineDetail(String symbol, String period, Integer size) {
//        if (size > 2000) {
//            size = 2000;
//        }
//        Object object = (Object) redisUtils.get(period + symbol);
//        if (object != null) {
//            return Result.success("获取详情成功", object);
//        }
//        return Result.fail(-1, "获取数据失败");
//    }
//

//    public Result getSymbols(String symbol) {
//        Object symbolObject = redisUtils.get(symbol);
//        return Result.success(symbolObject);
//    }


//    public void updateMarketsInfo() {
//        boolean flag = true;
//        Long nowTime = System.currentTimeMillis() / 1000;
//        if (count == 0) {
//            redisUtils.del("accessTime", "accessNumber");
//            count++;
//        }
//        if (redisUtils.get("accessTime") == null) {
//            redisUtils.set("accessTime", nowTime);
//        } else {
//            long time = 0;
//            if (redisUtils.get("accessTime") != null) {
//                Long accessTime = (Long) redisUtils.get("accessTime");
//                time = nowTime - Long.valueOf(accessTime);
//            }
//            if (time < 30 && redisUtils.get("accessNumber") != null && ((Integer) redisUtils.get("accessNumber")) > 30) {
//                flag = false;
//            }
//            if (time > 30 && redisUtils.get("accessNumber") != null && ((Integer) redisUtils.get("accessNumber")) > 30) {
//                number = 0;
//                redisUtils.set("accessTime", nowTime);
//                redisUtils.set("accessNumber", number++);
////                System.out.println("超过伍秒，重置," + redisUtils.get("accessNumber") + "------" + redisUtils.get("accessTime"));
//            }
//
//        }
//        if (flag) {
//            redisUtils.set("accessNumber", number++);
//            List<Symbols> symbolsList = symbolsSysMapper.getSymbolsList();
//            for (Symbols symbols : symbolsList) {
//                String[] symbol = symbols.getName().split("/");
//                KlineResponse kline = getApiClient().kline(symbol[0].toLowerCase() + symbol[1].toLowerCase(), "1day", "1");
//                if (kline != null && "ok".equalsIgnoreCase(kline.getStatus())) {
//                    List<Kline> klines = (List<Kline>) kline.checkAndReturn();
//                    if (klines.size() > 0) {
//                        Kline klineObject = klines.get(0);
//                        Map<String, Object> map = new HashMap<String, Object>();
//                        float spread = klineObject.getClose() - klineObject.getOpen();
//                        float rose = spread / klineObject.getOpen();
//                        map.put("symbol", symbols.getName());
//                        map.put("open", klineObject.getOpen());
//                        map.put("close", klineObject.getClose());
//                        map.put("high", klineObject.getHigh());
//                        map.put("low", klineObject.getLow());
//                        map.put("rose", rose);
//                        map.put("vol", klineObject.getVol());
//                        map.put("amount", klineObject.getAmount());
//                        map.put("spread", spread);
//                        redisUtils.set(symbols.getName(), map);
//                    }
//                }
//            }
//        }

//    }


//    public void updateHourDetail() {
//        boolean flag = true;
//        Long nowTime = System.currentTimeMillis() / 1000;
//        if (count == 0) {
//            redisUtils.del("accessTime", "accessNumber");
//            count++;
//        }
//        if (redisUtils.get("accessTime") == null) {
//            redisUtils.set("accessTime", nowTime);
//        } else {
//            long time = 0;
//            if (redisUtils.get("accessTime") != null) {
//                Long accessTime = (Long) redisUtils.get("accessTime");
//                time = nowTime - Long.valueOf(accessTime);
//            }
//            if (time < 30 && redisUtils.get("accessNumber") != null && ((Integer) redisUtils.get("accessNumber")) > 30) {
//                flag = false;
//            }
//            if (time > 30 && redisUtils.get("accessNumber") != null && ((Integer) redisUtils.get("accessNumber")) > 30) {
//                number = 0;
//                redisUtils.set("accessTime", nowTime);
//                redisUtils.set("accessNumber", number++);
////                System.out.println("超过伍秒，重置," + redisUtils.get("accessNumber") + "------" + redisUtils.get("accessTime"));
//            }
//
//        }
//        if (flag) {
//            redisUtils.set("accessNumber", number++);
//            List<Symbols> symbolsList = symbolsSysMapper.getSymbolsList();
//            for (Symbols symbols : symbolsList) {
//                String[] symbol = symbols.getName().split("/");
//                DetailResponse detailResponse = getApiClient().detail(symbol[0].toLowerCase() + symbol[1].toLowerCase());
//                if (detailResponse != null && "ok".equalsIgnoreCase(detailResponse.getStatus())) {
//                    Details detailResponses = (Details) detailResponse.getTick();
//                    if (detailResponses != null) {
//                        redisUtils.set("24" + symbols.getName(), detailResponses);
//                    }
//                }
//            }
//        }
//    }

//    public void updateTradInfo() {
//        boolean flag = true;
//        Long nowTime = System.currentTimeMillis() / 1000;
//        if (count == 0) {
//            redisUtils.del("accessTime", "accessNumber");
//            count++;
//        }
//        if (redisUtils.get("accessTime") == null) {
//            redisUtils.set("accessTime", nowTime);
//        } else {
//            long time = 0;
//            if (redisUtils.get("accessTime") != null) {
//                Long accessTime = (Long) redisUtils.get("accessTime");
//                time = nowTime - Long.valueOf(accessTime);
//            }
//            if (time < 30 && redisUtils.get("accessNumber") != null && ((Integer) redisUtils.get("accessNumber")) > 30) {
//                flag = false;
//            }
//            if (time > 30 && redisUtils.get("accessNumber") != null && ((Integer) redisUtils.get("accessNumber")) > 30) {
//                number = 0;
//                redisUtils.set("accessTime", nowTime);
//                redisUtils.set("accessNumber", number++);
////                System.out.println("超过伍秒，重置," + redisUtils.get("accessNumber") + "------" + redisUtils.get("accessTime"));
//            }
//
//        }
//        if (flag) {
//            redisUtils.set("accessNumber", number++);
//            List<Symbols> symbolsList = symbolsSysMapper.getSymbolsList();
//            for (Symbols symbols : symbolsList) {
//                String[] symbol = symbols.getName().split("/");
//                HistoryTradeResponse historyTradeResponse = getApiClient().historyTrade(symbol[0].toLowerCase() + symbol[1].toLowerCase(), "10");
//                if (historyTradeResponse != null && "ok".equalsIgnoreCase(historyTradeResponse.getStatus())) {
//                    List<HistoryTrade> objectList = (List<HistoryTrade>) historyTradeResponse.getData();
//                    if (objectList != null && objectList.size() > 0) {
//                        List<Map> buyList = new ArrayList<>();
//                        List<Map> sellList = new ArrayList<>();
//                        for (int i = 0; i < objectList.size(); i++) {
//                            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(objectList.get(i));
//                            if (jsonObject != null) {
//                                List<HistoryTrade> historyTrades = (List<HistoryTrade>) jsonObject.get("data");
//                                if (historyTrades != null && historyTrades.size() > 0) {
//                                    for (int j = 0; j < historyTrades.size(); j++) {
//                                        JSONObject jsonObject1 = (JSONObject) JSONObject.toJSON(historyTrades.get(j));
//                                        if (jsonObject1 != null) {
//                                            String direction = (String) jsonObject1.get("direction");
//                                            Double price = (Double) jsonObject1.get("price");
//                                            Double amount = (Double) jsonObject1.get("amount");
//                                            if ("buy".equals(direction) && (buyList == null || buyList.size() < 5)) {
//                                                Map map = new HashMap();
//                                                map.put("price", price);
//                                                map.put("direction", direction);
//                                                map.put("amount", amount);
//                                                buyList.add(map);
//                                            } else if ("sell".equals(direction) && (sellList == null || sellList.size() < 5)) {
//                                                Map map = new HashMap();
//                                                map.put("price", price);
//                                                map.put("direction", direction);
//                                                map.put("amount", amount);
//                                                sellList.add(map);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        redisUtils.set(TRADE + symbols.getName() + "_BUY", buyList);
//                        redisUtils.set(TRADE + symbols.getName() + "_SELL", sellList);
//
//                    }
//                }
//            }
//        }

//    }

//
//    public PageUtil getTradeInfoList(Symbols symbols, PageUtil pageUtil) {
//        int count = symbolsSysMapper.searchSymbolsCount(symbols);
//        // 分页数据
//        int pageTemp = symbols.getPage();
//        int limitTemp = symbols.getLimit();
//        if (count < symbols.getPage() * symbols.getLimit()) {
//            symbols.setLimit(
//                    count - (symbols.getPage() - 1) * symbols.getLimit());
//        }
//        symbols.setPage((pageTemp - 1) * limitTemp);
//        pageUtil.setCount(count);
//        pageUtil.setData(symbolsSysMapper.findList(symbols));
//        return pageUtil;
//    }
//
//
//    public void tradingInfoEditOperation(Symbols symbols) {
//        if (symbols != null && symbols.getId() != null) {
//            symbolsSysMapper.updateSymbols(symbols);
//        }
//    }


//    public void updateSymbolKine(String s) {
//        boolean flag = true;
//        Long nowTime = System.currentTimeMillis() / 1000;
//        if (count == 0) {
//            redisUtils.del("accessTime", "accessNumber");
//            count++;
//        }
//        if (redisUtils.get("accessTime") == null) {
//            redisUtils.set("accessTime", nowTime);
//        } else {
//            long time = 0;
//            if (redisUtils.get("accessTime") != null) {
//                Long accessTime = (Long) redisUtils.get("accessTime");
//                time = nowTime - Long.valueOf(accessTime);
//            }
//            if (time < 30 && redisUtils.get("accessNumber") != null && ((Integer) redisUtils.get("accessNumber")) > 30) {
//                flag = false;
//            }
//            if (time > 30 && redisUtils.get("accessNumber") != null && ((Integer) redisUtils.get("accessNumber")) > 30) {
//                number = 0;
//                redisUtils.set("accessTime", nowTime);
//                redisUtils.set("accessNumber", number++);
////                System.out.println("超过伍秒，重置," + redisUtils.get("accessNumber") + "------" + redisUtils.get("accessTime"));
//            }
//
//        }
//        if (flag) {
//            redisUtils.set("accessNumber", number++);
//            List<Symbols> symbolsList = symbolsSysMapper.getSymbolsList();
//            for (Symbols symbols : symbolsList) {
//                String[] symbol = symbols.getName().split("/");
//                KlineResponse kline = getApiClient().kline(symbol[0].toLowerCase() + symbol[1].toLowerCase(), s, "1000");
//                List<Object> list = new ArrayList<Object>();
//                if (kline != null && "ok".equalsIgnoreCase(kline.getStatus())) {
//                    List<Kline> klines = (List<Kline>) kline.checkAndReturn();
//                    if (klines != null && klines.size() > 0) {
//                        for (int i = 0; i < klines.size(); i++) {
//                            Kline klineObject = klines.get(i);
//                            Map<String, Object> map = new HashMap<String, Object>();
//                            float spread = klineObject.getClose() - klineObject.getOpen();
//                            float rose = spread / klineObject.getOpen();
//                            map.put("id", klineObject.getId());
//                            map.put("symbol", symbols.getName());
//                            map.put("open", klineObject.getOpen());
//                            map.put("close", klineObject.getClose());
//                            map.put("high", klineObject.getHigh());
//                            map.put("low", klineObject.getLow());
//                            map.put("rose", rose);
//                            map.put("vol", klineObject.getVol());
//                            map.put("amount", klineObject.getAmount());
//                            map.put("spread", spread);
//                            list.add(map);
//                        }
//                        redisUtils.set(s + symbols.getName(), list);
//                    }
//                }
//            }
//        }
//    }

//}


}
