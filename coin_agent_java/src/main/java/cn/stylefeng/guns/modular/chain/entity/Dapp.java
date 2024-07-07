package cn.stylefeng.guns.modular.chain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 钱包调用api记录 实体类 </p>
  *
  * @author yaying.liu
  * @since 2020-08-18 19:46:56
 */

@Data
@TableName("chain_dapp")
@Accessors(chain = true)
public class Dapp extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 钱包调用api记录
      */
     @TableId(value="dapp_id", type= IdType.AUTO)
     private Long dappId;

     /**
      * 接口名称
      */
     @TableField("name")
     private String name;

     /**
      * 请求
      */
     @TableField("request")
     private String request;

     /**
      * 响应
      */
     @TableField("response")
     private String response;

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