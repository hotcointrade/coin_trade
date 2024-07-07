package cn.stylefeng.guns.modular.chain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 豆兑换信息 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-01-17 14:46:22
 */

@Data
@TableName("chain_convert")
@Accessors(chain = true)
public class Convert extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * ID
      */
     @TableId(value="convert_id", type= IdType.AUTO)
     private Long convertId;

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
      * 兑换数量
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
      * 币种
      */
     @TableField("type")
     private String type;

     /**
      * 到账数量
      */
     @TableField("to_price")
     private java.math.BigDecimal toPrice;

     /**
      * 到账币种
      */
     @TableField("to_coin")
     private String toCoin;

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