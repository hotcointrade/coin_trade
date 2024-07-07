package cn.stylefeng.guns.modular.mining.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 矿机收益 实体类 </p>
  *
  * @author yaying.liu
  * @since 2022-06-07 13:36:19
 */

@Data
@TableName("mining_order_detail")
@Accessors(chain = true)
public class MiningOrderDetail extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 
      */
     @TableId(value="id", type= IdType.AUTO)
     private Long id;

     /**
      * 
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 
      */
     @TableField("mining_order_id")
     private Long miningOrderId;

     /**
      * 
      */
     @TableField("mining_unit")
     private String miningUnit;

     /**
      * 矿机当期产出
      */
     @TableField("output")
     private java.math.BigDecimal output;

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