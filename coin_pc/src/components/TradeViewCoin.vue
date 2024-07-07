<template>
	<div>
		<div id="trade-view"></div>
	</div>
</template>

<script>
import { widget } from '../../static/charting_library/charting_library.min.js'
import datafeeds from '@/js/datafees.js'
import { baseUrl } from '@/config/env'
import { klineApi } from '@/api/getData'
export default {
	props: ['nowPrice', 'symbolValue', 'type', 'klineObj', 'defaultfloatPrecision'],
	data() {
		return {
			symbol: this.symbolValue,
			interval: '1min',
			currTimestamp: parseInt(new Date().getTime() / 1000),
			chart: null,
			initdata: {},
			countDate: 0, //累加条数
			startData: 0, //起始条数
			lengsData: 200, //结束长度
			datafeeds: new datafeeds(this),
			onLoadedCallback: null, //初始数据回调
			onRealtimeCallback: null, //websocket数据回调
			baseUrl,
			// pageRequire: 0,
			// websock: null,
			// timerY: null,
			// newId: '',
		}
	},
	created() {
		console.log(this.symbol + '最新价格' + this.nowPrice)
	},
	computed: {
		getCoin() {
			return this.symbolValue
		},
		// getTimes() {
		// 	return this.interval
		// }
	},
	watch: {
		symbolValue(newvalue, oldvalue) {
			console.log("new Value:" + newvalue)
			console.log("old Value:" + oldvalue)
		},
		getCoin(newValue) {
			this.symbol = ((newValue).split('-'))[0];
			this.chart.chart().setChartType(1);
			this.chart.setSymbol(this.symbol, this.filter(this.interval));
			this.loadChart();
		},
		// getTimes(newValue) {
		// var that = this;
		// that.webSocket();
		// that.getKlineData();
		// }
	},

	methods: {
		filter(time) {
			if (time == '1min')
				return '1';
			else if (time == '5min')
				return '5';
			else if (time == '15min')
				return '15';
			else if (time == '30min')
				return '30';
			else if (time == '60min')
				return '60';
			else if (time == '4hour')
				return '240'
			else if (time == '1day')
				return '1D';
			else if (time == '1week')
				return '1W';
		},

		//初始化
		getBars(symbolInfo, resolution, rangeStartDate, rangeEndDate, onLoadedCallback) {
			this.getKlineData("init");
			this.onLoadedCallback = onLoadedCallback;
		},
		// 实时更新
		subscribeBars(symbolInfo, resolution, onRealtimeCallback, subscriberUID, onResetCacheNeededCallback) {
			// this.getKlineData("loadMore")
			this.onRealtimeCallback = onRealtimeCallback;
		},

		//获取配置信息
		getSymbol(symbol) {
			//console.log("//刻度值,保留小数，如：2位小数传100，"+this.defaultfloatPrecision)
			return {
				'name': symbol,
				'full_name': symbol,
				'timezone': 'America/New_York',
				'minmov': 1,
				'minmov2': 0,
				'pointvalue': 1,
				'fractional': false,
				'session': '24x7',
				'has_intraday': true,
				'has_no_volume': false,//是否将成交量独立出来
				'description': symbol,
				'pricescale': 10000, //** this.defaultfloatPrecision,//刻度值,保留小数，如：2位小数传100，
				'ticker': symbol,
			}
		},

		//设置品信息(重新获取初始数据/推送数据)
		setSymbols() {
			this.chart.setSymbol(this.symbol, this.filter(this.interval), function () {
				this.chart.chart().setVisibleRange(this.initdata)
				this.chart.chart().executeActionById("timeScaleReset");
			});
		},

		//卸载K线
		removeChart() {
			if (this.chart) {
				this.chart.remove();
				this.chart = null;
			}
		},

		//加载K线图插件
		loadChart() {
			var local = sessionStorage.getItem('language') || 'en';
			var languageTxt = '';

			if (local == 'en') {
				languageTxt = 'en'
			} else if (local == 'jp') {
				languageTxt = 'ja'
			} else if (local == 'ko') {
				languageTxt = 'ko'
			} else {
				languageTxt = 'zh_TW'
			}
			this.chart = new widget({
				container_id: 'trade-view',
				symbol: this.symbol,
				interval: this.filter(this.interval),
				locale: languageTxt,
				theme: 'Dark',
				debug: false,//注释在控制台打印的错误
				// autosize: true,
				// // //图标宽度
				width: '100%',
				// //图标高度
				height: 565,
				// fullscreen: true,
				// preset: "mobile",
				datafeed: this.datafeeds,
				timezone: 'America/New_York',
				library_path: 'static/charting_library/',
				indicators_file_name: "custom-study.js",
				drawings_access: { type: 'black', tools: [{ name: "Regression Trend" }] },
				toolbar_bg: '#131722',//左侧工具栏背景颜色
				loading_screen: {//定制加载进度条
					backgroundColor: "#131722"
				},
				charts_storage_url: 'http://saveload.tradingview.com',
				charts_storage_api_version: "1.1",
				client_id: 'tradingview.com',
				overrides: this.overrides(),
				//启用的功能
				enabled_features: [
					'same_data_requery',
					'disable_resolution_rebuild',//显示的时间与得到的数据时间一致
					'study_templates',
				],

				//面板上禁用的功能
				disabled_features: [
					//"header_resolutions", //时间选择、分时切换
					"timeframes_toolbar", //底部时间栏
					//"header_screenshot", //截图相机
					//"header_symbol_search", //搜索框
					"header_saveload",//储存
					//'save_chart_properties_to_local_storage',
					"volume_force_overlay",//在主数据列的同一窗格上放置成交量指示器
					//"use_localstorage_for_settings",//允许将用户设置保存到localstorage
					//"header_chart_type",//图表类型切换
					//"header_compare",//头部对比信息按钮
					//"display_market_status",
					"header_undo_redo", //头部左右箭头
					//"header_indicators", //指标
					//"snapshot_trading_drawings",//包含屏幕截图中的订单/位置/执行信号
					//"header_settings", //设置按钮
				],
				studies_overrides: {
					//柱状图颜色
					"volume.volume.color.0": "#FA5252",
					"volume.volume.color.1": "#12B886",

					"MA Cross.short:plot.color": "#6B3798",
					"MA Cross.long:plot.color": "#708957",

					//MACD样式
					"MACD.MACD.color": "#F6D399",
					"MACD.Signal.color": "#3884FF",
					"MACD.Histogram.color": "#6fc1a1",
					'MACD.Histogram.linewidth': 4,
				},
			});

			this.chart.onChartReady(function () {//创建tradingview图
				try {
					//1min按钮
					this.chart.createButton()
						.attr('title', "1min")
						.on('click', function (e) {

							if ($(this).hasClass("selected")) return;
							$(this)
								.addClass("selected")
								.parents(".group-wWM3zP_M-")
								.siblings(".group-wWM3zP_M-")
								.find(".selected")
								.removeClass("selected");
							this.interval = '1min';
							$(this).find('span').css('color', '#2178FB');
							$(this)
								.addClass("selected")
								.parents(".group-wWM3zP_M-")
								.siblings(".group-wWM3zP_M-")
								.find("span").css('color', '#61688a');
							this.chart.chart().setChartType(1);
							this.chart.setSymbol(this.symbol, "1");
						})
						.append('<span style="color: #2178FB;">1min</span>');
					//5min按钮
					this.chart.createButton()
						.attr('title', "5min")
						.on('click', function (e) {
							if ($(this).hasClass("selected")) return;
							$(this)
								.addClass("selected")
								.parents(".group-wWM3zP_M-")
								.siblings(".group-wWM3zP_M-")
								.find(".apply-common-tooltip.selected")
								.removeClass("selected");
							this.interval = '5min';
							$(this).find('span').css('color', '#2178FB');
							$(this)
								.addClass("selected")
								.parents(".group-wWM3zP_M-")
								.siblings(".group-wWM3zP_M-")
								.find("span").css('color', '#61688a');
							this.chart.chart().setChartType(1);
							this.chart.setSymbol(this.symbol, "5");
						})
						.append('<span style="color: #61688a;">5min</span>');
					// //15min按钮
					this.chart.createButton()
						.attr('title', "15min")
						.on('click', function (e) {
							if ($(this).hasClass("selected")) return;
							$(this)
								.addClass("selected")
								.parents(".group-wWM3zP_M-")
								.siblings(".group-wWM3zP_M-")
								.find(".apply-common-tooltip.selected")
								.removeClass("selected");
							this.interval = '15min';
							$(this).find('span').css('color', '#2178FB');
							$(this)
								.addClass("selected")
								.parents(".group-wWM3zP_M-")
								.siblings(".group-wWM3zP_M-")
								.find("span").css('color', '#61688a');
							this.chart.chart().setChartType(1);
							this.chart.setSymbol(this.symbol, "15");
						})
						.append('<span style="color: #61688a;">15min</span>');
					//30min
					this.chart.createButton()
						.attr('title', "30min")
						.on('click', function (e) {
							this.interval = '30min';
							$(this).find('span').css('color', '#2178FB');
							$(this)
								.addClass("selected")
								.parents(".group-wWM3zP_M-")
								.siblings(".group-wWM3zP_M-")
								.find("span").css('color', '#61688a');
							this.chart.chart().setChartType(1);
							this.chart.setSymbol(this.symbol, "30");
						})
						.append('<span style="color: #61688a ;">30min</span>');

					//1hour
					this.chart.createButton()
						.attr('title', "60min")
						.on('click', function (e) {
							this.interval = '60min';
							$(this).find('span').css('color', '#2178FB');
							$(this)
								.addClass("selected")
								.parents(".group-wWM3zP_M-")
								.siblings(".group-wWM3zP_M-")
								.find("span").css('color', '#61688a');
							this.chart.chart().setChartType(1);
							this.chart.setSymbol(this.symbol, "60");
						})
						.append('<span style="color:  #61688a;">1H</span>');

					//4hour
					this.chart.createButton()
						.attr('title', "4hour")
						.on('click', function (e) {
							this.interval = '4hour';
							$(this).find('span').css('color', '#2178FB');
							$(this)
								.addClass("selected")
								.parents(".group-wWM3zP_M-")
								.siblings(".group-wWM3zP_M-")
								.find("span").css('color', '#61688a');
							this.chart.chart().setChartType(1);
							this.chart.setSymbol(this.symbol, "240");
						})
						.append('<span style="color:  #61688a;">4H</span>');

					//1day
					this.chart.createButton()
						.attr('title', "1day")
						.on('click', function (e) {
							this.interval = '1day';
							$(this).find('span').css('color', '#2178FB');
							$(this)
								.addClass("selected")
								.parents(".group-wWM3zP_M-")
								.siblings(".group-wWM3zP_M-")
								.find("span").css('color', '#61688a');
							this.chart.chart().setChartType(1);
							this.chart.setSymbol(this.symbol, "1D");
						})
						.append('<span style="color:  #61688a;">1D</span>');

					//1week
					this.chart.createButton()
						.attr('title', "1week")
						.on('click', function (e) {
							this.interval = '1week';
							$(this).find('span').css('color', '#2178FB');
							$(this)
								.addClass("selected")
								.parents(".group-wWM3zP_M-")
								.siblings(".group-wWM3zP_M-")
								.find("span").css('color', '#61688a');
							this.chart.chart().setChartType(1);
							this.chart.setSymbol(this.symbol, "1W");
						})
						.append('<span style="color:  #61688a;">1W</span>');

					//图标属性
					this.chart.createButton()
						.attr('title', this.$t('nav.iconName'))
						.on('click', function (e) {
							this.chart.save(function (data) {
								this.chart.chart().executeActionById("chartProperties");
							})
						})
						.append('<img src="setting.svg" width="20" align="center"/>');

					//指标
					this.chart.createButton()
						.attr('title', this.$t('nav.quota'))
						.on('click', function (e) {
							this.chart.save(function (data) {
								this.chart.chart().executeActionById("insertIndicator");
							})
						})
						.append('<img src="quota.svg" width="20" align="center"/>');

					this.chart.chart().createStudy("Moving Average", !1, !1, [5], null, { 'Plot.color': '#E6D399' });
					this.chart.chart().createStudy("Moving Average", !1, !1, [10], null, { 'Plot.color': '#C479E5' });
					this.chart.chart().createStudy("Moving Average", !1, !1, [30], null, { 'Plot.color': '#8ADDC6' });
					//this.chart.chart().createStudy('指数平滑异同移动平均线(MACD_Custom)', false, false, [20], null, {});//自定义MACD,底部涨幅条
					//this.chart.chart().createStudy('MACD', false, false, [12], null, {});//自定义MACD,底部涨幅条

				} catch (e) { }
			})

		},

		overrides() {
			let style = {
				up: "#6FC1A1",//阳线
				down: "#EC6543",//阴线
				bg: "#121527",
				grid: "#1e2235",//方格
				grid: "#0066ED",//方格
				cross: "#9194A3",//十字交叉记号
				border: "#4e5b85",
				borderUpColor: "#6FC1A1",
				borderUpColor: "#0066ED",
				borderDownColor: "#EC6543",
				borderDownColor: "#0066ED",
				text: "#61688A",
				areatop: "rgba(122, 152, 247, .2)",
				areadown: "rgba(122, 152, 247, .02)"
			};

			return {
				//窗格背景
				"paneProperties.background": "#141826",
				//纵向网格线颜色
				"paneProperties.vertGridProperties.color": "#161b29",
				//横向网格线颜色
				"paneProperties.horzGridProperties.color": "#161b29",
				//标记水印透明度
				"symbolWatermarkProperties.transparency": 0,
				//刻度属性文本颜色
				"scalesProperties.textColor": '#757575',
				// 设置坐标轴字体大小
				//'scalesProperties.fontSize': 16,
				//不隐藏左上角标题
				'paneProperties.legendProperties.showLegend': true,
				'left_toolbar': false,
				//销量面板尺寸，支持的值: large, medium, small, tiny
				"volumePaneSize": "large",
				//隐藏左边尺度
				"scalesProperties.showLeftScale": false
			};
		},

		// 获取k线数据
		// flag:init 初始化，loadMore：加载更多
		getKlineData(flag) {
			this.$nextTick(async () => {
				var dataArr = new URLSearchParams();
				dataArr.set('symbol', this.symbol);
				dataArr.set('period', this.interval);
				dataArr.set('size', 600);
				dataArr.set('ts', parseInt(new Date().getTime() / 1000));
				dataArr.set('type', this.type);

				var result = await klineApi(dataArr);
				// console.log(result)	
				if (result.success) {
					let kdata = [];
					result.data.map(item => {
						let data = {
							//差8个小时 8*3600*1000
							// time: (Number(item.id) * 1000)+8*3600*1000,
							time: (Number(item.id) * 1000),
							close: item.close,
							open: item.open,
							high: item.high,
							low: item.low,
							volume: item.amount
						}
						kdata.push(data)
					})
					this.startData = this.startData + this.lengsData + this.countDate + 1;
					if (flag=='init'){
						//初始化
						this.onLoadedCallback(kdata);
						this.startKline()
					}else{
						//加载更多
						// this.$emit('getValueDise', kdata)
						this.onRealtimeCallback(kdata)
					}
					
				}
			})
		},

		// // 获取推送数据
		// getKline() {
		// 	this.$nextTick(async () => {
		// 		var dataArr = new URLSearchParams();
		// 		dataArr.set('symbol', this.symbol);
		// 		dataArr.set('period', this.interval);
		// 		dataArr.set('size', 600);
		// 		dataArr.set('type', this.type);
		// 		dataArr.set('ts', this.currTimestamp);
		// 		var result = await klineApi(dataArr);
		// 		if (result.success) {
		// 			var obj = result.data;
		// 			obj.forEach((item, index) => {
		// 				let kdata = {
		// 					time: (item.id) * 1000,
		// 					open: item.open,
		// 					close: item.close,
		// 					high: item.high,
		// 					low: item.low,
		// 					volume: item.amount
		// 				};
		// 				this.$emit('getValueDise', kdata)
		// 				this.onRealtimeCallback(kdata)
		// 			})
		// 		}
		// 	})
		// },


		//k线订阅
		startKline() {
			let data = {
				"event": "sub",
				"type": "kline",
				"symbol": this.symbol + "_" + this.interval
			}
			console.log("订阅", data);
			this.$webSocket.addEvent(data)
			this.$webSocket.addDoOn("/kline", (session, message) => {
				let response = JSON.parse(message.dataAsString())
				// console.log(response);

				//判断是否当前币种
				if (response.symbol == this.symbol + "_" + this.interval) {
					let respData = JSON.parse(response.data)
					let klineData = {}
					klineData.time = respData.ts * 1000;
					klineData.open = respData.open
					klineData.close = respData.close
					klineData.high = respData.high
					klineData.low = respData.low
					klineData.volume = respData.vol
					this.$emit('getValueDise', klineData)
					this.onRealtimeCallback(klineData)
				}

			})


		}

	},

	mounted() {
		//加载K线图
		this.loadChart();
	},
	beforeDestroy() {

	}
}
</script>
<style>
#trade-view {
	margin: 20px 0px;
}
</style>
