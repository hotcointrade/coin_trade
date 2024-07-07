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
  * <p> 常见问题 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-10-14 14:11:51
 */

@Data
@TableName("com_problem")
@Accessors(chain = true)
public class Problem extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 常见问题id
      */
     @TableId(value="problem_id", type= IdType.AUTO)
     private Long problemId;

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
      * 图片
      */
     @TableField("img")
     private String img;

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
