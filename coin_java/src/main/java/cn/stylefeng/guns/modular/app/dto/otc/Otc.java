package cn.stylefeng.guns.modular.app.dto.otc;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 挂单列表
 */
@Data
public class Otc
{
    //id
    private Long id;

    //昵称
    private String nickName;

    //单价
    private BigDecimal unitPrice;

    //数量
    private BigDecimal restNumber;

    //限额（最小）
    private BigDecimal limitLow;
    //限额（最大）
    private BigDecimal limitHigh;
    //支付方式
    private String payMethod;


}
