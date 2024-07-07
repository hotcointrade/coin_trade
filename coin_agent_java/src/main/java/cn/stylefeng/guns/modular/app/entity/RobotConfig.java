package cn.stylefeng.guns.modular.app.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("sys_robot_config")
@Accessors(chain = true)
public class RobotConfig extends BaseEntity implements Serializable {
    @TableId(value = "robot_id", type = IdType.AUTO)
    private Long robotId;

    /**
     * 机器人账户
     */
    @TableField("robot_user_id")
    private Long robotUserId;

    @TableField("robot_account")
    private String robotAccount;

    //交易对
    @TableField("symbol")
    private String symbol;
    //最大值
    @TableField("max_valuee")
    private BigDecimal maxValuee;

    //撮合的用户
    @TableField("user_id")
    private Long userId;
    @TableField("account")
    private String account;

    //模式
    @TableField("trade_mode")
    private String tradeMode;

    //状态 0开启 1关闭
    @TableField("status")
    private String status;

}
