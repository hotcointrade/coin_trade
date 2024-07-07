package cn.stylefeng.guns.modular.promotion.entity;

 import com.baomidou.mybatisplus.annotation.*;
 import lombok.Data;
 import java.io.Serializable;
 import cn.stylefeng.guns.modular.base.entity.BaseEntity;
 import lombok.experimental.Accessors;

 /**
  * <p> 网关记录 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-10-11 10:44:44
  */

 @Data
 @TableName("gateway_record")
 @Accessors(chain = true)
 public class GatewayRecord extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 网关记录id
      */
     @TableId(value="gateway_record_id", type= IdType.AUTO)
     private Long gatewayRecordId;

     /**
      * 接口编码
      */
     @TableField("interface_code")
     private String interfaceCode;

     /**
      * 请求参数
      */
     @TableField("request_data")
     private String requestData;

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