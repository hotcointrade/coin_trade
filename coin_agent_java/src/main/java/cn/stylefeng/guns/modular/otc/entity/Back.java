package cn.stylefeng.guns.modular.otc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 退还押金 实体类 </p>
  *
  * @author yaying.liu
  * @since 2020-07-09 14:33:04
 */

@Data
@TableName("otc_back")
@Accessors(chain = true)
public class Back extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 押金退还
      */
     @TableId(value="back_id", type= IdType.AUTO)
     private Long backId;

     /**
      * 用户
      */
     @TableField("member_id")
     private Long memberId;

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