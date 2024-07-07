package cn.stylefeng.guns.modular.otc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 用户押金 实体类 </p>
  *
  * @author yaying.liu
  * @since 2020-07-01 11:55:46
 */

@Data
@TableName("otc_deposit")
@Accessors(chain = true)
public class Deposit extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 用户押金
      */
     @TableId(value="deposit_id", type= IdType.AUTO)
     private Long depositId;

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
      * 昵称
      */
     @TableField("nick_name")
     private String nickName;

     /**
      * 币种
      */
     @TableField("coin")
     private String coin;

     /**
      * 数量
      */
     @TableField("number")
     private java.math.BigDecimal number;

     /**
      * 版本号
      */
     @TableField("version")
     @Version
     private Long version;

     /**
      * 状态 全额缴纳-1   待补缴-0
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