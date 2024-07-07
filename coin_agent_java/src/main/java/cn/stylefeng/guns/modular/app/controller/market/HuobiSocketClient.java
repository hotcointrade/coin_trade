package cn.stylefeng.guns.modular.app.controller.market;

/**
 * 火币socket客户端
 */

//@Component
public class HuobiSocketClient
{

//    private static final Logger logger = LoggerFactory.getLogger(HuobiSocketClient.class);
//
//    @Autowired
//    private Client client;
//
//    @Autowired
//    private RedisUtil redisUtil;
//
//    @PostConstruct
//    public void run()
//    {
//        try
//        {
//            URI uri = new URI("wss://api.huobi.pro/ws");
//            new Thread(() ->
//            {
//                while (true)
//                {
//                    try
//                    {
//                        Thread.sleep(60000);
//                        WebSocketc ws = new WebSocketc(uri);
//                        client.connect(ws);
//
//                        WebSocketDepth wsD = new WebSocketDepth(uri);
//                        client.connect(wsD);
//
//                    } catch (InterruptedException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//            WebSocketc ws = new WebSocketc(uri);
//            client.connect(ws);
//
//
//
//        } catch (URISyntaxException e)
//        {
//            e.printStackTrace();
//        }
//    }
//}
//
///**
// * k线 及实时行情
// */
//class WebSocketc extends WebSocketClient
//{
//
//
//    public static String KLINE = "market.%s.kline.%s";
//    public static String DEPTH = "market.%s.depth.step1";
//    private static final Logger logger = LoggerFactory.getLogger(WebSocketc.class);
//    private BigDecimal cnyUsdt;
//
//    public WebSocketc(URI uri)
//    {
//        super(uri, new Draft_6455());
//        this.uri = uri;
////        this.cnyUsdt = (BigDecimal) cny;
//    }
//
//    @Override
//    public void onOpen(ServerHandshake shake)
//    {
//
//        for (ProConst.KlineType klineType : ProConst.KlineType.values())
//        {
//
//            String symbols = klineType.code;
//            String[] symbol = symbols.split("/");
//            String currency = symbol[0].toLowerCase() + symbol[1].toLowerCase();
////            String depth=String.format(DEPTH,currency);
////            sendWsDepth("sub", depth);
//            for (ProConst.PeriodType periodType : ProConst.PeriodType.values())
//            {
//                String topic = String.format(KLINE, currency, periodType.code);
//                sendWsMarket("sub", topic);
//            }
//
//        }
//
//
//    }
//
//    @Override
//    public void onMessage(String arg0)
//    {
//        if (arg0 != null)
//        {
//            System.out.println("receive message " + arg0);
//        }
//    }
//
//    @Override
//    public void onError(Exception arg0)
//    {
//        String message = "";
//        try
//        {
//            message = new String(arg0.getMessage().getBytes(), "UTF-8");
//        } catch (UnsupportedEncodingException e)
//        {
//            e.printStackTrace();
//            System.out.println("has error ,the message is :" + message);
//        }
//    }
//
//    @Override
//    public void onClose(int arg0, String arg1, boolean arg2)
//    {
////        System.out.println("connection close ");
////        System.out.println(arg0 + "   " + arg1 + "  " + arg2);
//    }
//
//    @Override
//    public void onMessage(ByteBuffer bytes)
//    {
//        try
//        {
//            String message = new String(ZipUtils.decompress(bytes.array()), "UTF-8");
//            JSONObject jsonObject = JSONObject.parseObject(message);
//            if (!"".equals(message))
//            {
//                if (message.indexOf("ping") > 0)
//                {
//                    String pong = jsonObject.toString();
//                    send(pong.replace("ping", "pong"));
//                }
//                else
//                {
//                    MergedResponse<JSONObject> kline = JSONObject.toJavaObject(jsonObject, MergedResponse.class);
//                    if (kline.getCh() == null)
//                        return;
//                    String[] ch = kline.getCh().split("\\.");
//                    String symbol = ch[1].substring(0, 3).toUpperCase() + "/USDT";
//                    String time = ch[3];
//                    if (kline != null)
//                    {
//                        Kline klines = JSONObject.toJavaObject(kline.tick, Kline.class);
//
//                        String key = Constant.KINE + symbol + "_" + time;
//                        PromotionFactory.me().redisUtil.set(key+"_new", klines);
//                    }
//                }
//            }
//        } catch (CharacterCodingException e)
//        {
//            e.printStackTrace();
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendWsMarket(String op, String topic)
//    {
//        JSONObject req = new JSONObject();
//        req.put(op, topic);
//        send(req.toString());
//    }
//    public void sendWsDepth(String op, String topic)
//    {
//        JSONObject req = new JSONObject();
//        req.put(op, topic);
//        send(req.toString());
//    }
//
//    public void sendWsMarketKline(String op, String topic, String from, String to)
//    {
//        JSONObject req = new JSONObject();
//        req.put(op, topic);
//        req.put("from", from);
//        req.put("to", to);
//        send(req.toString());
//    }
//
//
//}
//
///**
// * 深度
// */
//class WebSocketDepth extends WebSocketClient
//{
//
//
//    //    public static String KLINE = "market.%s.kline.%s";
//    public static String DEPTH = "market.%s.depth.step1";
//    private static final Logger logger = LoggerFactory.getLogger(WebSocketc.class);
//    private BigDecimal cnyUsdt;
//
//    public WebSocketDepth(URI uri)
//    {
//        super(uri, new Draft_6455());
//        this.uri = uri;
////        this.cnyUsdt = (BigDecimal) cny;
//    }
//
//    @Override
//    public void onOpen(ServerHandshake shake)
//    {
//
//        for (ProConst.KlineType klineType : ProConst.KlineType.values())
//        {
//
//            String symbols = klineType.code;
//            String[] symbol = symbols.split("/");
//            String currency = symbol[0].toLowerCase() + symbol[1].toLowerCase();
//            String depth = String.format(DEPTH, currency);
//            sendWsDepth("sub", depth);
//        }
//
//
//    }
//
//    @Override
//    public void onMessage(String arg0)
//    {
//        if (arg0 != null)
//        {
//            System.out.println("receive message " + arg0);
//        }
//    }
//
//    @Override
//    public void onError(Exception arg0)
//    {
//        String message = "";
//        try
//        {
//            message = new String(arg0.getMessage().getBytes(), "UTF-8");
//        } catch (UnsupportedEncodingException e)
//        {
//            e.printStackTrace();
//            System.out.println("has error ,the message is :" + message);
//        }
//    }
//
//    @Override
//    public void onClose(int arg0, String arg1, boolean arg2)
//    {
////        System.out.println("connection close ");
//        System.out.println(arg0 + "   " + arg1 + "  " + arg2);
//    }
//
//    @Override
//    public void onMessage(ByteBuffer bytes)
//    {
//        try
//        {
//            String message = new String(ZipUtils.decompress(bytes.array()), "UTF-8");
//            JSONObject jsonObject = JSONObject.parseObject(message);
//            if (!"".equals(message))
//            {
//                if (message.indexOf("ping") > 0)
//                {
//                    String pong = jsonObject.toString();
//                    send(pong.replace("ping", "pong"));
//                }
//                else
//                {
//                    MergedResponse<JSONObject> kline = JSONObject.toJavaObject(jsonObject, MergedResponse.class);
//                    if (kline.getCh() == null)
//                        return;
//                    String[] ch = kline.getCh().split("\\.");
//                    String symbol = ch[1].substring(0, 3).toUpperCase() + "/USDT";
////                    String time = ch[3];
//                    if (kline != null)
//                    {
//                        Depth klines = JSONObject.toJavaObject(kline.tick, Depth.class);
//
////                        String key = Constant.DEPTH + symbol + "_" + time;
//                        String key = Constant.DEPTH + symbol;
//
//                        PromotionFactory.me().redisUtil.set(key, klines);
//                    }
//                }
//            }
//        } catch (CharacterCodingException e)
//        {
//            e.printStackTrace();
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendWsMarket(String op, String topic)
//    {
//        JSONObject req = new JSONObject();
//        req.put(op, topic);
//        send(req.toString());
//    }
//
//    public void sendWsDepth(String op, String topic)
//    {
//        JSONObject req = new JSONObject();
//        req.put(op, topic);
//        send(req.toString());
//    }
//
//    public void sendWsMarketKline(String op, String topic, String from, String to)
//    {
//        JSONObject req = new JSONObject();
//        req.put(op, topic);
//        req.put("from", from);
//        req.put("to", to);
//        send(req.toString());
//    }
}
