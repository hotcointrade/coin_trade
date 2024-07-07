package cn.stylefeng.guns.modular.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 团队返佣 实体类 </p>
  *
  * @author yaying.liu
  * @since 2022-08-22 14:46:40
 */

@Data
@TableName("app_team_level")
@Accessors(chain = true)
public class TeamLevel extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 代理返佣
      */
     @TableId(value="team_level_id", type= IdType.AUTO)
     private Long teamLevelId;

     /**
      * 等级
      */
     @TableField("level")
     private Integer level;

     /**
      * 数量
      */
     @TableField("number")
     private String number;

     /**
      * 是否直属
      */
     @TableField("teamNotBonusRecord")
     private String teamNotBonusRecord;

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
