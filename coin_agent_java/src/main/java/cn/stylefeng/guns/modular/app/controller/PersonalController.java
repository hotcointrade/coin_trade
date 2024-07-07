package cn.stylefeng.guns.modular.app.controller;


import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.modular.app.service.PersonalService;
import cn.stylefeng.guns.modular.app.vo.chain.CoinPublicBaseTransaction;
import cn.stylefeng.guns.modular.app.vo.chain.DepositBaseDto;
import cn.stylefeng.guns.modular.app.vo.chain.WalletResult;
import cn.stylefeng.guns.modular.app.vo.chain.WithdrawBaseDto;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.RandomUtil;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.chain.entity.Recharge;
import cn.stylefeng.guns.modular.chain.entity.Withdraw;
import cn.stylefeng.guns.modular.chain.service.PublicService;
import cn.stylefeng.guns.modular.chain.service.RechargeService;
import cn.stylefeng.guns.modular.chain.service.WithdrawService;
import cn.stylefeng.guns.modular.e.entity.Bought;
import cn.stylefeng.guns.modular.e.service.BoughtService;
import cn.stylefeng.guns.modular.extension.ida.IdaBean;
import cn.stylefeng.guns.modular.fin.entity.Legal;
import cn.stylefeng.guns.modular.fin.entity.Wallet;
import cn.stylefeng.guns.modular.fin.service.LegalService;
import cn.stylefeng.guns.modular.fin.service.WalletService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import cn.stylefeng.guns.modular.chain.entity.Public;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 外部回调统一Api
 * <p>
 * 注：当前token未拦截
 */
@RestController
@RequestMapping("/api/personal")
public class PersonalController extends Constant
{

    private Logger log = LoggerFactory.getLogger(PersonalController.class);

    @Autowired
    PersonalService personalService;

    @Autowired
    WalletService walletService;

    @Autowired
    RechargeService rechargeService;

    @Autowired
    WithdrawService withdrawService;

    @Autowired
    PublicService publicService;


    @Autowired
    RedisUtil redisUtil;

    @Autowired
    BoughtService boughtService;

    @Autowired
    LegalService legalService;

    /**
     * 充值回调
     *
     * @param deposits
     * @return
     */
    @RequestMapping(value = "/rechargeCallback")
    public WalletResult rechargeCallback(@RequestBody List<DepositBaseDto> deposits) throws Exception
    {

        WalletResult wallerResult = new WalletResult();
        List<String> txHashs = new ArrayList<>();
        if (deposits.size() > 0) {
            // 遍历充值回调的参数
            for (DepositBaseDto depositBaseDto : deposits) {
                Recharge chainRechargeQ = new Recharge();
                if (depositBaseDto != null) {
                    chainRechargeQ.setTxHash(depositBaseDto.getTxHash()).setDel("N");
                    Recharge chainRechargeR =
                            rechargeService.getOne(new QueryWrapper<>(chainRechargeQ));
                    if (chainRechargeR == null) {
                        String orderNo = RandomUtil.code("CR");
                        Recharge entity = new Recharge();
                        //充币数量
                        BigDecimal price = new BigDecimal(depositBaseDto.getAmount());
                        entity.setOrderNo(orderNo)
                                .setMemberId(Long.valueOf(depositBaseDto.getUserId()))
                                .setPrice(price)
                                .setRechargeAddress(depositBaseDto.getToAddress())
                                .setFromAddress(depositBaseDto.getFromAddress())
                                .setActualPrice(price)
                                .setTxHash(depositBaseDto.getTxHash())
                                .setType(depositBaseDto.getCoin())
                                .setRemark("1")
                                .setStatus("Y")
                                .setGas(new BigDecimal(depositBaseDto.getGases()))
                                .setCreateUser(Long.valueOf(depositBaseDto.getUserId()));
                        entity.setCreateTime(new Date());
                        rechargeService.save(entity);

                        BigDecimal actualPrice = price;
                        Wallet wallet = null;
                        String coin = depositBaseDto.getCoin();
                        switch (depositBaseDto.getCoin()) {
                            case "ETH":
                                /**
                                 * 用户钱包
                                 */
                                wallet = F.me().getWallet(Long.valueOf(depositBaseDto.getUserId()), "ETH");
                                //充币流水记录
                                //流水记录
                                F.me().saveCashflow(Long.valueOf(depositBaseDto.getUserId()), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.RECHARGE
                                        , price, coin, actualPrice, coin, new BigDecimal(depositBaseDto.getGases()), coin
                                        , ItemCode.USED, coin, null, null,
                                        wallet.getUsedPrice(), wallet.getUsedPrice().add(actualPrice),
                                        SYS_PLATFORM, Long.valueOf(depositBaseDto.getUserId())
                                );

                                break;
                            case "ETH_USDT":
                                wallet = F.me().getWallet(Long.valueOf(depositBaseDto.getUserId()), "USDT");
                                //流水记录
                                F.me().saveCashflow(Long.valueOf(depositBaseDto.getUserId()), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.RECHARGE
                                        , actualPrice, coin, actualPrice, coin, new BigDecimal(depositBaseDto.getGases()), coin
                                        , ItemCode.USED, coin, null, null,
                                        wallet.getUsedPrice(), wallet.getUsedPrice().add(actualPrice),
                                        SYS_PLATFORM, Long.valueOf(depositBaseDto.getUserId())
                                );
                                break;
                            case "AT":
                                wallet = F.me().getWallet(Long.valueOf(depositBaseDto.getUserId()), "AT");
                                //流水记录
                                F.me().saveCashflow(Long.valueOf(depositBaseDto.getUserId()), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.RECHARGE
                                        , actualPrice, coin, actualPrice, coin, new BigDecimal(depositBaseDto.getGases()), coin
                                        , ItemCode.USED, coin, null, null,
                                        wallet.getUsedPrice(), wallet.getUsedPrice().add(actualPrice),
                                        SYS_PLATFORM, Long.valueOf(depositBaseDto.getUserId())
                                );
                                break;
                        }

                        /**
                         * 给用户账户加钱
                         */
                        wallet.setUsedPrice(wallet.getUsedPrice().add(actualPrice))
                                .setUpdateUser(wallet.getMemberId());
                        walletService.updateById(wallet);
                    }
                }
                // 用于返回txhash
                txHashs.add(depositBaseDto.getTxHash());
            }
        }
        wallerResult.setData(txHashs)
                .setMessage("")
                .setStatus("200")
                .setInnerException("");
        return wallerResult;
    }

