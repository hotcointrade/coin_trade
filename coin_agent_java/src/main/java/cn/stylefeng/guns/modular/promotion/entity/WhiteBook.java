package cn.stylefeng.guns.modular.promotion.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
  * <p> 白皮书 实体类 </p>
 */

@Data
@TableName("com_white_book")
@Accessors(chain = true)
public class WhiteBook extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     @TableId(value="id", type= IdType.AUTO)
     private Long id;

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
     private String language;


     /**
      * 图片
      */
     @TableField("img")
     private String img;

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
