
/**
 * 配置编译环境和线上环境之间的切换
 */

let baseUrl = '';
let sockeUrl = '';

/**
 启用本地开发模式
*/
if (process.env.NODE_ENV == 'development') {
    baseUrl = 'http://localhost:8185/api' //本地开发
    sockeUrl = "sd:ws://127.0.0.1:8602/";
} else if (process.env.NODE_ENV == 'production') {
    //first
    baseUrl = 'https://api.xxxx.com/api'//线上部署
    sockeUrl = "sd:wss://api-ws.cointrade.com/";


    //second
    // baseUrl = 'https://api.nyccomputer.com/api'//线上部署
    //   sockeUrl = "sd:wss://api-ws.cointrade.com/";


}

export {
    baseUrl, sockeUrl
}
