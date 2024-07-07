package cn.stylefeng.guns.modular.app.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ApiRecordDto<T> {
    /**
     * 总额
     */
    private BigDecimal totalPrice;
    /**
     * 累计
     */
    private BigDecimal countPrice;
    /**
     * 可用
     */
    private BigDecimal usedPrice;
    /**
     * 页面类型
     */
    private String pageType;
    /**
     * 数据
     */
    private T records;

}
