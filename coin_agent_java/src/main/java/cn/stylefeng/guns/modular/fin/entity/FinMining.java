package cn.stylefeng.guns.modular.fin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 矿机账户 实体类 </p>
  *
  * @author yaying.liu
  * @since 2022-06-07 13:37:29
 */

@Data
@TableName("fin_mining")
@Accessors(chain = true)
public class FinMining extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 币币账户
      */
     @TableId(value="mining_id", type= IdType.AUTO)
     private Long miningId;

     /**
      * 用户id
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 可用额度
      */
     @TableField("used_price")
     private java.math.BigDecimal usedPrice;

     /**
      * 冻结额度
      */
     @TableField("locked_price")
     private java.math.BigDecimal lockedPrice;

     /**
      * 类型
      */
     @TableField("type")
     private String type;

     /**
      * 版本
      */
     @TableField("version")
     @Version
     private Long version;

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
      * 钱包大类型
      */
     @TableField("remark")
     private String remark;




 }