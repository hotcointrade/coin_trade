package cn.stylefeng.guns.modular.com.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 行业资讯 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-12-10 14:09:15
 */

@Data
@TableName("com_business")
@Accessors(chain = true)
public class Business extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 行业资讯表
      */
     @TableId(value="business_id", type= IdType.AUTO)
     private Long businessId;

     /**
      * 文章类型
      */
     @TableField("type")
     private String type;

     /**
      * 主图
      */
     @TableField("main_img")
     private String mainImg;

     /**
      * 子图JSON
      */
     @TableField("sub_img")
     private String subImg;

     /**
      * 标题
      */
     @TableField("title")
     private String title;

     /**
      * 内容
      */
     @TableField("content")
     private String content;

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