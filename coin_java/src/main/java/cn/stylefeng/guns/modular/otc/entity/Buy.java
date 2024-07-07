package cn.stylefeng.guns.modular.otc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 购买挂单订单 实体类 </p>
  *
  * @author yaying.liu
  * @since 2020-07-13 14:52:41
 */

@Data
@TableName("otc_buy")
@Accessors(chain = true)
public class Buy extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 购买挂单订单
      */
     @TableId(value="buy_id", type= IdType.AUTO)
     private Long buyId;

     /**
      * 用户
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 账号
      */
     @TableField("account")
     private String account;

     /**
      * 昵称
      */
     @TableField("nick_name")
     private String nickName;

     /**
      * 订单号
      */
     @TableField("order_no")
     private String orderNo;

     /**
      * 币种
      */
     @TableField("coin")
     private String coin;

     /**
      * 单价
      */
     @TableField("unit_price")
     private java.math.BigDecimal unitPrice;

     /**
      * 数量
      */
     @TableField("number")
     private java.math.BigDecimal number;

     /**
      * 总额
      */
     @TableField("total_price")
     private java.math.BigDecimal totalPrice;

     /**
      * 最小购买数量
      */
     @TableField("low_number")
     private java.math.BigDecimal lowNumber;

     /**
      * 最小购买金额
      */
     @TableField("low_price")
     private java.math.BigDecimal lowPrice;

     /**
      * 付款方式
      */
     @TableField("pay_method")
     private String payMethod;

     /**
      * 剩余数量
      */
     @TableField("rest_number")
     private java.math.BigDecimal restNumber;

     /**
      * 完成数量
      */
     @TableField("finish_number")
     private java.math.BigDecimal finishNumber;

     /**
      * 版本号
      */
     @TableField("version")
     @Version
     private Long version;

     /**
      * 状态(是否仍在挂单) Y:是 N:否
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