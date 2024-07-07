package cn.stylefeng.guns.core.bipay.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SINOHOPE Waas充值地址
 */
@Getter
@AllArgsConstructor
public enum WaasCoinType {

//	SEPOLIA("SEPOLIA","USDT-ERC20"),

	ETH("ETH","USDT-ERC20"),

	TRON("TRON","USDT-TRC20"),
	;

	//链名
	private final String chainName;

	private final String chainCoin;

}
