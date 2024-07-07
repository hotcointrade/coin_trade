package cn.stylefeng.guns.core.bipay.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * U盾充值地址
 */
@Getter
@AllArgsConstructor
public enum UdunCoinType {
	
	ERC_USDT("60","USDT","ERC20","USDT-ERC20"),
	MATIC_USDT("62","USDT","MATIC","USDT-MATIC"),
	TRC_USDT("195","USDT","TRC20","USDT-TRC20"),
	;
	
	// 编号/合约地址
	private final String code;
	
	// 代币名称
	private final String coin;
	
	//链名
	private final String chainName;
	
	private final String chainCoin;
	


}