    /**
     * 提币回调
     *
     * @param withdraws
     * @return
     */
    @RequestMapping(value = "/withdrawCallback")
    public WalletResult withdrawCallback(@RequestBody List<WithdrawBaseDto> withdraws)
    {

        WalletResult walletResult = new WalletResult();
        List<String> txHashs = new ArrayList<>();
        if (withdraws.size() > 0)
        {
            // 遍历提币回调的参数
            for (WithdrawBaseDto withdrawBaseDto : withdraws)
            {
                if (withdrawBaseDto != null && withdrawBaseDto.getSuccess().equals(new Byte("1")))
                {
                    Withdraw chainWithdrawQ = new Withdraw();
                    chainWithdrawQ.setOrderNo(withdrawBaseDto.getBusinessId());
                    Withdraw chainWithdrawR =
                            withdrawService.getOne(new QueryWrapper<>(chainWithdrawQ));
                    // 判断是否已存在txHash
                    if (chainWithdrawR != null && StringUtils.isBlank(chainWithdrawR.getTxHash()))
                    {
                        chainWithdrawR.setTxHash(withdrawBaseDto.getTxHash())
                                .setStatus(ProConst.WithdrawStatusEnum.PASS.getCode())
                                .setRemark(withdrawBaseDto.getGases()) //暂存旷工费
                                .setUpdateUser(SYS_PLATFORM);
                        withdrawService.updateById(chainWithdrawR);
                    }
                }
                // 用于返回txhash
                txHashs.add(withdrawBaseDto.getTxHash());
            }
            walletResult.setData(txHashs)
                    .setMessage("")
                    .setStatus("200")
                    .setInnerException("");
            return walletResult;
        }
        return walletResult;
    }

    /**
     * 公账提币回调
     */
    @RequestMapping(value = "/publicCallback")
    public WalletResult publicCallback(@RequestBody List<CoinPublicBaseTransaction> coinPublicBaseTransactions)
    {
        WalletResult wallerResult = new WalletResult();
        List<String> txHashs = new ArrayList<>();
        if (coinPublicBaseTransactions != null && coinPublicBaseTransactions.size() > 0)
        {
            for (CoinPublicBaseTransaction coinPublicBaseTransaction : coinPublicBaseTransactions)
            {
                //判断返回值和txHash是否为空
                if (coinPublicBaseTransaction != null && StringUtils.isNotBlank(coinPublicBaseTransaction.getTxHash()))
                {
                    Public chainPublicQ = new Public();
                    chainPublicQ.setTxHash(coinPublicBaseTransaction.getTxHash());
                    Public chainPublicR = publicService.getOne(new QueryWrapper<>(chainPublicQ));
                    if (chainPublicR != null && StrUtil.equals(ProConst.WithdrawStatusEnum.COIN.getCode(), chainPublicR.getStatus()))
                    {
                        //更新公账提币状态
                        chainPublicR.setStatus(ProConst.WithdrawStatusEnum.PASS.getCode())
                                .setRemark(coinPublicBaseTransaction.getGases())
                                .setUpdateUser(SYS_PLATFORM);
                        publicService.updateById(chainPublicR);
                    }
                }
                txHashs.add(coinPublicBaseTransaction.getTxHash());
            }
        }
        wallerResult.setData(txHashs)
                .setStatus("200")
                .setMessage("")
                .setInnerException("");
        return wallerResult;
    }




}
