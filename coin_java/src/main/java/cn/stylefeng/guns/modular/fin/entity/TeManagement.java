package cn.stylefeng.guns.modular.fin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 理财认购币
 */
@TableName("te_management")
@Data
public class TeManagement implements Serializable{
	/** 理财币id */
	@TableField("id")
	private Long id;
	/** 标题 */
	@TableField("title")
	private String title;
	/** 描述 */
	@TableField("describes")
	private String describes;
	/** 发行总量 */
	@TableField("issuance_count")
	private Integer issuanceCount;
	/** 发行价格 */
	@TableField("issuance_price")
	private BigDecimal issuancePrice;
	/** 支付币种 */
	@TableField("currency")
	private String currency;
	/** 封面图 */
	@TableField("img")
	private String img;
	/** 最低认购金额 */
	@TableField("min_price")
	private BigDecimal minPrice;
	/** 抽签总数 */
	@TableField("draws_total")
	private Integer drawsTotal;
	/** 中签率 */
	@TableField("odds_winning")
	private BigDecimal oddsWinning;
	/** 开始时间 */
	@TableField("start_time")
	private Date startTime;
	/** 结束时间 */
	@TableField("end_time")
	private Date endTime;
	/** 官方链接 */
	@TableField("com_link")
	private String comLink;
	/** 白皮书链接 */
	@TableField("white_book_link")
	private String whiteBookLink;
	/** 状态:1=进行中,2=已结束 */
	@TableField("status")
	private String status;
	/** 介绍 */
	@TableField("introduce")
	private String introduce;
	/** 条件 */
	@TableField("conditions")
	private String conditions;
    
	
}