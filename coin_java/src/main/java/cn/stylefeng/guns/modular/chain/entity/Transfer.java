package cn.stylefeng.guns.modular.chain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 转账信息 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-01-14 17:08:46
 */

@Data
@TableName("chain_transfer")
@Accessors(chain = true)
public class Transfer extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * ID
      */
     @TableId(value="transfer_id", type= IdType.AUTO)
     private Long transferId;

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
      * 转账数量
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
      * 转账类型
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




 }