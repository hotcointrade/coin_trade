package cn.stylefeng.guns.modular.e.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 期货杠杆倍率
 */
@TableName("e_futures_leverage")
@Data
@Accessors(chain = true)
public class FuturesLeverage extends BaseEntity implements Serializable{
	@TableId(value="futures_leverage_id", type= IdType.AUTO)
	private Long futuresLeverageId;
	/** 名称 */
	@TableField("name")
	private String name;
	/** 倍率 */
	@TableField("value")
	private BigDecimal value;
	/** 状态(字典) */
	@TableField("status")
	private String status;
	/** 删除标志 */
	@TableField("del")
	private String del;
	/** 备注 */
	@TableField("remark")
	private String remark;

    
	
}