package cn.stylefeng.guns.modular.com.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 收货地址 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-12-10 20:11:22
 */

@Data
@TableName("com_shipping")
@Accessors(chain = true)
public class Shipping extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 收货地址id
      */
     @TableId(value="shipping_id", type= IdType.AUTO)
     private Long shippingId;

     /**
      * 用户id
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 收货人姓名
      */
     @TableField("name")
     private String name;

     /**
      * 电话
      */
     @TableField("phone")
     private String phone;

     /**
      * 收货地址
      */
     @TableField("address")
     private String address;

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