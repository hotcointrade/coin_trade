package cn.stylefeng.guns.modular.e.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 生活支付打款 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-06-29 17:01:20
 */

@Data
@TableName("e_live_record")
@Accessors(chain = true)
public class LiveRecord extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 生活支付打款
      */
     @TableId(value="live_record_id", type= IdType.AUTO)
     private Long liveRecordId;

     /**
      * 订单号
      */
     @TableField("order_no")
     private String orderNo;

     /**
      * 用户
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 用户
      */
     @TableField("account")
     private String account;

     /**
      * 付款金额（CNY）
      */
     @TableField("cny")
     private java.math.BigDecimal cny;

     /**
      * 支付数量（MGE）
      */
     @TableField("md")
     private java.math.BigDecimal md;

     /**
      * 链接
      */
     @TableField("link")
     private String link;

     /**
      * 二维码
      */
     @TableField("link_code")
     private String linkCode;

     /**
      * 打款备注
      */
     @TableField("content")
     private String content;

     /**
      * 付款时间
      */
     @TableField("pay_time")
     private java.util.Date payTime;

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