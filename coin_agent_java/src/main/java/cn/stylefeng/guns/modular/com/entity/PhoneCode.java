package cn.stylefeng.guns.modular.com.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 电话区号 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-12-20 17:59:48
 */

@Data
@TableName("com_phone_code")
@Accessors(chain = true)
public class PhoneCode extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * ID
      */
     @TableId(value="phone_code_id", type= IdType.AUTO)
     private Long phoneCodeId;

     /**
      * 国家
      */
     @TableField("country")
     private String country;

     /**
      * 区号
      */
     @TableField("code")
     private String code;

     /**
      * 类型 0：国内 1：国外
      */
     @TableField("type")
     private String type;

     /**
      * 所在区域
      */
     @TableField("area")
     private String area;

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