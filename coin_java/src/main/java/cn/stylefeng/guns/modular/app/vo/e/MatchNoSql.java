package cn.stylefeng.guns.modular.app.vo.e;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class MatchNoSql
{
    //去向用户id
    private Long toId;
    //来源用户id
    private Long fromId;
    //备注
    private String remark;
    //流水币种
    private String coin;
    //订单价格
    private BigDecimal price;
    private Date  createTime;
    //订单号
    private String orderNo;
    //订单类型（买、卖）
    private String matchType;

}
