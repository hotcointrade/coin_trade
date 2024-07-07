package cn.stylefeng.guns.modular.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 官方社区 实体类 </p>
  *
  * @author yaying.liu
  * @since 2022-10-18 18:24:14
 */

@Data
@TableName("app_community")
@Accessors(chain = true)
public class Community extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 
      */
     @TableId(value="community_id", type= IdType.AUTO)
     private Long communityId;

     /**
      * 内容
      */
     @TableField("content")
     private String content;

     /**
      * 语言
      */
     @TableField("language")
     private String language;

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