package cn.stylefeng.guns.modular.websocket.api;

import cn.stylefeng.guns.modular.app.common.ApiStatus;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.websocket.service.ImBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;

/**
 * 交易所行情接口
 */
@RestController
@CrossOrigin
public class ImBuyApi
{


    @Autowired
    ImBuyService imBuyService;


//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    RedisUtil redisUtil;



//
//    /**
//     * 订阅
//     * @param token
//     * @return
//     * @throws Exception
//     */
//    @MessageMapping("/sub/{token}")
//    @SendTo("/topic/status")
//    public String sub(@DestinationVariable String token) throws Exception
//    {
//        return ptpOpenService.sub(token);
//    }
//
//      /**
//     * 取消订阅
//     * @param token
//     * @return
//     * @throws Exception
//     */
//    @MessageMapping("/cancel/{token}")
//    @SendTo("/topic/status")
//    public String cancel(@DestinationVariable String token) throws Exception
//    {
//        return ptpOpenService.cancel(token);
//    }



    /**
     *
     *    火币实时行情 ：/topic/ticket.$symbol
     * @return
     * @throws Exception
     */
//    @Scheduled(initialDelay = 7000, fixedRate = 100)
    public String huobiTicket() throws Exception
    {
        imBuyService.huobiTicket();
        return "huobiTicket";
    }


    /**
     *
     *    K线 ：/topic/$symbol.%period
     * @return
     * @throws Exception
     */
//    @Scheduled(initialDelay = 7000, fixedRate = 100)
    public String kline() throws Exception
    {
        imBuyService.kline();
        return "kline";
    }


    /**
     * 深度数据：   /topic/depth.$symbol
     * @return
     * @throws Exception
     */
//    @Scheduled(initialDelay = 7000, fixedRate = 100)
    public String depth() throws Exception
    {

        imBuyService.depth();
        return "depth";
    }



//    /**
//     * 页面信息   /user/{token}/info
//     * @return
//     * @throws Exception
//     */
//    @Scheduled(initialDelay = 7000, fixedRate = 1000)
//    public String page() throws Exception
//    {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        ptpOpenService.ptpPage();
//
//        return "serverTimeToUser";
//    }






    /**
     * ====================    test    ==============
     */


    /**
     * 这里用于客户端推送消息到服务端，推送地址为：setApplicationDestinationPrefixes("/app")设置得前缀加上@MessageMapping注解得地址
     * 此处为/app/send
     * 当前方法处理后将结果转发给@SendTo注解得地址，如果没有指定，则采用默认
     *
     * @param message 客户端推送过来得消息，一般生产环境会封装
     * @return
     * @throws Exception
     * @MessageMapping 指定要接收消息的地址，类似@RequestMapping。除了注解到方法上，也可以注解到类上
     * @SendTo 默认消息将被发送到与传入消息相同的目的地，但是目的地前面附加前缀（默认情况下为"/topic")
     */
    @MessageMapping("/send")
    @SendTo("/topic/web-to-server-to-web")
    public String greeting(String message) throws Exception
    {

        // 模拟延时，以便测试客户端是否在异步工作
        Thread.sleep(1000);
        return "Hello, " + message + "!";
    }
    /**
     * 最基本的服务器端主动推送消息给客户端
     *
     * @return
     * @throws Exception
     */
////    @Scheduled(initialDelay = 7000, fixedRate = 2000)
//    public String serverTime() throws Exception
//    {
//        // 发现消息
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        messagingTemplate.convertAndSend("/topicTest/servertime", df.format(new Date()));
//        messagingTemplate.convertAndSend("/topic/servertime", ptpOpenService.ptpPage());
//        return "servertime";
//    }

    /**
     * 服务端推送消息到指定用户得客户端
     * 例如以下将会推送到
     * 因为设置了setUserDestinationPrefix("/userTest")，因此推送到/userTest/fanqi/info
     * 如果没有设置setUserDestinationPrefix()，则默认前端为user，将会推送到/user/fanqi/info
     * 客户端订阅/userTest/fanqi/info将会收到服务端推送得消息
     *
     * @return
     * @throws Exception
     */
//    @Scheduled(initialDelay = 7000, fixedRate = 2000)
//    public String serverTimeToUser() throws Exception
//    {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        messagingTemplate.convertAndSendToUser("fanqi", "/info", df.format(new Date()));
//        return "serverTimeToUser";
//    }

}
