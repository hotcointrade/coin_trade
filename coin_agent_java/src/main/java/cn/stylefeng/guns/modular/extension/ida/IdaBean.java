package cn.stylefeng.guns.modular.extension.ida;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IdaBean
{
    /**
     * 	签名方式：MD5，目前只支持MD5
     */
    private String signType;
    /**
     * 调用方订单号
     */
    private String orderNo;
    /**
     *订单金额
     */
    private String orderAmount;
    /**
     *订单币种：CNY
     */
    private String orderCurrency;
    /**
     *我方订单号
     */
    private String transactionId;
    /**
     *处理状态：success
     */
    private String status;
    /**
     *签名,规则见下面说明
     */
    private String sign;
    /**
     *	到账的币币数
     */
    private String amount;
    /**
     * 扣除手续费到账的币币数
     */
    private String realAmount;

}
