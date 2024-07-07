package cn.stylefeng.guns.modular.e.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
  * <p> 撮合交易成交明细 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-05-20 10:29:28
 */

@Data
@TableName("e_match_detail")
@Accessors(chain = true)
public class MatchDetail extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 撮合交易
      */
     @TableId(value="match_detail_id", type= IdType.AUTO)
     private Long matchDetailId;

     /**
      * 买方id
      */
     @TableField("buy_id")
     private Long buyId;
     /**
      * 买方订单号
      */
     @TableField("buy_order")
     private String buyOrder;

     /**
      * 卖方id
      */
     @TableField("sell_id")
     private Long sellId;
     /**
      * 买方订单号
      */
     @TableField("sell_order")
     private String sellOrder;

     /**
      * 成交量 USDT
      */
     @TableField("number")
     private java.math.BigDecimal number;



     /**
      * 单价
      */
     @TableField("unit")
     private java.math.BigDecimal unit;

     /**
      * 交易对
      */
     @TableField("symbols")
     private String symbols;

     /**
      * 状态
      */
     @TableField("status")
     private String status;

     /**
      * 版本号
      */
     @TableField("version")
     private Long version;

     /**
      * 删除标志
      */
     @TableField("del")
     private String del;

     /**
      * 备注 转换币种 （BTC)量
      */
     @TableField("remark")
     private String remark;




 }