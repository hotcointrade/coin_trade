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
  * <p> 用户协议 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-10-12 19:31:14
 */

@Data
@TableName("app_declares")
@Accessors(chain = true)
public class Declares extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      *
      */
     @TableId(value="declare_id", type= IdType.AUTO)
     private Long declareId;

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
     private String language;

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
