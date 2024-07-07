package cn.stylefeng.guns.modular.com.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 视频和音频 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-12-10 14:44:00
 */

@Data
@TableName("com_media")
@Accessors(chain = true)
public class Media extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 多媒体资料id
      */
     @TableId(value="media_id", type= IdType.AUTO)
     private Long mediaId;

     /**
      * 标题
      */
     @TableField("title")
     private String title;

     /**
      * 内容
      */
     @TableField("content")
     private String content;

     /**
      * 链接地址
      */
     @TableField("address")
     private String address;

     /**
      * 图片
      */
     @TableField("img")
     private String img;

     /**
      * 类型(0:视频 1:语音）
      */
     @TableField("type")
     private String type;

     /**
      * 排序
      */
     @TableField("sort")
     private Long sort;

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