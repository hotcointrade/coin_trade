package cn.stylefeng.guns.modular.chain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
 * <p> 充币记录 实体类 </p>
 *
 * @author yaying.liu
 * @Date 2020-01-14 16:00:54
 */

@Data
@TableName("chain_recharge")
@Accessors(chain = true)
public class Recharge extends BaseEntity implements Serializable
{

    private static final long serialVersionUID = 1L;

    /**
     * 充值
     */
    @TableId(value = "recharge_id", type = IdType.AUTO)
    private Long rechargeId;

    @TableField("request_id")
    private  String requestId;

    /**
     * 单号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 用户ID
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 金额
     */
    @TableField("price")
    private java.math.BigDecimal price;

    /**
     * 实际金额
     */
    @TableField("actual_price")
    private java.math.BigDecimal actualPrice;

    /**
     * 矿工费
     */
    @TableField("gas")
    private java.math.BigDecimal gas;

    /**
     *
     */
    @TableField("recharge_address")
    private String rechargeAddress;

    /**
     *
     */
    @TableField("from_address")
    private String fromAddress;

    /**
     * 唯一标识
     */
    @TableField("tx_hash")
    private String txHash;

    /**
     * 币种
     */
    @TableField("type")
    private String type;

    /**
     * 状态(字典)
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


}