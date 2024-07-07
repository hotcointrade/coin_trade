package cn.stylefeng.guns.modular.app.controller.market;

import lombok.Data;
import lombok.experimental.Accessors;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Kline {
	//时间戳
	@Id
	private long id;
	private Long ts;

	private float amount;
	private Integer count;
	private float open;
	private float close;
	private float low;
	private float high;
	private float vol;
	private float rose;

	private Integer number;
	private BigDecimal cny;
	private String symbol;
	
	//分时
	private String period;
	//配合页面图标
	private String img;
}
