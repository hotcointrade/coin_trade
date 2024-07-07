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
 * 资讯分类
 */
@Data
@TableName("com_information_category")
@Accessors(chain = true)
public class InformationCategory extends BaseEntity implements Serializable {

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
      * 排序
      */
     @TableField("sort")
     private Integer sort;


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