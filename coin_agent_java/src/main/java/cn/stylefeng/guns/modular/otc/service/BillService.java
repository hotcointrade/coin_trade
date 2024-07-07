package cn.stylefeng.guns.modular.otc.service;

import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.HomeService;
import cn.stylefeng.guns.modular.app.service.OtcService;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.bulletin.service.SendSMSExtService;
import cn.stylefeng.guns.modular.fin.entity.Legal;
import cn.stylefeng.guns.modular.fin.service.LegalService;
import cn.stylefeng.guns.modular.otc.entity.Buy;
import cn.stylefeng.guns.modular.otc.entity.Sell;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import cn.stylefeng.guns.modular.otc.entity.Bill;
import cn.stylefeng.guns.modular.otc.mapper.BillMapper;
import org.springframework.transaction.annotation.Transactional;

import static cn.stylefeng.guns.modular.base.state.Constant.SYS_PLATFORM;

/**
 * 交易结算订单Service
 *
 * @author yaying.liu
 * @since 2020-07-13 15:03:40
 */
@Service
@Slf4j
public class BillService extends ServiceImpl<BillMapper, Bill>
{

    @Autowired
    LegalService legalService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OtcService otcService;

    @Autowired
    DepositService depositService;

    @Autowired
    BuyService buyService;

    @Autowired
    SellService sellService;

    @Autowired
    SendSMSExtService sendSMSExtService;

    @Autowired
    HomeService homeService;

    //状态：是
    public static final String YES = "1";
    //状态：否
    public static final String NO = "0";

    //状态：审核中
    public static final String CHECK = "2";

    public static final String BUY = "BUY";

    public static final String SELL = "SELL";

