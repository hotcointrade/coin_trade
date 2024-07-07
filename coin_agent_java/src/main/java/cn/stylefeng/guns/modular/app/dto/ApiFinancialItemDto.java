package cn.stylefeng.guns.modular.app.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class ApiFinancialItemDto
{
    //指定账户总额
    private BigDecimal totalPrice;
    //指定账户总额 -折合
    private BigDecimal convertPrice;
    //展示页面
    private String pageType;

    private List<ApiWalletDto> walletDtoList;

}
