package cn.stylefeng.guns.modular.chain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 用户钱包地址 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-12-10 18:24:36
 */

@Data
@TableName("chain_coin")
@Accessors(chain = true)
public class Coin extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 币地址id
      */
     @TableId(value="chain_coin_id", type= IdType.AUTO)
     private Long chainCoinId;

     /**
      * 币地址
      */
     @TableField("address")
     private String address;

     /**
      * 用户ID
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