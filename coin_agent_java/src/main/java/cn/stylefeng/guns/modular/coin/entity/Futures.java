package cn.stylefeng.guns.modular.coin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 期货交易对
 */
@TableName("coin_futures")
@Data
@Accessors(chain = true)
public class Futures extends BaseEntity implements Serializable{
	/** 期货交易对 */
	@TableId(value="futures_id", type= IdType.AUTO)
	private Long futuresId;
	/** 交易对 */
	@TableField("symbol")
	private String symbol;
	/** 币种类型（0-平台币  1-火币) */
	@TableField("type")
	private String type;
	/** 点差 */
	@TableField("spread")
	private BigDecimal spread;
	/** 版本号 */
	@TableField("version")
	private Long version;
	/** 状态(字典) */
	@TableField("status")
	private String status;
	/** 删除标志 */
	@TableField("del")
	private String del;
	/** 备注 */
	@TableField("remark")
	private String remark;

	/** 保留小数位 */
	@TableField("number")
	private BigDecimal number;
	/**  每手价值数量 */
	@TableField("hand_number")
	private BigDecimal handNumber;
	/** 建仓手续费 */
	@TableField("open_fee_price")
	private BigDecimal openFeePrice;
	/** 平仓手续费 */
	@TableField("close_fee_price")
	private BigDecimal closeFeePrice;
	/** 最小价格变动 */
	@TableField("fluncuation")
	private String fluncuation;
    
	
}