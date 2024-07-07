package cn.stylefeng.guns.modular.otc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 申诉订单 实体类 </p>
  *
  * @author yaying.liu
  * @since 2020-07-15 17:16:25
 */

@Data
@TableName("otc_appeal")
@Accessors(chain = true)
public class Appeal extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 申诉订单
      */
     @TableId(value="appeal_id", type= IdType.AUTO)
     private Long appealId;

     /**
      * 操作挂单id
      */
     @TableField("opid")
     private Long opid;

     /**
      * 订单类型（BUY-购买单 SELL-出售单）
      */
     @TableField("type")
     private String type;

     /**
      * 买方
      */
     @TableField("buy_mid")
     private Long buyMid;

     /**
      * 买方昵称
      */
     @TableField("buy_name")
     private String buyName;

     /**
      * 卖方
      */
     @TableField("sell_mid")
     private Long sellMid;

     /**
      * 卖方昵称
      */
     @TableField("sell_name")
     private String sellName;

     /**
      * 订单号
      */
     @TableField("order_no")
     private String orderNo;

     /**
      * 参考号
      */
     @TableField("mark_no")
     private String markNo;

     /**
      * 交易币种
      */
     @TableField("coin")
     private String coin;

     /**
      * 交易单价
      */
     @TableField("unit_price")
     private java.math.BigDecimal unitPrice;

     /**
      * 交易数量
      */
     @TableField("number")
     private java.math.BigDecimal number;

     /**
      * 交易金额
      */
     @TableField("cny")
     private java.math.BigDecimal cny;

     /**
      * 交易方式
      */
     @TableField("pay_method")
     private String payMethod;

     /**
      * 收款人
      */
     @TableField("pay_name")
     private String payName;

     /**
      * 收款账号
      */
     @TableField("pay_account")
     private String payAccount;

     /**
      * 收款二维码（微信、支付宝）
      */
     @TableField("pay_img")
     private String payImg;

     /**
      * 银行
      */
     @TableField("bank")
     private String bank;

     /**
      * 支行
      */
     @TableField("branch")
     private String branch;

     /**
      * 付款时间
      */
     @TableField("pay_time")
     private java.util.Date payTime;

     /**
      * 下单时间
      */
     @TableField("order_time")
     private java.util.Date orderTime;

     /**
      * 申诉方（BUY-买方 SELL-卖方）
      */
     @TableField("appeal_type")
     private String appealType;

     /**
      * 申诉内容
      */
     @TableField("content")
     private String content;

     /**
      * 申诉凭证
      */
     @TableField("img")
     private String img;

     /**
      * 责任方（BUY-买方责任 SELL-卖方责任 NO-双方无责）
      */
     @TableField("duty")
     private String duty;

     /**
      * 版本号
      */
     @TableField("version")
     @Version
     private Long version;

     /**
      * 状态()
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