    /**
     * 查询交易结算订单
     */
    public Page<Map<String, Object>> selectByCondition(String condition,
                                                       String beginTime, String endTime,
                                                       String status, String buyAccount,
                                                       String buyName, String sellAccount, String sellName,
                                                       String payMethod, String orderNo, String markNo,Long memberId,String recommendIds)
    {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, condition, beginTime, endTime, status, buyAccount, buyName, sellAccount, sellName, payMethod, orderNo, markNo,memberId,recommendIds);
    }

    /**
     * 删除交易结算订单
     */
    public void deleteBill(Long billId)
    {
        Bill entity = this.baseMapper.selectById(billId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加交易结算订单
     */
    public void addBill(Bill bill)
    {
        this.baseMapper.insert(bill);
    }

    public Page<Map<String, Object>> selectAppeal(String condition, String beginTime, String endTime,
                                                  String status, String buyAccount,
                                                  String buyName, String sellAccount,
                                                  String sellName, String payMethod,
                                                  String orderNo, String markNo, String appealStatus,
                                                  String duty,Long memberId,String recommendIds)
    {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectAppeal(page, condition, beginTime, endTime, status,
                buyAccount, buyName, sellAccount, sellName, payMethod, orderNo, markNo, appealStatus, duty,memberId,recommendIds);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseData fail(Long billId)
    {
        Bill bill = this.baseMapper.selectById(billId);

        if (!ProConst.BillStatus.APPEAL.code.equals(bill.getStatus()))
            return ResponseData.error(500, "订单已发生更新，请重新操作");
        if (StrUtil.isBlank(bill.getDuty()))
            return ResponseData.error(500, "请先判责，再放币");

        bill.setStatus(ProConst.BillStatus.CANCEL.code)
                .setAppealStatus(ProConst.AppealStatus.CANCEL.code)
                .setCancelTime(new Date())
                .setUpdateUser(SYS_PLATFORM);
        if (!updateById(bill))
            throw new UpdateDataException(100);

        BigDecimal number = bill.getNumber();
        String coin = bill.getCoin();

        //购买单，加还 挂单出售单数量，如用户撤单了，退还到可用资产
        if (bill.getType().equals(BUY))
        {
            //我是买方

            Sell sell = sellService.getById(bill.getOpid());

            sell.setRestNumber(sell.getRestNumber().add(bill.getNumber())).setUpdateUser(SYS_PLATFORM);
            if (!sellService.updateById(sell))
                throw new UpdateDataException(100);
            if (sell.getStatus().equals("N"))//已撤单，资产解冻
            {

                Legal legal = F.me().getLegal(sell.getMemberId(), sell.getCoin());
                BigDecimal beforeUsed = legal.getUsedPrice();
                BigDecimal afterUsed = legal.getUsedPrice().add(number);
                BigDecimal beforeLock = legal.getLockedPrice();
                BigDecimal afterLock = legal.getLockedPrice().subtract(number);
                legal.setUsedPrice(afterUsed)
                        .setLockedPrice(afterLock)
                        .setUpdateUser(SYS_PLATFORM);
                if (!legalService.updateById(legal))
                    throw new UpdateDataException(100);
                //可用 +
                F.me().saveCashflow(legal.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.OTC_SELL_CANCEL,
                        number, coin, number, coin, BigDecimal.ZERO, coin,
                        ProConst.ItemCode.USED, coin, null, null,
                        beforeUsed, afterUsed, legal.getMemberId(), legal.getMemberId());
                //冻结 -
                F.me().saveCashflow(legal.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.OTC_SELL_CANCEL,
                        number, coin, number, coin, BigDecimal.ZERO, coin,
                        ProConst.ItemCode.LOCKED, coin, null, null,
                        beforeLock, afterLock, legal.getMemberId(), legal.getMemberId());
            }

        }
        //出售单，加还 挂单购买数量 ，同时解冻 出售用户账户数量（冻结转可用），加惩罚机制
        if (bill.getType().equals(SELL))
        {


            //f.注（非处罚条例）：解冻卖家对应币数量
            Buy buy = buyService.getById(bill.getOpid());
            buy.setRestNumber(buy.getRestNumber().add(bill.getNumber())).setUpdateUser(SYS_PLATFORM);
            if (!buyService.updateById(buy))
                throw new UpdateDataException(100);

            Legal legal = F.me().getLegal(bill.getSellMid(), bill.getCoin());

            BigDecimal beforeUsed = legal.getUsedPrice();
            BigDecimal afterUsed = legal.getUsedPrice().add(number);
            BigDecimal beforeLock = legal.getLockedPrice();
            BigDecimal afterLock = legal.getLockedPrice().subtract(number);
            legal.setUsedPrice(afterUsed)
                    .setLockedPrice(afterLock)
                    .setUpdateUser(SYS_PLATFORM);
            if (!legalService.updateById(legal))
                throw new UpdateDataException(100);
            //可用 +
            F.me().saveCashflow(legal.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.OTC_SELL_CANCEL,
                    number, coin, number, coin, BigDecimal.ZERO, coin,
                    ProConst.ItemCode.USED, coin, null, null,
                    beforeUsed, afterUsed, legal.getMemberId(), legal.getMemberId());
            //冻结 -
            F.me().saveCashflow(legal.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.OTC_SELL_CANCEL,
                    number, coin, number, coin, BigDecimal.ZERO, coin,
                    ProConst.ItemCode.LOCKED, coin, null, null,
                    beforeLock, afterLock, legal.getMemberId(), legal.getMemberId());

        }
        String op = "取消";

        //责任方（BUY-买方责任 SELL-卖方责任 NO-双方无责）
        String duty = "BUY".equals(bill.getDuty()) ? "买方" : "SELL".equals(bill.getDuty()) ? "卖方" : "双方无责";

        Member buyMember= F.me().getMember(bill.getBuyMid());
        Member sellMember= F.me().getMember(bill.getSellMid());

        smsDuty(bill,op,duty,buyMember!=null?buyMember:new Member(),sellMember!=null?sellMember:new Member());


        return ResponseData.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseData pass(Long billId)
    {
        Bill entity = this.baseMapper.selectById(billId);

        if (!ProConst.BillStatus.APPEAL.code.equals(entity.getStatus()))
            return ResponseData.error(500, "订单已发生更新，请重新操作");
        if (StrUtil.isBlank(entity.getDuty()))
            return ResponseData.error(500, "请先判责，再放币");

        entity.setStatus(ProConst.BillStatus.FINISH.code)
                .setAppealStatus(ProConst.AppealStatus.PASS.code)
                .setUpdateUser(SYS_PLATFORM);

        if (!updateById(entity))
            throw new UpdateDataException(100);

        //买方 +币 可用添加
        Member buy = F.me().getMember(entity.getBuyMid());
        Legal buyLegal = F.me().getLegal(buy.getMemberId(), entity.getCoin());

        BigDecimal flowPrice = entity.getNumber();
        String coin = entity.getCoin();
        BigDecimal beforeBuy = buyLegal.getUsedPrice();
        BigDecimal afterBuy = buyLegal.getUsedPrice().add(flowPrice);

        buyLegal.setUsedPrice(afterBuy).setUpdateUser(SYS_PLATFORM);
        if (!legalService.updateById(buyLegal))
            throw new UpdateDataException(100);

        //卖方 -币 冻结扣除
        Member otcSell = F.me().getMember(entity.getSellMid());
        Legal otcSellLegal = F.me().getLegal(otcSell.getMemberId(), entity.getCoin());


        BigDecimal beforeOtcSell = otcSellLegal.getLockedPrice();
        BigDecimal afterOtcSell = otcSellLegal.getLockedPrice().subtract(flowPrice);

        otcSellLegal.setLockedPrice(afterOtcSell).setUpdateUser(SYS_PLATFORM);

        if (!legalService.updateById(otcSellLegal))
            throw new UpdateDataException(100);

        //买方流水 可用+
        F.me().saveCashflow(buy.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.OTC_FINISH,
                flowPrice, coin, flowPrice, coin, BigDecimal.ZERO, coin,
                ProConst.ItemCode.USED, coin, null, null,
                beforeBuy, afterBuy, entity.getSellMid(), entity.getBuyMid());
        //卖方流水 冻结-
        F.me().saveCashflow(otcSell.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.OTC_FINISH,
                flowPrice, coin, flowPrice, coin, BigDecimal.ZERO, coin,
                ProConst.ItemCode.LOCKED, coin, null, null,
                beforeOtcSell, afterOtcSell, entity.getSellMid(), entity.getBuyMid());

        String op = "放币";

        //责任方（BUY-买方责任 SELL-卖方责任 NO-双方无责）
        String duty = "BUY".equals(entity.getDuty()) ? "买方" : "SELL".equals(entity.getDuty()) ? "卖方" : "双方无责";

        smsDuty(entity,op,duty,buy,otcSell);


        return ResponseData.success();
    }

    /**
     * 申诉判责 短信提示
     * @param entity 订单
     * @param op 操作（放币、取消）
     * @param duty 责任方
     * @param buyMember 买方
     * @param sellMember 卖方
     */
    private void smsDuty(Bill entity, String op, String duty,Member buyMember,Member sellMember)
    {

        //【ImBuy】订单号：%s，申诉已作%s处理，责任方为%s
        String smsDuty = F.me().cfg("SMS_DUTY");
        if (StrUtil.isNotBlank(smsDuty) && otcService.smsIsOpen())
        {
            String content = String.format(smsDuty, entity.getOrderNo(), op,duty);
           homeService.sendMsgWithPhoneOrEmail(buyMember,content);
           homeService.sendMsgWithPhoneOrEmail(sellMember,content);
        }

    }

    /**
     * 买方责任
     * 1买方，处罚如下：
     * a.限制买方自后台完成处理时间点开始24小时内无法继续购买；
     * 2挂单买方,处罚如下：
     * a.则扣除50%保证金，
     * b.关闭挂单买卖功能，补缴后再次开通，
     * c.且所有挂单购买订单剩余未交易部分全部自动撤销，
     * d.所有挂单出售订单全部撤销，解冻对应剩余数量资产
     * e.正在交易中订单正常进行。正在交易中订单再次违规，扣除剩余保证金50%，一直违规一直扣前一次剩余的50%(注：存在交易则无法退押金，对应押金退还业务）
     *
     * @param billId
     */
    public void dutyBuy(Long billId)
    {
        Bill entity = this.baseMapper.selectById(billId);

        if (entity.getDuty() != null)
            return;

        if (BUY.equals(entity.getType()))
        {
            redisUtil.set(String.format(Constant.LIMIT_DONT, entity.getBuyMid()), 1, Long.parseLong(F.me().cfg("LIMIT_DONT")));

        }
        if (SELL.equals(entity.getType()))
        {
            Member member = F.me().getMember(entity.getBuyMid());
            otcService.punish(member, entity);
        }

        // 责任方（BUY-买方责任 SELL-卖方责任 NO-双方无责）
        entity.setDuty(ProConst.DutyType.BUY.code)
                .setUpdateUser(SYS_PLATFORM);
        updateById(entity);

    }


    public void dutySell(Long billId)
    {
        Bill entity = this.baseMapper.selectById(billId);

        if (entity.getDuty() != null)
            return;

        if (SELL.equals(entity.getType()))
        {
            redisUtil.set(String.format(Constant.LIMIT_DONT, entity.getSellMid()), 1, Long.parseLong(F.me().cfg("LIMIT_DONT")));
        }
        if (BUY.equals(entity.getType()))
        {
            Member member = F.me().getMember(entity.getSellMid());
            otcService.punish(member, entity);
        }

        // 责任方（BUY-买方责任 SELL-卖方责任 NO-双方无责）
        entity.setDuty(ProConst.DutyType.SELL.code)
                .setUpdateUser(SYS_PLATFORM);
        updateById(entity);

    }

    public void dutyNull(Long billId)
    {
        Bill entity = this.baseMapper.selectById(billId);

        if (entity.getDuty() != null)
            return;
        // 责任方（BUY-买方责任 SELL-卖方责任 NO-双方无责）
        entity.setDuty(ProConst.DutyType.NO.code)
                .setUpdateUser(SYS_PLATFORM);
        updateById(entity);

    }
}