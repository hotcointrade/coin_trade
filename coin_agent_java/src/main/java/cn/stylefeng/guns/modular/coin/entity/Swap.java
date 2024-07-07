package cn.stylefeng.guns.modular.coin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 合约交易对 实体类 </p>
  *
  * @author yaying.liu
  * @since 2020-08-25 10:49:35
 */

@Data
@TableName("coin_swap")
@Accessors(chain = true)
public class Swap extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 合约交易对
      */
     @TableId(value="swap_id", type= IdType.AUTO)
     private Long swapId;

     /**
      * 交易对
      */
     @TableField("symbol")
     private String symbol;
     private String img;

     /**
      * 每手价值数量
      */
     @TableField("hand_number")
     private Double handNumber;

     /**
      * 建仓手续费
      */
     @TableField("open_fee_price")
     private BigDecimal openFeePrice;

     /**
      *平仓手续费
      */
     @TableField("close_fee_price")
     private BigDecimal closeFeePrice;
     /**
      * 保留小数位
      */
     @TableField("number")
     private Integer number;

     /**
      * 币种类型（0-平台币  1-火币)
      */
     @TableField("type")
     private String type;

     /**
      * 点差
      */
     @TableField("spread")
     private java.math.BigDecimal spread;

     /**
      * 版本号
      */
     @TableField("version")
     @Version
     private Long version;

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
