package cn.stylefeng.guns.modular.e.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
 * <p> 委托单信息 实体类 </p>
 *
 * @author yaying.liu
 * @Date 2020-03-18 10:38:28
 */

@Data
@TableName("e_match")
@Accessors(chain = true)
public class Match extends BaseEntity implements Serializable
{

    private static final long serialVersionUID = 1L;

    /**
     * 撮合交易
     */
    @TableId(value = "match_id", type = IdType.AUTO)
    private Long matchId;

    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 用户
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 订单类型（买、卖）
     */
    @TableField("match_type")
    private String matchType;

    /**
     * 交易方式
     */
    @TableField("trade")
    private String trade;

    /**
     * 交易数量
     */
    @TableField("number")
    private java.math.BigDecimal number;

    /**
     * 单价
     */
    @TableField("unit")
    private java.math.BigDecimal unit;

    /**
     * 总价
     */
    @TableField("total_price")
    private java.math.BigDecimal totalPrice;

    /**
     * 成交方式
     */
    @TableField("deal_way")
    private String dealWay;

    /**
     * 已成交总价
     */
    @TableField("finish_number")
    private java.math.BigDecimal finishNumber;

    /**
     * 未完成数量
     */
    @TableField("un_finish_numebr")
    private java.math.BigDecimal unFinishNumebr;

    /**
     * 委托量(BTC)
     */
    @TableField("will")
    private java.math.BigDecimal will;

    /**
     * 实际吃单量（去掉手续费部分）(BTC)
     */
    @TableField("actual")
    private java.math.BigDecimal actual;

    /**
     * 手续费量(BTC)
     */
    @TableField("fee_number")
    private java.math.BigDecimal feeNumber;


    /**
     * 成交量(BTC)
     */
    @TableField("ok")
    private java.math.BigDecimal ok;


    /**
     * 剩余冻结
     */
    @TableField("rest_frozen")
    private java.math.BigDecimal restFrozen;

    /**
     * 手续费 (USDT)
     */
    @TableField("fee")
    private java.math.BigDecimal fee;

    /**
     * 撮合到账数量
     */
    @TableField("convert_number")
    private java.math.BigDecimal convertNumber;

    /**
     * 手续费比例
     */
    @TableField("rate")
    private String rate;

    /**
     * 优先级
     */
    @TableField("priority")
    private Long priority;

    /**
     * 交易对
     */
    @TableField("symbols")
    private String symbols;

    /**
     * 状态（未支付，已支付，部分撮合，全部撮合，已经撤销，完成结算，撤销结算）
     */
    @TableField("status")
    private String status;

    /**
     * 删除标志
     */
    @TableField("del")
    private String del;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    @TableField("version")
    @Version
    private Long version;

}