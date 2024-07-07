package cn.stylefeng.guns.modular.app.controller.market;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import okhttp3.*;
import okhttp3.OkHttpClient.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * API client.
 *
 * @Date 2018/1/14
 * @Time 16:02
 */
public class ApiClientSwap {

    static final int CONN_TIMEOUT = 5;
    static final int READ_TIMEOUT = 5;
    static final int WRITE_TIMEOUT = 5;

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);


    static final String API_URL = "https://api.btcgateway.pro/";
    //    static final String API_URL = "https://api.huobi.br.com";
    static final String API_HOST = getHost();

    static final MediaType JSON = MediaType.parse("application/json");
    static final OkHttpClient client = createOkHttpClient();

    final String accessKeyId;
    final String accessKeySecret;
    final String assetPassword;

    /**
     * 创建一个ApiClient实例
     *
     * @param accessKeyId     AccessKeyId
     * @param accessKeySecret AccessKeySecret
     */
    public ApiClientSwap(String accessKeyId, String accessKeySecret) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.assetPassword = null;
    }

    /**
     * 创建一个ApiClient实例
     *
     * @param accessKeyId     AccessKeyId
     * @param accessKeySecret AccessKeySecret
     * @param assetPassword   AssetPassword
     */
    public ApiClientSwap(String accessKeyId, String accessKeySecret, String assetPassword) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.assetPassword = assetPassword;
    }

