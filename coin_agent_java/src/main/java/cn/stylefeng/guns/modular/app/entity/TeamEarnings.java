package cn.stylefeng.guns.modular.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 团队收益 实体类 </p>
  *
  * @author yaying.liu
  * @since 2022-02-18 20:22:22
 */

@Data
@TableName("app_team_earnings")
@Accessors(chain = true)
public class TeamEarnings extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      *
      */
     @TableId(value="team_earnings_id", type= IdType.AUTO)
     private Long teamEarningsId;

     /**
      * 上级id
      */
     @TableField("earnings_id")
     private Long earningsId;

     /**
      * 上级账户
      */
     @TableField("earnings_account")
     private String earningsAccount;

     /**
      * 收益数量
      */
     @TableField("price")
     private java.math.BigDecimal price;
     @TableField("type")
     private String type;

     /**
      * 是否直属
      */
     @TableField("direct")
     private String direct;

     /**
      * 注册人id
      */
     @TableField("regist_id")
     private Long registId;

     /**
      * 注册人账户
      */
     @TableField("regist_account")
     private String registAccount;

     /**
      * 删除标志
      */
     @TableField("del")
     private String del;




 }
