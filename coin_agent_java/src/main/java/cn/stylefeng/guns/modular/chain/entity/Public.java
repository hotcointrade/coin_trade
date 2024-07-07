package cn.stylefeng.guns.modular.chain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 公账提币 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-01-17 18:55:07
 */

@Data
@TableName("chain_public")
@Accessors(chain = true)
public class Public extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * ID
      */
     @TableId(value="public_id", type= IdType.AUTO)
     private Long publicId;

     /**
      * 提币单号
      */
     @TableField("order_no")
     private String orderNo;

     /**
      * 提币地址
      */
     @TableField("to_address")
     private String toAddress;

     /**
      * 提币币种
      */
     @TableField("coin")
     private String coin;

     /**
      * 提币币种类型
      */
     @TableField("type")
     private String type;

     /**
      * 提币数量
      */
     @TableField("price")
     private java.math.BigDecimal price;

     /**
      * hash值
      */
     @TableField("tx_hash")
     private String txHash;

     /**
      * 状态(字典) 0：提币中 1：已完成 2:未完成
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

     @TableField(exist=false)
     private String code;


 }