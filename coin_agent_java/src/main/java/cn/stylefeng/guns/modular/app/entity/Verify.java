package cn.stylefeng.guns.modular.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 实名认证 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-03-11 16:31:58
 */

@Data
@TableName("app_verify")
@Accessors(chain = true)
public class Verify extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 实名认证id
      */
     @TableId(value="verify_id", type= IdType.AUTO)
     private Long verifyId;

     /**
      * 用户id
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 真实姓名
      */
     @TableField("name")
     private String name;

     /**
      * 身份证号
      */
     @TableField("id_card")
     private String idCard;

     /**
      * 姓
      */
     @TableField("fist_name")
     private String fistName;

     /**
      * 名
      */
     @TableField("last_name")
     private String lastName;


     /**
      * 证件类型
      */
     @TableField("type")
     private String type;

     /**
      * 正面
      */
     @TableField("front_img")
     private String frontImg;

     /**
      * 反面
      */
     @TableField("back_img")
     private String backImg;

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