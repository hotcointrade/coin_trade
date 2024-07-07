import os, shutil
import datetime

from pymongo import MongoClient
import pandas as pd

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


def unzip_kline_data():
    """
    解压文件
    """
    for root, dirs, files in os.walk("./data/spot/daily/klines/ADAUSDT/"):
        for file in files:
            if file.endswith(".zip"):
                file_path = os.path.join(root, file)
                # 处理文件路径，例如打印文件路径
                print(file_path)
                shutil.unpack_archive(file_path, extract_dir=root, format="zip")
                os.remove(file_path)


def start_import():
    """
    读取csv导入mongo
    :return:
    """
    for root, dirs, files in os.walk("./data/spot/daily"):
        for file in files:
            if file.endswith(".csv"):
                file_path = os.path.join(root, file)
                # 处理文件路径，例如打印文件路径
                print(file)
                split = file.split("-")
                symbol = split[0].replace("USDT", "/USDT")
                if "1mo" == split[1]:
                    interval = "1M"
                else:
                    interval = split[1]
                interval = binance_interval_enum[interval]
                import_mongo(file_path, symbol, interval)


def import_mongo(file_path, symbol, interval):
    """
    导入到mongodb
    :param file_path: 文件路径
    :param symbol: 交易对
    :param interval: 时间单位
    :return:
    """
    # 集合名称
    coll_name = mongo_coll.replace("$symbol$", symbol).replace("$period$", interval)
    collection = mongo_client[coll_name]
    df = pd.read_csv(file_path, sep='\t', header=None)

    kline_list = []
    for row in df.itertuples():
        data = row[1].split(",")
        print(data[0])
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
    # 解压压缩包
    unzip_kline_data()
    # 导入csv数据到Mongodb
    start_import()
