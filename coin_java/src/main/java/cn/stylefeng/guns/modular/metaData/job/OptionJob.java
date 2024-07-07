package cn.stylefeng.guns.modular.metaData.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.stylefeng.guns.modular.app.controller.market.Kline;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.HomeService;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.coin.constant.ContractOptionStatus;
import cn.stylefeng.guns.modular.coin.entity.ContractOption;
import cn.stylefeng.guns.modular.coin.entity.ContractOptionCoin;
import cn.stylefeng.guns.modular.coin.entity.ContractOptionOrder;
import cn.stylefeng.guns.modular.coin.service.ContractOptionCoinService;
import cn.stylefeng.guns.modular.coin.service.ContractOptionOrderService;
import cn.stylefeng.guns.modular.coin.service.ContractOptionService;
import cn.stylefeng.guns.modular.fin.entity.FinOption;
import cn.stylefeng.guns.modular.fin.service.FinOptionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
@Logger
@Slf4j
public class OptionJob {

    @Autowired
    private ContractOptionService contractOptionService;

    @Autowired
    private ContractOptionCoinService contractOptionCoinService;

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private ContractOptionOrderService optionOrderService;

    @Autowired
    private FinOptionService finOptionService;

    @Autowired
    private HomeService homeService;


//    @Scheduled(cron = "0/10 * * * * ?")//10s一次
//	@Scheduled(fixedRate = 100000)
    public void createOptionContract() {
        //结束这一期
        LambdaQueryWrapper<ContractOption> endQuery = new LambdaQueryWrapper<>();
        endQuery.eq(ContractOption::getStatus, ContractOptionStatus.STARTING.getCode());
        List<ContractOption> contractOptions = contractOptionService.getBaseMapper().selectList(endQuery);

        for (ContractOption contractOption : contractOptions) {
            //判断是否到时间
            if (new Date().getTime() >= contractOption.getCloseTime()) {
                if (contractOption.getTotalBuy().add(contractOption.getTotalSell()).compareTo(BigDecimal.ZERO) == 0) {
                    // 无交易额，直接删除
                    contractOptionService.removeById(contractOption.getId());
                } else {
                    log.info("====币种：{} ,第{}期清算开始====", contractOption.getSymbol(), contractOption.getOptionNo());
                    String key = Constant.KINE + contractOption.getSymbol() + Constant._NEW; //
                    Kline kline = redisUtil.getBean(key, Kline.class);
                    contractOption.setClosePrice(BigDecimal.valueOf(kline.getClose()));
                    contractOption.setStatus(ContractOptionStatus.CLOSED.getCode());
                    BigDecimal openPrice = contractOption.getOpenPrice().setScale(4, RoundingMode.DOWN);
                    BigDecimal closePrice = contractOption.getClosePrice();
                    if (openPrice.compareTo(closePrice) == 0) {
                        contractOption.setResult(3); // 平
                    } else if (openPrice.compareTo(closePrice) > 0) {
                        contractOption.setResult(2); // 跌
                    } else {
                        contractOption.setResult(1); // 涨
                    }
                    contractOptionService.updateById(contractOption);

                    this.optionOrderSettlement(contractOption.getOptionNo(), contractOption.getSymbol(), contractOption.getResult());
                    log.info("===清算结束===");
                }
            }
        }

        //开始新一期
        LambdaQueryWrapper<ContractOptionCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractOptionCoin::getVisible, 1);
        List<ContractOptionCoin> contractOptionCoins = contractOptionCoinService
                .getBaseMapper().selectList(queryWrapper);
        List<ContractOption> collect = new ArrayList<>();
        for (ContractOptionCoin e : contractOptionCoins) {
            LambdaQueryWrapper<ContractOption> eq = new LambdaQueryWrapper<ContractOption>()
                    .eq(ContractOption::getSymbol, e.getSymbol())
                    .in(ContractOption::getStatus,
                            Arrays.asList(ContractOptionStatus.STARTING.getCode()));
            Integer count = contractOptionService.getBaseMapper()
                    .selectCount(eq);
            //判断上一期有没有结束
            if (count == 0) {
                log.info("币种：{}，创建新的一期", e.getSymbol());
                // 最新价格
                Date date = new Date();
                String key = Constant.KINE + e.getSymbol() + Constant._NEW; //
                Kline kline = redisUtil.getBean(key, Kline.class);
                ContractOption contractOption = new ContractOption();
                contractOption.setOptionNo((int) (date.getTime() / 1000));
                contractOption.setSymbol(e.getSymbol());
                contractOption.setOpenPrice(kline == null ? BigDecimal.ZERO : BigDecimal.valueOf(kline.getClose()));
                contractOption.setOpenTime(date.getTime());
                contractOption.setStatus(ContractOptionStatus.STARTING.getCode());    // 投注中
                contractOption.setResult(0); // 待开奖
                // 关闭时间
                DateTime dateTime = DateUtil.offsetSecond(date, e.getOpenTimeGap());
                contractOption.setCloseTime(dateTime.getTime());
                contractOption.setCreateTime(new Date());
                collect.add(contractOption);
                log.info("===新的一期创建完毕===");
            }
        }
        if (CollectionUtil.isNotEmpty(collect)) {
            contractOptionService.saveBatch(collect);
        }

    }

    /**
     * 订单结算
     */
    private void optionOrderSettlement(Integer optionNo, String symbol, Integer result) {
        List<ContractOptionOrder> contractOptionOrders = optionOrderService.list(Wrappers.<ContractOptionOrder>lambdaQuery()
                .eq(ContractOptionOrder::getStatus, 0)
                .eq(ContractOptionOrder::getOptionNo, optionNo));

        contractOptionOrders.forEach(optionOrder -> {
            log.info("结算：{}", symbol);
            FinOption finOption = F.me().getFinOption(optionOrder.getMemberId(), optionOrder.getBaseSymbol());
            if (finOption == null) {
                Member member = F.me().getMember(optionOrder.getMemberId());
                homeService.genOption(member);
            }
            ContractOptionCoin coin = contractOptionCoinService.getOne(Wrappers.<ContractOptionCoin>lambdaQuery().eq(ContractOptionCoin::getSymbol, symbol));
            if (coin == null) return;

            //买的方向和本期结果方向相同为胜(true)，反之为负(false)，若平局则为null
            Boolean ctrlFlag = result == 3 ? null : Objects.equals(result, optionOrder.getDirection() + 1);
            boolean flag = homeService.jiesuan(optionOrder, finOption, coin, ctrlFlag);
            if (flag) {
                //Y 需要返佣
                homeService.optionRecord(coin, optionOrder);
            }
            optionOrderService.updateById(optionOrder);
            finOptionService.updateWallet(finOption);
            contractOptionCoinService.updateById(coin);
        });


    }

    /**
     * 另一个版本结算
     */
    // @Scheduled(cron = "0/5 * * * * ?")//5s一次
    public void SettlementContractOption() {
        List<ContractOption> contractOptions = contractOptionService.getBaseMapper()
                .selectList(new LambdaQueryWrapper<ContractOption>().eq(ContractOption::getStatus, ContractOptionStatus.OPENING.getCode()));
        for (ContractOption contractOption : contractOptions) {
            //判断是否到时间
            if (new Date().getTime() > contractOption.getCloseTime()) {
                System.out.println(new Date().getTime() + "," + contractOption.getCloseTime());
                log.info("====币种：{} ,第{}期清算开始====", contractOption.getSymbol(), contractOption.getOptionNo());
                String key = Constant.KINE + contractOption.getSymbol() + Constant._NEW; //
                Kline kline = redisUtil.getBean(key, Kline.class);
                contractOption.setClosePrice(BigDecimal.valueOf(kline.getClose()));
                contractOption.setStatus(ContractOptionStatus.CLOSED.getCode());
                BigDecimal openPrice = contractOption.getOpenPrice().setScale(4, RoundingMode.DOWN);
                BigDecimal closePrice = contractOption.getClosePrice();
                if (openPrice.compareTo(closePrice) == 0) {
                    contractOption.setResult(3); // 平
                } else if (openPrice.compareTo(closePrice) > 0) {
                    contractOption.setResult(2); // 跌
                } else {
                    contractOption.setResult(1); // 涨
                }
                //更新每一期合约
                contractOptionService.updateById(contractOption);
                List<ContractOptionOrder> contractOptionOrders = optionOrderService.list(Wrappers.<ContractOptionOrder>lambdaQuery()
                        .eq(ContractOptionOrder::getStatus, 0)
                        .eq(ContractOptionOrder::getOptionNo, contractOption.getOptionNo()));
                contractOptionOrders.forEach(contractOptionOrder -> {
                    //判断合约钱包是否存在
                    FinOption finOption = F.me().getFinOption(contractOptionOrder.getMemberId(), contractOptionOrder.getBaseSymbol());
                    // 不存在则创建一个
                    if (finOption == null) {
                        Member member = F.me().getMember(contractOptionOrder.getMemberId());
                        homeService.genOption(member);
                    }
                    ContractOptionCoin coin = contractOptionCoinService.getOne(Wrappers.<ContractOptionCoin>lambdaQuery().eq(ContractOptionCoin::getSymbol, contractOptionOrder.getSymbol()));
                    if (coin == null) return;
                    Integer result = contractOption.getResult();
                    Boolean ctrlFlag = result == 3 ? null : Objects.equals(result, contractOptionOrder.getDirection() + 1);
                    boolean flag = homeService.optionsContractSettlement(contractOptionOrder, finOption, coin, ctrlFlag);
                    if (flag) {
                        //Y 需要返佣
                        homeService.optionRecord(coin, contractOptionOrder);
                    }
                    //更新合约订单
                    optionOrderService.updateById(contractOptionOrder);
                    //更新期权钱包
                    finOptionService.updateWallet(finOption);
                    //更新期权交易对
                    contractOptionCoinService.updateById(coin);
                });

            }
        }
    }
}
