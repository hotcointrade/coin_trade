import time

from binance.spot import Spot
from pymongo import MongoClient
import datetime

client = MongoClient("127.0.0.1", 27017)  # 如果是本地连接host,port参数可以省略
mongo_client = client["coin"]
mongo_coll = "KDATA_$symbol$_$period$"

binance_interval_enum = {
    "1m": "1min",
    "5m": "5min",
    "15m": "15min",
    "30m": "30min",
    "1h": "1hour",
    "1d": "1day",
    "1w": "1week",
    "1M": "1mon",
}


def run(symbol_list, interval_list):
    """
    同步数据
    :param symbol_list: 交易对列表
    :param interval_list: 时间单位
    :return:
    """
    proxies = {'https': 'http://127.0.0.1:7890'}
    client = Spot(proxies=proxies)
    for symbol in symbol_list:
        for interval in interval_list:
            print("交易对：{},时间单位:{}".format(symbol, interval))
            # 获取数据
            response_kdata = client.klines(symbol=symbol, interval=interval, limit=1000)

            # 导入到mongodb
            import_mongo(response_kdata=response_kdata, symbol=symbol.replace("USDT", "/USDT"),
                         interval=binance_interval_enum.get(interval))

            time.sleep(2)


def import_mongo(response_kdata, symbol, interval):
    """
    导入到mongodb
    :param response_kdata: k线数据
    :param symbol: 交易对
    :param interval: 时间单位
    :return:
    """
    # 集合名称
    coll_name = mongo_coll.replace("$symbol$", symbol).replace("$period$", interval)
    collection = mongo_client[coll_name]

    kline_list = []
    for data in response_kdata:
        ts = int(data[0]) / 1000
        dt = datetime.datetime.fromtimestamp(ts)
        d = {
            '_id': int(ts),  # 兼容java写法命名成_id
            'ts': int(ts),
            'amount': data[5],
            'count': data[8],
            'open': data[1],
            'close': data[4],
            'low': data[3],
            'high': data[2],
            'vol': data[7],
            'date': dt.strftime('%Y-%m-%d %H:%M:%S')
        }
        kline_list.append(d)
    try:
        collection.insert_many(kline_list, ordered=False)
    except Exception as e:
        # 忽略错误
        pass


if __name__ == '__main__':
    symbol_list = ["BTCUSDT", "ETHUSDT", "TRXUSDT", "LTCUSDT", "BCHUSDT", "EOSUSDT", "ETCUSDT", "XRPUSDT", "VETUSDT",
                   "ADAUSDT",
                   "UNIUSDT", "SOLUSDT", "ICPUSDT", "XLMUSDT", "XMRUSDT", "FILUSDT", "HOTUSDT", ]
    interval_list = ["1m", "5m", "15m", "30m", "1h", "1d", "1w", "1M"]
    run(symbol_list, interval_list)
