package cn.stylefeng.guns.modular.bulletin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 轮播图
 */
@TableName("com_news")
@Data
@Accessors(chain = true)
public class News extends BaseEntity {

    @TableId(value = "carousel_id",type = IdType.AUTO)
    private Long carouselId;

    @TableField("img")
    private String img;

    @TableField("title")
    private String title;
    private String language;
    @TableField("link")
    private String link;
    @TableField("sort")
    private Long sort;
    @TableField("status")
    private String status;
    @TableField("del")
    private String del;
    @TableField("remark")
    private String remark;
    @TableField("content")
    private String content;


}
