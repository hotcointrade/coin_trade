package cn.stylefeng.guns.modular.app.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 提现详情dto
 */
@Data
@Accessors(chain = true)
public class ApiWithdrawCoinDto {
    /**
     * 最小提幣数量
     */
    private String min;

    /**
     * 可用USDT
     *
     */
    private BigDecimal usdtPrice;

    /**
     * 手续费
     */
    private String fee;

    /**
     *USDT:DFS
     */
    private String usdtCdfs;

}
