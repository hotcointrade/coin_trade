package cn.stylefeng.guns.modular.app.controller.market;

import lombok.Data;

@Data
public class Depth {
	//当前的所有买单 [price, quote volume]
	private Object bids;
	//当前的所有卖单 [price, quote volume]
	private Object asks;
	private Long version;
	private Long ts;
	
}
