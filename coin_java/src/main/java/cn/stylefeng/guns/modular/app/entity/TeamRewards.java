package cn.stylefeng.guns.modular.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 社区奖励 实体类 </p>
  *
  * @author yaying.liu
  * @since 2022-07-07 17:45:57
 */

@Data
@TableName("app_team_rewards")
@Accessors(chain = true)
public class TeamRewards extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      *
      */
     @TableId(value="team_rewards_id", type= IdType.AUTO)
     private Long teamRewardsId;

     /**
      * 人数
      */
     @TableField("number")
     private Integer number;

     /**
      * 奖励
      */
     @TableField("reward")
     private java.math.BigDecimal reward;
     /**
      * 币种
      */
     @TableField("coin")
     private String coin;

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
