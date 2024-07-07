package cn.stylefeng.guns.modular.e.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 行情涨跌控制 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-03-23 11:44:38
 */

@Data
@TableName("e_quotes")
@Accessors(chain = true)
public class Quotes extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 行情涨跌控制
      */
     @TableId(value="quotes_id", type= IdType.AUTO)
     private Long quotesId;

     /**
      * 币种
      */
     @TableField("symbols")
     private String symbols;

     /**
      * 行情幅度
      */
     @TableField("value")
     private java.math.BigDecimal value;


     /**
      * 状态(字典)
      */
     @TableField("status")
     private String status;

     //拉升次数
     @TableField("version")
     private java.math.BigDecimal version;
     //拉升之后的输值
     @TableField("group_value")
     private java.math.BigDecimal groupValue;
     //持续时间 秒s
     @TableField("keep_times")
     private java.math.BigDecimal keepTimes;

     /**
      * 删除标志
      */
     @TableField("del")
     private String del;

     /**
      * 备注 接用字段
      */
     @TableField("remark")
     private String remark;




 }