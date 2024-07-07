package cn.stylefeng.guns.modular.extension.ida;

import lombok.Data;
import lombok.experimental.Accessors;

/***
 * 收银台 请求
 */
@Data
@Accessors(chain = true)
public class CashDto
{
    /**
     * pickupUrl	是	string	付款客户成功后的页面
     * receiveUrl	是	string	服务器接受支 付结果的后台 地址 ，回调CRM的地址
     * signType	是	string	签名类型，只支持MD5，默认值：MD5
     * orderNo	是	string	订单号
     * type	是	String	货币类型：法币：legal,数字币:digit
     * orderAmount	是	float	订单金额 ,type为legal这个字段不能为0
     * amount	是	float	数字币数量 ,type为digit 这个字段不能为0
     * orderCurrency	是	string	法币渠道法币类型
     * coin	是	string	数字币币种
     * customerId	是	string	客户交易者账号 32 不为空 如 83430099
     * sign	是	string	签名 sign=MD5(pickupUrl+receiveUrl+signType+orderNo+orderAmount+orderCurrency+customerId+MD5 key)注意：不包含crmNo参数
     * crmNo	是	string	商户识别号 8 可以为空，如果为空则从订单号识别 TW001478
     */
    private String pickupUrl ;
    private String receiveUrl ;
    private String orderNo ;
    private String orderAmount ;
    private String amount ;
    private String orderCurrency;
    private String customerId ;
    //商户号
    private String crmNo ;
    private String coin ;
    //key
    private String sign ;

    //请求url
    private String url;
}
