package cn.stylefeng.guns.modular.app.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class ApiFinancialDto
{
    //总资产折合
    private BigDecimal totalPrice;

    private List<ApiFinancialItemDto> apiFinancialItemDto;

}
