package cn.stylefeng.guns.modular.bulletin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@TableName("com_article")
public class Article extends BaseEntity {
    @TableId(value = "article_id", type = IdType.AUTO)
    private Long articleId;

    private String articleType;
    private String mainImg;
    private String title;
    private String status;
    private String  language;
    private String del;
    private String remark;
    private String subImg;
    private String content;

}
