package cn.stylefeng.guns.modular.app.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
  * <p> 用户的充币地址 实体类 </p>
 */
@Data
@TableName("app_member_recharge_address")
@Accessors(chain = true)
public class MemberRechargeAddress implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 地址id
      */
     @TableId(value="id", type= IdType.AUTO)
     private Long id;

     /**
      * 用户
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 钱包id
      */
     @TableField("wallet_id")
     private String walletId;

     /**
      * 钱包名称
      */
     @TableField("wallet_name")
     private String walletName;

     /**
      * 地址
      */
     @TableField("address")
     private String address;

     /**
      * 币种
      */
     @TableField("coin")
     private String coin;

     /**
      * memo值或Tag值
      */
     @TableField("remark")
     private String remark;


     @TableField(value = "create_time", fill = FieldFill.INSERT)
     private Date createTime;


     @TableField(value = "update_time", fill = FieldFill.UPDATE)
     private Date updateTime;


 }