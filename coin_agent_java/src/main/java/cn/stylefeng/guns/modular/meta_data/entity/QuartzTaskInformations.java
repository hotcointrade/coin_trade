package cn.stylefeng.guns.modular.meta_data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@TableName("quartz_task_informations")
@Data
public class QuartzTaskInformations implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @TableField("version")
    private Integer version;

    @TableField("taskno")
    private String taskno;
    @TableField("taskname")
    private String taskname;
    @TableField("schedulerrule")
    private String schedulerrule;
    @TableField("frozenstatus")
    private String frozenstatus;
    @TableField("executorno")
    private String executorno;
    @TableField("frozentime")
    private Long frozentime;
    @TableField("unfrozentime")
    private Long unfrozentime;
    @TableField("createtime")
    private Long createtime;
    @TableField("lastmodifytime")
    private Long lastmodifytime;
    @TableField("sendtype")
    private String sendtype;
    @TableField("url")
    private String url;
    @TableField("executeparamter")
    private String executeparamter;
    @TableField("timekey")
    private String timekey;
}
