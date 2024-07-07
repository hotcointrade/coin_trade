package cn.stylefeng.guns.modular.fin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 申请 实体类 </p>
  *
  * @author yaying.liu
  * @since 2022-10-18 20:42:53
 */

@Data
@TableName("fin_loan")
@Accessors(chain = true)
public class Loan extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      *
      */
     @TableId(value="loan_id", type= IdType.AUTO)
     private Long loanId;

     /**
      * 手机号
      */
     @TableField("member_id")
     private Long memberId;
     /**
      * 手机号
      */
     @TableField("phone")
     private String phone;

     /**
      * 币种
      */
     @TableField("coin")
     private String coin;

     /**
      * 收款钱包地址
      */
     @TableField("address")
     private String address;

     /**
      * 身份证
      */
     @TableField("id_card")
     private String idCard;

     /**
      * 护照
      */
     @TableField("id_card2")
     private String idCard2;

     /**
      * 金额
      */
     @TableField("price")
     private java.math.BigDecimal price;

     /**
      *
      */
     @TableField("img")
     private String img;

     /**
      * 备注
      */
     @TableField("remark")
     private String remark;

     /**
      * 状态
      */
     @TableField("status")
     private String status;

     /**
      * 删除标志
      */
     @TableField("del")
     private String del;




 }
