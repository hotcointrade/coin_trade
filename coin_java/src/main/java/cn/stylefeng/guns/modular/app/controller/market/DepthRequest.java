package cn.stylefeng.guns.modular.app.controller.market;

import lombok.Data;

@Data
public class DepthRequest {
	private String symbol;
	private Integer type;
}
