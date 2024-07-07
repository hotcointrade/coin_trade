package cn.stylefeng.guns.modular.fin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 理财释放记录
 */
@TableName("te_management_release")
@Data
public class TeManagementRelease implements Serializable{
	/** id */
	@TableField("id")
	private Long id;
	/** 用户id */
	@TableField("uid")
	private Long uid;
	//图标
	@TableField("img")
	private String img;
	/** 用户名 */
	@TableField("username")
	private String username;
	/** 理财购买id */
	@TableField("detail_id")
	private Long detailId;
	/** 认筹币种 */
	@TableField("instrument")
	private String instrument;
	/** 认筹数量 */
	@TableField("sum")
	private String sum;
	/** 收益数量 */
	@TableField("earnings_sum")
	private String earningsSum;
	/** 状态:0=待释放,1=已释放 */
	@TableField("status")
	private String status;
	/** 释放时间 */
	@TableField("period_time")
	private Date periodTime;
	/** 添加时间 */
	@TableField("createtime")
	private Date createtime;
	/** 更新时间 */
	@TableField("updatetime")
	private Date updatetime;


}
