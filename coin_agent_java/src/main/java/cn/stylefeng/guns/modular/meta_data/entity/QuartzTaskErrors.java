package cn.stylefeng.guns.modular.meta_data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("quartz_task_errors")
@Data
public class QuartzTaskErrors implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ID_WORKER)
    private Long id;

    @TableField("taskexecuterecordid")
    private String taskexecuterecordid;

    @TableField("errorkey")
    private String errorkey;

    @TableField("createtime")
    private Long createtime;

    @TableField("lastmodifytime")
    private Long lastmodifytime;

    @TableField("errorvalue")
    private String errorvalue;
}