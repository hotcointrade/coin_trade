package cn.stylefeng.guns.modular.fin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 第三方接口记录表 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-12-23 14:15:58
 */

@Data
@TableName("fin_pay_record")
@Accessors(chain = true)
public class PayRecord extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 
      */
     @TableId(value="pay_record_id", type= IdType.AUTO)
     private Long payRecordId;

     /**
      * 接口编码
      */
     @TableField("pay_code")
     private String payCode;

     /**
      * 请求数据
      */
     @TableField("request_data")
     private String requestData;

     /**
      * 响应数据
      */
     @TableField("response_data")
     private String responseData;

     /**
      * 用户id
      */
     @TableField("member_id")
     private Long memberId;

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