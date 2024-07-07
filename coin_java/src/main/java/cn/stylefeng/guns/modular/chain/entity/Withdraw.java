package cn.stylefeng.guns.modular.chain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
 * <p> 提币信息 实体类 </p>
 *
 * @author yaying.liu
 * @Date 2019-12-10 20:05:21
 */

@Data
@TableName("chain_withdraw")
@Accessors(chain = true)
public class Withdraw extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "withdraw_id", type = IdType.AUTO)
    private Long withdrawId;

    /**
     * 单号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 用户
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 提现数量
     */
    @TableField("price")
    private java.math.BigDecimal price;

    /**
     * 实际金额
     */
    @TableField("actual_price")
    private java.math.BigDecimal actualPrice;


    /**
     * 手续费
     */
    @TableField("fee")
    private java.math.BigDecimal fee;


    /**
     * 提币币种 0：USDT
     */
    @TableField("type")
    private String type;

    /**
     * 地址
     */
    @TableField("to_address")
    private String toAddress;

    /**
     * 哈希值
     */
    @TableField("tx_hash")
    private String txHash;

      /**
     * 返回信息
     */
    @TableField("response")
    private String response;



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

    /**
     * 矿工费
     */
    @TableField("gases")
    private BigDecimal gases;

}