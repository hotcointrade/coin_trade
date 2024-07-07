package cn.stylefeng.guns.modular.app.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 流水记录dto
 */
@Data
@Accessors(chain = true)
public class ApiCashflowDto {

    /**
     * 名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 时间戳
     */
    private long createTimeStamp;

    /**
     * 符号
     */
    private String symbol;

    /**
     * 币种单位
     */
    private String flowCoin;

    /**
     * 金额
     */
    private BigDecimal flowPrice;


    /**
     * 页面类型
     */
    private String pageType;

    //流水类型
    private String flowType;

    //流水编码
    private String fType;

    //备注
    private String remark;
}
