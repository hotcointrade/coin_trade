package cn.stylefeng.guns.modular.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 收款方式 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-03-12 11:23:03
 */

@Data
@TableName("app_payment")
@Accessors(chain = true)
public class Payment extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 收款方式
      */
     @TableId(value="payment_id", type= IdType.AUTO)
     private Long paymentId;

     /**
      * 用户
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 姓名
      */
     @TableField("name")
     private String name;

     /**
      * 账号
      */
     @TableField("idcard")
     private String idcard;

     /**
      * 类型
      */
     @TableField("type")
     private String type;

     /**
      * 收款码
      */
     @TableField("img")
     private String img;

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