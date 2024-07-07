package cn.stylefeng.guns.modular.app.vo.e;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletBo
{
    private Long walletId;
    private BigDecimal total;
    private BigDecimal used;
    private String type;
    private String remark;
}
