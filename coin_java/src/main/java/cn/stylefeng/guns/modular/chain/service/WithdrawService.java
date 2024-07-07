package cn.stylefeng.guns.modular.chain.service;


import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.modular.app.service.BiPayService;
import cn.stylefeng.guns.modular.app.vo.chain.WithdrawDto;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.HttpUtils;
import cn.stylefeng.guns.modular.coin.service.SpotService;
import cn.stylefeng.guns.modular.fin.entity.Wallet;
import cn.stylefeng.guns.modular.fin.service.WalletService;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.stylefeng.guns.modular.chain.entity.Withdraw;
import cn.stylefeng.guns.modular.chain.mapper.WithdrawMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 提币信息Service
 *
 * @author yaying.liu
 * @Date 2019-12-10 20:05:21
 */
@Service
@Slf4j
public class WithdrawService extends ServiceImpl<WithdrawMapper, Withdraw>
{


    @Autowired
    WalletService walletService;

    @Value("${eth.key}")
    private String key;
    @Value("${eth.secret}")
    private String secret;
    @Value("${eth.memberCode}")
    private String memberCode;
    //创建钱包地址
    @Value("${eth.createUrl}")
    private String createUrl;
    //提幣地址
    @Value("${eth.mentionUrl}")
    private String mentionUrl;
    //地址校验
    @Value("${eth.checkAddressUrl}")
    private String checkAddressUrl;

    @Resource
    BiPayService biPayService;

    @Resource
    SpotService spotService;

