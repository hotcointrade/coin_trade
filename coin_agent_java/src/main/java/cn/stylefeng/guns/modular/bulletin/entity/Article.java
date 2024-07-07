package cn.stylefeng.guns.modular.bulletin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@TableName("com_article")
@Data
@Accessors(chain = true)
public class Article extends BaseEntity {
    @TableId(value = "article_id", type = IdType.AUTO)
    private Long articleId;

    @TableField("article_type")
    private String articleType;
    @TableField("main_img")
    private String mainImg;
    @TableField("title")
    private String title;
    @TableField("status")
    private String status;
    private String  language;
    @TableField("del")
    private String del;
    @TableField("remark")
    private String remark;
    @TableField("sub_img")
    private String subImg;
    @TableField("content")
    private String content;

}
