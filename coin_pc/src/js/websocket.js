import {
	SocketD
} from '@noear/socket.d';
import {
	sockeUrl
} from '@/config/env.js'


class WebSocketService {
	constructor() {
		this.client = null;
		this.eventListener = SocketD.newEventListener()
			.doOnOpen(s => { //会话打开时
				// 暂时用不上
			})
			.doOnMessage((s, m) => { //收到任意消息时
				// 暂时用不上
			});
	}

	/**
	 * 
	 * 创建连接,放到App.vue进行初始化连接
	 * 
	 * @param {Object} onSubscribeCallback
	 */
	async connect() {
		this.client = await SocketD.createClient(sockeUrl).config((c) => c.fragmentSize(1024 * 1024))
			.listen(this.eventListener)
			.open();
	}

	/**
	 * 添加/修改订阅,切换币种时无需取消订阅，{"event":"sub","type":"historyTrade","symbol":"ETH/USDT"} 直接覆盖
	 * 
	 * 进入页面onshow请求http数据后在调用订阅
	 * 离开页面beforeDestroy,onHide后取消订阅
	 * 
	 * 历史订单
	 * {"event":"sub","type":"historyTrade","symbol":"BTC/USDT"}
	 * {"event":"unSub","type":"historyTrade"}  
	 * 深度/盘口
	 * {"event":"sub","type":"depth","symbol":"BTC/USDT"}
	 * {"event":"unSub","type":"depth"}
	 * k线
	 * {"event":"sub","type":"kline","symbol":"BTC/USDT_1m"}
	 * {"event":"unSub","type":"kline"}
	 * @param {Object} onSubscribeCallback
	 */
	addEvent(data) {
		// console.log("onSubscribeCallback", onSubscribeCallback);
		const entity = SocketD.newEntity(JSON.stringify(data));//.metaPut("sender","noear");
		//发送并请求（且，等待一个答复）
		this.client.sendAndRequest("/subscribe", entity).thenReply(reply=>{
		    //打印
		    console.info(reply);
		});

	}


	/**
	 * 添加消息回调
	 * event
	 * 		/ticket
	 * 		/historyTrade
	 * 		/depth
	 *		/kline
	 * @param {Object} onSubscribeCallback
	 */
	addDoOn(event,onSubscribeCallback) {
		// console.log("onSubscribeCallback", onSubscribeCallback);
		this.eventListener.doOn(event, onSubscribeCallback);
	}



	//  /**
	//   * 
	//   * 创建连接,执行该方法必须放到onShow生命周期,执行完成后onHide和beforeDestroy进行关闭连接
	//   * 
	//   * @param {Object} onSubscribeCallback
	//   */
	//   async connect(onSubscribeCallback) {
	// 	 this.client = await SocketD.createClient(sockeUrl).config((c) => c.fragmentSize(1024 * 1024))
	// 		.listen(SocketD.newEventListener()
	// 			.doOnOpen(session => {
	// 				// console.log("会话打开时", session);
	// 			})
	// 			.doOnMessage((session, msg) => { //收到消息
	// 				// console.log("收到消息",msg);
	// 			})
	// 			.doOn("/subscribe", onSubscribeCallback)
	// 		)
	// 		.open();
	// 		console.log( this.client);
	// }

	/**
	 * 关闭连接
	 */
	close() {
		if (this.client) {
			this.client.close();
		}
	}



}

export default new WebSocketService();