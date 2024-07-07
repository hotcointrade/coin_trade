package cn.stylefeng.guns.modular.websocket.service;


import cn.stylefeng.guns.modular.app.controller.PersonalController;
import cn.stylefeng.guns.modular.app.controller.market.Detail;
import cn.stylefeng.guns.modular.app.controller.market.Kline;
import cn.stylefeng.guns.modular.app.controller.market.dto.KlineDto;
import cn.stylefeng.guns.modular.app.service.CommonService;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ImBuyService extends Constant
{

    @Autowired
    CommonService commonService;

//    @Autowired
//    SimpMessagingTemplate messagingTemplate;

    @Autowired
    RedisUtil redisUtil;

    public void kline()
    {

//        for (KlineType klineType : KlineType.values())
//        {
//            for (PeriodType periodType : PeriodType.values())
//            {
//                KlineDto dto = new KlineDto();
//                dto.setSize(1000)
//                        .setPeriod(periodType.code)
//                        .setSymbol(klineType.code);
//
//                Result result = commonService.kline(dto);
//                String sub = "/topic/%s.%s";//第一个%s为币种小写 第二个为分时
//                sub = String.format(sub, klineType.value, dto.getPeriod());
//
//                messagingTemplate.convertAndSend(sub, result);
//            }
//
//        }

    }

    public Object depth()
    {
//        for (KlineType klineType : KlineType.values())
//        {
//            String sub = "/topic/depth.%s";//第一个%s为币种小写
//            sub = String.format(sub, klineType.value);
//            messagingTemplate.convertAndSend(sub, commonService.tradeList(klineType.code,0));
//        }

        return "depth深度数据" + System.currentTimeMillis();
    }

    public void huobiTicket()
    {
//        BigDecimal cnyUsdt = (BigDecimal) redisUtil.get(CNY_USDT);
//        for (KlineType klineType : KlineType.values())
//        {
//            String sub = "/topic/ticket.%s";//第一个%s为币种小写
//            sub = String.format(sub, klineType.value);
//            Detail detail = (Detail) redisUtil.get(TF_TICKET + klineType.code);
//            Kline entity = (Kline) redisUtil.get(KINE + klineType.code + _NEW);
//            float spread = entity.getClose() - entity.getOpen();
//            float rose = spread / entity.getOpen();
//            entity.setRose(rose).setCny(new BigDecimal(entity.getClose()).divide(cnyUsdt, 2, RoundingMode.DOWN))
//                    .setSymbol(klineType.code)
//                    .setAmount(detail.getAmount())
//            ;
//            messagingTemplate.convertAndSend(sub, success(entity));
//        }
    }


}
