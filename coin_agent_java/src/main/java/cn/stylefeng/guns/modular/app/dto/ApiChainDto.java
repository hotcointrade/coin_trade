package cn.stylefeng.guns.modular.app.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class ApiChainDto {
    /**
     * 充值
     */
    private Long id;

    /**
     * 金额
     */
    private java.math.BigDecimal price;

    /**
     * 实际金额
     */
    private java.math.BigDecimal actualPrice;

    /**
     * 区块链id
     */
    private String txHash;

    /**
     * 地址
     */
    private String address;

    /**
     * 手续费
     */
    private BigDecimal fee;
    /**
     * 状态(字典)
     */
    private String status;

    private String type;

    private String remark;

    private Date time;

    private long timeStamp;

    /**
     * 币种
     */
    private String coin;
}
