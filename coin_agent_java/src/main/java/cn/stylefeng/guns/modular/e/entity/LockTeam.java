package cn.stylefeng.guns.modular.e.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 锁仓团队奖配置 实体类 </p>
  *
  * @author yaying.liu
  * @since 2020-09-25 10:45:37
 */

@Data
@TableName("e_lock_team")
@Accessors(chain = true)
public class LockTeam extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 锁仓团队奖配置
      */
     @TableId(value="lock_team_id", type= IdType.AUTO)
     private Long lockTeamId;

     /**
      * 编码
      */
     @TableField("code")
     private String code;

     /**
      * 描述
      */
     @TableField("detail")
     private String detail;

     /**
      * 最低要求（USDT)
      */
     @TableField("price")
     private java.math.BigDecimal price;

     /**
      * 团队奖收益率（%）
      */
     @TableField("rate")
     private String rate;

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