//    /**
//     * 查询交易对
//     *
//     * @return List of symbols.
//     */
//    public List<Symbol> getSymbols() {
//        ApiResponse<List<Symbol>> resp =
//                get("/v1/common/symbols", null, new TypeReference<ApiResponse<List<Symbol>>>() {
//                });
//        return resp.checkAndReturn();
//    }
//
//    /**
//     * 查询所有账户信息
//     *
//     * @return List of accounts.
//     */
//    public List<Account> getAccounts() {
//        ApiResponse<List<Account>> resp =
//                get("/v1/account/accounts", null, new TypeReference<ApiResponse<List<Account>>>() {
//                });
//        return resp.checkAndReturn();
//    }
//
//    /**
//     * 创建订单
//     *
//     * @param request CreateOrderRequest object.
//     * @return Order id.
//     */
//    public Long createOrder(CreateOrderRequest request) {
//        ApiResponse<Long> resp =
//                post("/v1/order/orders/place", request, new TypeReference<ApiResponse<Long>>() {
//                });
//        return resp.checkAndReturn();
//    }
//
//    /**
//     * 执行订单
//     *
//     * @param orderId The id of created order.
//     * @return Order id.
//     */
//    public String placeOrder(long orderId) {
//        ApiResponse<String> resp = post("/v1/order/orders/" + orderId + "/place", null,
//                new TypeReference<ApiResponse<String>>() {
//                });
//        return resp.checkAndReturn();
//    }


    // ----------------------------------------行情API-------------------------------------------

    /**
     * GET GET /swap-ex/market/detail/merged 获取 Market Detail 24小时成交量数据
     *
     * @param symbol
     * @return
     */


     /**
     * GET /swap-ex/market/history/kline 获取K线数据(永续）
     *
     * @param symbol
     * @param period
     * @param size
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public KlineResponse klineSwapEx(String symbol, String period, String size) {
        HashMap map = new HashMap();
        map.put("contract_code", symbol);
        map.put("period", period);
        map.put("size", size);
        KlineResponse resp = get("/swap-ex/market/history/kline", map, new TypeReference<KlineResponse<List<Kline>>>() {
        });
        if (resp != null) {
            return resp;
        }
        return null;
    }



    /**
     * GET /market/detail/merged 获取聚合行情(Ticker)
     *
     * @param symbol
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public MergedResponse merged(String symbol) {
        HashMap map = new HashMap();
        map.put("symbol", symbol);
//        MergedResponse resp = get("/market/detail/merged", map, new TypeReference<MergedResponse<List<Merged>>>() {
//        });
        MergedResponse resp = get("/market/detail/merged", map, new TypeReference<MergedResponse<Merged>>() {
        });
        return resp;
    }


//    public static void main(String[] args) throws IOException {
//        KlineResponse s= getApiClient().klineSwapEx("BTC-USD","1min","10");
//        System.out.println(s.toString());
//
////
////        String httpPost =HttpUtil.get("https://api.huobi.pro/market/detail/merged?symbol=ethusdt");
////        System.out.println(";;"+httpPost);
//    }
    static final String API_KEY = "40e0f1a6-a983d11f-19351346-7ec30";

    static final String API_SECRET = "8be8da24-a6454280-2edde4ee-85a8a";
    private static volatile ApiClientSwap clienta;
    public static ApiClientSwap getApiClient() {
        if (clienta == null) {
            synchronized (ApiClient.class) {
                if (clienta == null) {
                    clienta = new ApiClientSwap(API_KEY, API_SECRET);
                }
            }
        }
        return clienta;
    }
//    /**
//     * GET /market/depth 获取 Market Depth 数据
//     *
//     * @param request
//     * @return
//     */
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    public DepthResponse depth(DepthRequest request) {
//        HashMap map = new HashMap();
//        map.put("symbol", request.getSymbol());
//        map.put("type", request.getType());
//
//        DepthResponse resp = get("/market/depth", map, new TypeReference<DepthResponse<List<Depth>>>() {
//        });
//        return resp;
//    }

//    /**
//     * GET /market/trade 获取 Trade Detail 数据
//     *
//     * @param symbol
//     * @return
//     */
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    public TradeResponse trade(String symbol) {
//        HashMap map = new HashMap();
//        map.put("symbol", symbol);
//        TradeResponse resp = get("/market/trade", map, new TypeReference<TradeResponse>() {
//        });
//        return resp;
//    }

    /**
     * GET /market/history/trade 批量获取最近的交易记录
     *
     * @param symbol
     * @param size
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public HistoryTradeResponse historyTrade(String symbol, String size) {
        HashMap map = new HashMap();
        map.put("symbol", symbol);
        map.put("size", size);
        HistoryTradeResponse resp = get("/market/history/trade", map, new TypeReference<HistoryTradeResponse>() {
        });
        return resp;
    }

    /**
     * GET /swap-ex/market/detail/merged  获取 Market Detail 24小时成交量数据
     *
     * @param symbol
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public DetailResponse detail(String symbol) {
        HashMap map = new HashMap();
        map.put("contract_code", symbol);
        DetailResponse resp = get("/swap-ex/market/detail/merged", map, new TypeReference<DetailResponse<Detail>>() {
        });
        return resp;
    }






//    /**
//     * GET /v1/common/symbols 查询系统支持的所有交易对及精度
//     *
//     * @param symbol
//     * @return
//     */
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    public SymbolsResponse symbols(String symbol) {
//        HashMap map = new HashMap();
//        map.put("symbol", symbol);
//        SymbolsResponse resp = get("/v1/common/symbols", map, new TypeReference<SymbolsResponse<Symbols>>() {
//        });
//        return resp;
//    }

//    /**
//     * GET /v1/common/currencys 查询系统支持的所有币种
//     *
//     * @param symbol
//     * @return
//     */
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    public CurrencysResponse currencys(String symbol) {
//        HashMap map = new HashMap();
//        map.put("symbol", symbol);
//        CurrencysResponse resp = get("/v1/common/currencys", map, new TypeReference<CurrencysResponse>() {
//        });
//        return resp;
//    }

//    /**
//     * GET /v1/common/timestamp 查询系统当前时间
//     *
//     * @return
//     */
//    public TimestampResponse timestamp() {
//        TimestampResponse resp = get("/v1/common/timestamp", null, new TypeReference<TimestampResponse>() {
//        });
//        return resp;
//    }

//    /**
//     * GET /v1/account/accounts 查询当前用户的所有账户(即account-id)
//     *
//     * @return
//     */
//    @SuppressWarnings({"rawtypes"})
//    public AccountsResponse accounts() {
//        AccountsResponse resp = get("/v1/account/accounts", null, new TypeReference<AccountsResponse<List<Accounts>>>() {
//        });
//        return resp;
//    }

//    /**
//     * GET /v1/account/accounts/{account-id}/balance 查询指定账户的余额
//     *
//     * @param accountId
//     * @return
//     */
//    @SuppressWarnings({"rawtypes"})
//    public BalanceResponse balance(String accountId) {
//        BalanceResponse resp = get("/v1/account/accounts/" + accountId + "/balance", null, new TypeReference<BalanceResponse<Balance>>() {
//        });
//        return resp;
//    }
//
//    /**
//     * POST /v1/order/orders/{order-id}/submitcancel 申请撤销一个订单请求
//     *
//     * @param orderId
//     * @return
//     */
//    public SubmitcancelResponse submitcancel(String orderId) {
//        SubmitcancelResponse resp = post("/v1/order/orders/" + orderId + "/submitcancel", null, new TypeReference<SubmitcancelResponse>() {
//        });
//        return resp;
//    }
//
//    /**
//     * POST /v1/order/orders/batchcancel 批量撤销订单
//     *
//     * @param orderList
//     * @return
//     */
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    public BatchcancelResponse submitcancels(List orderList) {
//        Map<String, List> parameterMap = new HashMap();
//        parameterMap.put("order-ids", orderList);
//        BatchcancelResponse resp = post("/v1/order/orders/batchcancel", parameterMap, new TypeReference<BatchcancelResponse<Batchcancel<List, List<BatchcancelBean>>>>() {
//        });
//        return resp;
//    }
//
//    /**
//     * GET /v1/order/orders/{order-id} 查询某个订单详情
//     *
//     * @param orderId
//     * @return
//     */
//    @SuppressWarnings({"rawtypes"})
//    public OrdersDetailResponse ordersDetail(String orderId) {
//        OrdersDetailResponse resp = get("/v1/order/orders/" + orderId, null, new TypeReference<OrdersDetailResponse>() {
//        });
//        return resp;
//    }


//    /**
//     * GET /v1/order/orders/{order-id}/matchresults 查询某个订单的成交明细
//     *
//     * @param orderId
//     * @return
//     */
//    @SuppressWarnings({"rawtypes"})
//    public MatchresultsOrdersDetailResponse matchresults(String orderId) {
//        MatchresultsOrdersDetailResponse resp = get("/v1/order/orders/" + orderId + "/matchresults", null, new TypeReference<MatchresultsOrdersDetailResponse>() {
//        });
//        return resp;
//    }
//
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    public IntrustDetailResponse intrustOrdersDetail(IntrustOrdersDetailRequest req) {
//        HashMap map = new HashMap();
//        map.put("symbol", req.symbol);
//        map.put("states", req.states);
//        if (req.startDate != null) {
//            map.put("startDate", req.startDate);
//        }
//        if (req.startDate != null) {
//            map.put("start-date", req.startDate);
//        }
//        if (req.endDate != null) {
//            map.put("end-date", req.endDate);
//        }
//        if (req.types != null) {
//            map.put("types", req.types);
//        }
//        if (req.from != null) {
//            map.put("from", req.from);
//        }
//        if (req.direct != null) {
//            map.put("direct", req.direct);
//        }
//        if (req.size != null) {
//            map.put("size", req.size);
//        }
//        IntrustDetailResponse resp = get("/v1/order/orders/", map, new TypeReference<IntrustDetailResponse<List<IntrustDetail>>>() {
//        });
//        return resp;
//    }

//  public IntrustDetailResponse getALlOrdersDetail(String orderId) {
//    IntrustDetailResponse resp = get("/v1/order/orders/"+orderId, null,new TypeReference<IntrustDetailResponse>() {});
//    return resp;
//  }


    // send a GET request.
    <T> T get(String uri, Map<String, String> params, TypeReference<T> ref) {
        if (params == null) {
            params = new HashMap<>();
        }
        return call("GET", uri, null, params, ref);
    }

    // send a POST request.
    <T> T post(String uri, Object object, TypeReference<T> ref) {
        return call("POST", uri, object, new HashMap<String, String>(), ref);
    }

    // call api by endpoint.
    <T> T call(String method, String uri, Object object, Map<String, String> params,
               TypeReference<T> ref) {
        ApiSignature sign = new ApiSignature();
        sign.createSignature(this.accessKeyId, this.accessKeySecret, method, API_HOST, uri, params);
        String s = "";
        try {
            Request.Builder builder = null;
            if ("POST".equals(method)) {
                RequestBody body = RequestBody.create(JSON, JsonUtil.writeValue(object));
                builder = new Request.Builder().url(API_URL + uri + "?" + toQueryString(params)).post(body);
            } else {
                builder = new Request.Builder().url(API_URL + uri + "?" + toQueryString(params)).get();
            }
            if (this.assetPassword != null) {
                builder.addHeader("AuthData", authData());
            }
            Request request = builder.build();
            Response response = client.newCall(request).execute();
            s = response.body().string();
//            System.out.println("-----s:"+JsonUtil.writeValue(s));
            return JsonUtil.readValue(s, ref);
        } catch (IOException e) {
//            logger.info("获取火币接口请求超时");
        }
        return null;
    }

    String authData() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(this.assetPassword.getBytes(StandardCharsets.UTF_8));
        md.update("hello, moto".getBytes(StandardCharsets.UTF_8));
        Map<String, String> map = new HashMap<>();
        map.put("assetPwd", DatatypeConverter.printHexBinary(md.digest()).toLowerCase());
        try {
            return ApiSignature.urlEncode(JsonUtil.writeValue(map));
        } catch (IOException e) {
            throw new RuntimeException("Get json failed: " + e.getMessage());
        }
    }

    // Encode as "a=1&b=%20&c=&d=AAA"
    String toQueryString(Map<String, String> params) {
        return String.join("&", params.entrySet().stream().map((entry) -> {
            return entry.getKey() + "=" + ApiSignature.urlEncode(entry.getValue());
        }).collect(Collectors.toList()));
    }

    // create OkHttpClient:
    static OkHttpClient createOkHttpClient() {
        return new Builder().connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    static String getHost() {
        String host = null;
        try {
            host = new URL(API_URL).getHost();
        } catch (MalformedURLException e) {
            System.err.println("parse API_URL error,system exit!,please check API_URL:" + API_URL);
            System.exit(0);
        }
        return host;
    }

}


