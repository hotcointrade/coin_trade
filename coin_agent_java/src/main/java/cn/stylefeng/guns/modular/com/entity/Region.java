package cn.stylefeng.guns.modular.com.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 地区 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-12-06 11:32:41
 */

@Data
@TableName("com_region")
@Accessors(chain = true)
public class Region extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 地区表id
      */
     @TableId(value="region_id", type= IdType.AUTO)
     private Long regionId;

     /**
      * 名称
      */
     @TableField("name")
     private String name;

     /**
      * 父级id
      */
     @TableField("parent_id")
     private Long parentId;

     /**
      * 缩写
      */
     @TableField("short_name")
     private String shortName;

     /**
      * 层级
      */
     @TableField("level_type")
     private Long levelType;

     /**
      * 编码
      */
     @TableField("city_code")
     private String cityCode;

     /**
      * 邮编
      */
     @TableField("zip_code")
     private String zipCode;

     /**
      * 合称
      */
     @TableField("merger_name")
     private String mergerName;

     /**
      * 经度
      */
     @TableField("lng")
     private Float lng;

     /**
      * 纬度
      */
     @TableField("lat")
     private Float lat;

     /**
      * 拼音
      */
     @TableField("pinyin")
     private String pinyin;

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