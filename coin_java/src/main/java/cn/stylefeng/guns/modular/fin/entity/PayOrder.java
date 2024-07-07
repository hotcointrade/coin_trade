package cn.stylefeng.guns.modular.fin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
  * <p> 支付订单 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-12-23 14:14:16
 */

@Data
@TableName("fin_pay_order")
@Accessors(chain = true)
@ToString
public class PayOrder extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 
      */
     @TableId(value="pay_order_id", type= IdType.AUTO)
     private Long payOrderId;

     /**
      * 用户id
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 订单号
      */
     @TableField("order_no")
     private String orderNo;

     /**
      * 金额
      */
     @TableField("total_price")
     private java.math.BigDecimal totalPrice;

     /**
      * 单价
      */
     @TableField("unit_price")
     private java.math.BigDecimal unitPrice;

     /**
      * 数量
      */
     @TableField("number")
     private Long number;

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
      * 支付平台(微信、支付宝、银联)
      */
     @TableField("pay_type")
     private String payType;

     /**
      * 支付流水号（第三方接口返回流水号）
      */
     @TableField("platform_number")
     private String platformNumber;

     /**
      * 支付状态（第三方接口返回状态）
      */
     @TableField("platform_status")
     private String platformStatus;

     /**
      * 支付设备 IOS ANDROID
      */
     @TableField("device")
     private String device;

     /**
      * 订单关闭时间
      */
     @TableField("close_time")
     private java.util.Date closeTime;

     /**
      * 订单完成时间
      */
     @TableField("finish_time")
     private java.util.Date finishTime;

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