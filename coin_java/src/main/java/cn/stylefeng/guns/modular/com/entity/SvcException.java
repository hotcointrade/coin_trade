package cn.stylefeng.guns.modular.com.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 异常信息 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-12-24 10:20:02
 */

@Data
@TableName("com_svc_exception")
@Accessors(chain = true)
public class SvcException extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;


     /**
      * 异常id
      */
     @TableId(value="svc_exception_id", type= IdType.AUTO)
     private Long svcExceptionId;

     /**
      * 异常方法
      */
     @TableField("method")
     private String method;

     /**
      * 异常信息
      */
     @TableField("msg")
     private String msg;

     /**
      * 详情内容
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