    /**
     * 查询提币信息
     */
    public Page<Map<String, Object>> selectByCondition(Map condition)
    {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, condition);
    }
    public List<Withdraw> selectByMemberId(Long... memberIds ){

        return this.baseMapper.selectByMemberIds(memberIds);
    };

    /**
     * 删除提币信息
     */
    public void deleteWithdraw(Long withdrawId)
    {
        Withdraw entity = this.baseMapper.selectById(withdrawId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseData pass(Withdraw entity)
    {


//        Wallet wallet = PromotionFactory.me().getWallet(entity.getMemberId(), entity.getType());
        String coin=entity.getType();
        //是否为外盘代币
        boolean out = false;
//        if (StrUtil.equals(entity.getType(), "USDT")
//                || StrUtil.equals(entity.getType(), "ETH")
//                || StrUtil.equals(entity.getType(), "AT")
//                )
//        {
//            out = true;
//        }
        if (F.me().cfg(Constant.CHAIN_OPEN).equals("Y") && out) {
            /**
             * :外盘
             */
            if (entity.getType().equals("USDT"))
                coin="ETH_USDT";
            JSONObject jsonObject = HttpUtils.sendCoinWithdraw(memberCode, key, secret, entity.getOrderNo(),
                    coin,entity.getMemberId(), entity.getToAddress(), mentionUrl,
                    entity.getActualPrice());
            log.info("会员提币审核通过，请求中心化钱包提币接口，返回数据为"+jsonObject);
            if(jsonObject != null) {
                String s = jsonObject.getString("status");
                if ("200".equals(s)) {
                    WithdrawDto withdrawDto = JSONObject.toJavaObject(jsonObject.getJSONObject("data"),WithdrawDto.class);
                    if (withdrawDto != null){
                        entity.setTxHash(withdrawDto.getTxHash());
                        entity.setGases(withdrawDto.getGases());
                    }
                    entity.setStatus(ProConst.WithdrawStatusEnum.COIN.getCode());
                    entity.setUpdateTime(new Date());
                    entity.setUpdateUser(1l);
                    this.baseMapper.updateById(entity);
                    if ("USDT-ERC20".equals(entity.getType())
                            || "USDT-TRC20".equals(entity.getType())
                            ||  "USDT-OMNI".equals(entity.getType()) ){
                        entity.setType("USDT");
                    }
                    Wallet wallet = F.me().getWallet(entity.getMemberId(), entity.getType());
                    if(wallet==null){
                        log.info("钱包{}不存在",entity.getType());
                        throw new UpdateDataException(100);
                    }
                    BigDecimal before = wallet.getLockedPrice();
                    BigDecimal after = before.subtract(entity.getPrice());
                    wallet.setLockedPrice(after)
                            .setUpdateUser(-1L);
                    boolean row = walletService.updateById(wallet);
                    if (!row)
                        throw new UpdateDataException(100);
                    F.me().saveCashflow(wallet.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.WITHDRAW_OUT,
                            entity.getPrice(), entity.getType(), entity.getPrice(), entity.getType(), BigDecimal.ZERO, entity.getType(),
                            ProConst.ItemCode.LOCKED, entity.getType(), null, null,
                            before, after
                            , wallet.getMemberId(), Constant.SYS_PLATFORM);

                    return ResponseData.success();
                }else {
                    Map<String, Object> map = new HashedMap();
                    map.put("request", entity);
                    map.put("response", jsonObject.getString("message"));
                    JSONObject json = new JSONObject(map);
                    F.me().saveWalletException("/withdraw/pass", "提币有误", json);
                    throw new ServiceException(500,jsonObject.getString("message"));
                }
            }
        }else {
            /**
             * :线下提币通过
             */
            entity.setStatus(ProConst.WithdrawStatusEnum.PASS.getCode());
            this.updateById(entity);
            if ("USDT-ERC20".equals(entity.getType())
                    || "USDT-TRC20".equals(entity.getType())
                    ||  "USDT-OMNI".equals(entity.getType()) ){
                entity.setType("USDT");
            }
            Wallet wallet = F.me().getWallet(entity.getMemberId(), entity.getType());

            BigDecimal before = wallet.getLockedPrice();
            BigDecimal after = before.subtract(entity.getPrice());

            wallet.setLockedPrice(after)
                    .setUpdateUser(-1L);
            boolean row = walletService.updateById(wallet);
            if (!row)
                throw new UpdateDataException(100);
            F.me().saveCashflow(wallet.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.WITHDRAW_OUT,
                    entity.getPrice(), entity.getType(), entity.getPrice(), entity.getType(), BigDecimal.ZERO, entity.getType(),
                    ProConst.ItemCode.LOCKED, entity.getType(), null, "提币通过流水",
                    before, after
                    , wallet.getMemberId(), Constant.SYS_PLATFORM);

            walletService.updateById(wallet);

        }
        return ResponseData.success();
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseData fail(Withdraw entity)
    {

        entity.setStatus(ProConst.WithdrawStatusEnum.REJECT.getCode());

        this.updateById(entity);

        if ("USDT-ERC20".equals(entity.getType()) || "USDT-TRC20".equals(entity.getType()) || "USDT-OMNI".equals(entity.getType())){
            entity.setType("USDT");
        }
        Wallet wallet = F.me().getWallet(entity.getMemberId(), entity.getType());

        BigDecimal lockBefore = wallet.getLockedPrice();
        BigDecimal lockAfter = wallet.getLockedPrice().subtract(entity.getPrice());

        BigDecimal usedBefore = wallet.getUsedPrice();
        BigDecimal usedAfter = wallet.getUsedPrice().add(entity.getPrice());

        wallet.setLockedPrice(lockAfter)
                .setUsedPrice(usedAfter);

        boolean rows = walletService.updateById(wallet);
        if (!rows)
            throw new UpdateDataException(100);

        F.me().saveCashflow(wallet.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.WITHDRAW_REJECT,
                entity.getPrice(), entity.getType(), entity.getPrice(), entity.getType(), BigDecimal.ZERO, entity.getType(),
                ProConst.ItemCode.LOCKED, entity.getType(), null, null,
                lockBefore, lockAfter
                , wallet.getMemberId(), wallet.getMemberId());

        F.me().saveCashflow(wallet.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.WITHDRAW_REJECT,
                entity.getPrice(), entity.getType(), entity.getPrice(), entity.getType(), BigDecimal.ZERO, entity.getType(),
                ProConst.ItemCode.USED, entity.getType(), null, null,
                usedBefore, usedAfter
                , wallet.getMemberId(), wallet.getMemberId());


        return ResponseData.success();
    }


    /**
     * 添加提币信息
     */
    public void addWithdraw(Withdraw withdraw)
    {
        this.baseMapper.insert(withdraw);
    }
}
