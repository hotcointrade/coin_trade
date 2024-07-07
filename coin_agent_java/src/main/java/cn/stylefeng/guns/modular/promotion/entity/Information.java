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
 * 资讯
 */
@Data
@TableName("com_information")
@Accessors(chain = true)
public class Information extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * id
      */
     @TableId(value="id", type= IdType.AUTO)
     private Long id;

     /**
      * 标题
      */
     @TableField("title")
     private String title;

     /**
      * 封面
      */
     @TableField("cover")
     private String cover;

     /**
      * 内容
      */
     @TableField("content")
     private  String content;

     /**
      * 类别id
      */
     @TableField("category_id")
     private Long categoryId;


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