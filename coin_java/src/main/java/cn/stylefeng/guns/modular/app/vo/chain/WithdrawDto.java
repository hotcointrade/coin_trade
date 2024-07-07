package cn.stylefeng.guns.modular.app.vo.chain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 钱包提现dto
 */
@Data
@Accessors(chain = true)
public class WithdrawDto
{
    private String businessId;

    private BigDecimal gases;

    private String fromAddress;

    private String toAddress;

    private String uid;

    private String coinWithdrawId;

    private String remark;

    private BigDecimal amount;

    private String coin;

    private String txHash;

    private String memberCode;

    private Byte success;
}
