package cn.stylefeng.guns.modular.bulletin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 轮播图
 */
@TableName("app_carousel")
@Data
@Accessors(chain = true)
public class AppCarousel extends BaseEntity {

    @TableId(value = "carousel_id",type = IdType.AUTO)
    private Long carouselId;

    @TableField("img")
    private String img;

    @TableField("title")
    private String title;

    @TableField("link")
    private String link;
    @TableField("sort")
    private Long sort;
    @TableField("type")
    private String type;
    /**
     * 显示端口：PC表示pc端，PHONE表示移动端
     */
    @TableField("show_type")
    private String showType;

    @TableField("status")
    private String status;

    @TableField("del")
    private String del;
    @TableField("remark")
    private String remark;
    @TableField("content")
    private String content;


}