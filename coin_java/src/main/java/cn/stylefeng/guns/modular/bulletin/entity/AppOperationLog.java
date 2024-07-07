package cn.stylefeng.guns.modular.bulletin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * app操作日志
 */
@TableName("app_operation_log")
@Data
@Accessors(chain =true)
public class AppOperationLog {
    @TableId(value = "operation_log_id",type = IdType.AUTO)
    private Long operationLogId;
    @TableField("log_type")
    private String logType;
    @TableField("log_name")
    private String logName;
    @TableField("user_id")
    private Long userId;
    @TableField("class_name")
    private String className;
    @TableField("create_time")
    private Date createTime;
    @TableField("succeed")
    private String succeed;
    @TableField("method")
    private String method;
    @TableField("message")
    private String message;
}