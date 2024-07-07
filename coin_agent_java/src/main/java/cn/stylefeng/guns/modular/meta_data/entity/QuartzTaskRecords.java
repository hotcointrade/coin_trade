package cn.stylefeng.guns.modular.meta_data.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@TableName("quartz_task_records")
@Data
public class QuartzTaskRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @TableField("taskno")
    private String taskno;

    @TableField("timekeyvalue")
    private String timekeyvalue;

    @TableField("executetime")
    private Long executetime;

    @TableField("taskstatus")
    private String taskstatus;

    @TableField("failcount")
    private Integer failcount;

    @TableField("failreason")
    private String failreason;

    @TableField("createtime")
    private Long createtime;

    @TableField("lastmodifytime")
    private Long lastmodifytime;
}
