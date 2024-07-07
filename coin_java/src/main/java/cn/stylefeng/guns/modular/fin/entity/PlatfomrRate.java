package cn.stylefeng.guns.modular.fin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 系统汇率 实体类 </p>
  *
  * @author yaying.liu
  * @since 2022-10-18 13:18:11
 */

@Data
@TableName("fin_platform_rate")
@Accessors(chain = true)
public class PlatfomrRate extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 平台汇率id
      */
     @TableId(value="platform_rate_id", type= IdType.AUTO)
     private Long platformRateId;

     /**
      * 名称
      */
     @TableField("rate_name")
     private String rateName;

     /**
      * 语言代码
      */
     @TableField("rate_code")
     private String rateCode;

     /**
      * 汇率
      */
     @TableField("number")
     private String number;

     /**
      * 符号
      */
     @TableField("sign")
     private String sign;

     /**
      * 排序
      */
     @TableField("sort")
     private Integer sort;

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
