package cn.stylefeng.guns.modular.waas.sinohope;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.stylefeng.guns.modular.app.controller.market.Kline;
import cn.stylefeng.guns.modular.app.entity.MemberRechargeAddress;
import cn.stylefeng.guns.modular.app.service.MemberRechargeAddressService;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.chain.entity.Recharge;
import cn.stylefeng.guns.modular.chain.service.RechargeService;
import cn.stylefeng.guns.modular.fin.entity.Contract;
import cn.stylefeng.guns.modular.fin.service.ContractService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sinohope.request.WaasNotifyReqParam;
import com.sinohope.response.common.ResultCode;
import com.sinohope.response.common.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

import static cn.stylefeng.guns.modular.base.state.Constant.KINE;
import static cn.stylefeng.guns.modular.base.state.Constant._NEW;


@CrossOrigin
@RequestMapping("/sinohope")
@RestController
@Slf4j
public class SinohopeCallBack {
    @Resource
    private MemberRechargeAddressService memberRechargeAddressService;

    @Resource
    private RechargeService rechargeService;

    @Resource
    private ContractService contractService;

    @Resource
    RedisUtil redisUtil;

    @RequestMapping("/callback/notify")
    public ResultData callback(@RequestBody WaasNotifyReqParam waasNotifyReqParam) {
        log.info("执行充值转账回调函数：" + waasNotifyReqParam);
        // 防止同一次充值，回调被多次调用
        List<Recharge> recharges = rechargeService.list(Wrappers.<Recharge>lambdaQuery().eq(Recharge::getRequestId, waasNotifyReqParam.getRequestId()));
        // state == 10
        JSONObject obj = JSONUtil.parseObj(waasNotifyReqParam.getRequestDetail());
        if (recharges.size() == 0 && obj.getInt("state") == 10) {
            String walletId = obj.getStr("walletId");
            MemberRechargeAddress memberRechargeAddress = memberRechargeAddressService.getOne(Wrappers.<MemberRechargeAddress>lambdaQuery().eq(MemberRechargeAddress::getWalletId, walletId).last("limit 1"));
            // 充值记录
            Recharge entity = new Recharge();
            entity.setRequestId(waasNotifyReqParam.getRequestId());
            String assetId = obj.getStr("assetId");
            // 转账金额（未计算）
            BigDecimal amountData = obj.getBigDecimal("amount");
            // 币种位数
            BigDecimal feeAssetDecimal = new BigDecimal("1e" + obj.getStr("feeAssetDecimal"));
            if ("ETH_SEPOLIA".equals(assetId) || "ETH_ETH".equals(assetId)) {
                entity.setType("USDT-ERC20");
                // 将eth金额计算成usdt
                Kline map = redisUtil.getBean(KINE + "ETH/USDT" + _NEW, Kline.class);
                BigDecimal amount = NumberUtil.mul(NumberUtil.div(amountData, feeAssetDecimal), map.getClose());
                entity.setPrice(amount);
                entity.setActualPrice(amount);
            }
            if ("USDT_SEPOLIA".equals(assetId) || "USDT_ETH".equals(assetId) || "USDC_ETH".equals(assetId)) {
                entity.setType("USDT-ERC20");
                BigDecimal amount = NumberUtil.div(amountData, feeAssetDecimal);
                entity.setPrice(amount);
                entity.setActualPrice(amount);
            }
            if ("TRX_TRON".equals(assetId)) {
                entity.setType("USDT-TRC20");
                Kline map = redisUtil.getBean(KINE + "TRX/USDT" + _NEW, Kline.class);
                BigDecimal amount = NumberUtil.mul(NumberUtil.div(amountData, feeAssetDecimal), map.getClose());
                entity.setPrice(amount);
                entity.setActualPrice(amount);
            }
            if ("USDT_TRON".equals(assetId) || "USDC_TRON".equals(assetId)) {
                entity.setType("USDT-TRC20");
                BigDecimal amount = NumberUtil.div(amountData, feeAssetDecimal);
                entity.setPrice(amount);
                entity.setActualPrice(amount);
            }
            entity.setOrderNo(cn.stylefeng.guns.modular.base.util.RandomUtil.code("CR"))
                    .setMemberId(memberRechargeAddress.getMemberId())
                    .setGas(new BigDecimal(obj.getStr("gasPrice")))
                    .setRechargeAddress(obj.getStr("to"))
                    .setFromAddress(obj.getStr("from"))
                    .setTxHash(obj.getStr("txHash"))
                    .setStatus(ProConst.Status.PASS.code)
                    .setDel("N")
                    .setRemark("")
                    .setCreateUser(memberRechargeAddress.getMemberId());
            rechargeService.save(entity);
            // 充值成功保存流水记录
            Contract contract = F.me().getContract(memberRechargeAddress.getMemberId(), "USDT");
            F.me().saveCashflow(contract.getMemberId(), ProConst.WalletBigType.CONTRACT, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.RECHARGE,
                    entity.getActualPrice(), entity.getType(), entity.getActualPrice(), entity.getType(), BigDecimal.ZERO, entity.getType(),
                    ProConst.ItemCode.USED, entity.getType(), null, null,
                    contract.getUsedPrice(), contract.getUsedPrice().add(entity.getActualPrice()), contract.getMemberId(), contract.getMemberId());
            // 更新合约钱包余额
            contract.setUsedPrice(contract.getUsedPrice().add(entity.getActualPrice()));
            contractService.updateById(contract);
        }
        return new ResultData(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), (Object) null, waasNotifyReqParam.getRequestId());
    }
}
