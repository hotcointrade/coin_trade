package cn.stylefeng.guns.modular.redisMsg;

import cn.stylefeng.guns.core.websocket.WebSocketService;
import cn.stylefeng.guns.modular.app.service.JobService;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.coin.entity.SymbolsSetting;
import cn.stylefeng.guns.modular.coin.service.SymbolsSettingService;
import cn.stylefeng.guns.modular.fin.service.WalletService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class RedisReceiver {
	
	@Autowired
	private Gson gson;
	
	@Autowired
	private JobService jobService;
	
	@Resource
	WalletService walletService;
	@Autowired
	SymbolsSettingService symbolsSettingService;
	
	public void receiveMessage(String message) {
		String coin = gson.fromJson(message, String.class);
		System.err.println("消息队列深度刷新：" + coin);
		jobService.mdDepth(coin);
	}
	
	public void releaseFot(String message) {
		String msg = gson.fromJson(message, String.class);
		System.err.println("释放fot：" + msg);
		walletService.releaseFotRun();
	}
	
	public void createKlineReturn(String message) {
		SymbolsSetting symbolsSetting = gson.fromJson(message, SymbolsSetting.class);
		System.err.println("接收createKlineReturn：" + symbolsSetting.getSymbol());
		symbolsSetting.setCreateUser(Constant.SYS_PLATFORM);
		symbolsSetting.setUpdateUser(Constant.SYS_PLATFORM);
		SymbolsSetting symbolsSetting1 = new SymbolsSetting();
		symbolsSetting1.setSymbol(symbolsSetting.getSymbol());
		symbolsSetting1.setP8LossRatio(symbolsSetting.getP8LossRatio());
		int one = symbolsSettingService.count(new QueryWrapper<>(symbolsSetting1));
		if (one == 0)
			symbolsSettingService.save(symbolsSetting);
		try {
			if (one == 0) {
				WebSocketService.sendInfo(symbolsSetting.getSymbol() + "生成k线数据成功!", Constant.SYSTEMSOCKETSENDNAME);
			} else {
				WebSocketService.sendInfo(symbolsSetting.getSymbol() + "生成k线数据失败!已有k线数据", Constant.SYSTEMSOCKETSENDNAME);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void createKlineReturnError(String message) {
		SymbolsSetting symbolsSetting = gson.fromJson(message, SymbolsSetting.class);
		System.err.println("接收createKlineReturn：" + symbolsSetting.getSymbol());
		System.err.println("接收createKlineReturnError：" + message);
		try {
			WebSocketService.sendInfo(symbolsSetting.getSymbol() + "生成数据失败!请重试", Constant.SYSTEMSOCKETSENDNAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
