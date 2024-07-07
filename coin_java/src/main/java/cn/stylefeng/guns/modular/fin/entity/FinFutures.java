package cn.stylefeng.guns.modular.fin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
  * <p> 合约账户 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-03-12 19:09:49
 */

@Data
@TableName("fin_futures")
@Accessors(chain = true)
public class FinFutures extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 合约账户
      */
     @TableId(value="contract_id", type= IdType.AUTO)
     private Long contractId;

     /**
      * 用户id
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 权益账户
      */
     @TableField("worth_price")
     private BigDecimal worthPrice;

     /**
      * 未实现盈亏
      */
     @TableField("no_pl")
     private BigDecimal noPl;

     /**
      * 持仓保证金
      */
     @TableField("position_price")
     private BigDecimal positionPrice;

     /**
      * 可用保证金
      */
     @TableField("used_price")
     private BigDecimal usedPrice;

     /**
      * 委托保证金资产

      */
     @TableField("entrust_price")
     private BigDecimal entrustPrice;

     /**
      * 持仓配资
      */
     @TableField("give_price")
     private BigDecimal givePrice;

     /**
      * 保证金率
      */
     @TableField("rate")
     private BigDecimal rate;

     /**
      * 类型
      */
     @TableField("type")
     private String type;

     /**
      * 版本
      */
     @TableField("version")
     private Long version;

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
      * 钱包大类型
      */
     @TableField("remark")
     private String remark;

     //初始化
     public static FinFutures init(){
          FinFutures entity=new FinFutures();
          entity.setWorthPrice(BigDecimal.ZERO)
                  .setUsedPrice(BigDecimal.ZERO)
                  .setPositionPrice(BigDecimal.ZERO)
                  .setNoPl(BigDecimal.ZERO);
          return entity;
     }


 }
