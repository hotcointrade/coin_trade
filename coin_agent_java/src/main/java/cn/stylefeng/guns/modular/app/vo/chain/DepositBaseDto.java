package cn.stylefeng.guns.modular.app.vo.chain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DepositBaseDto
{
    private String key;
    private String timeStamp;
    private String nonceStr;
    private String sign;
    private String memberCode;
    private Long id;
    private String coin;
    private String userId;
    private String fromAddress;
    private String toAddress;
    private String gases;
    private String amount;
    private String txHash;
    private Long blockIndex;
    private String blockHash;
    private Long tranIndex;
    private Long timeStart;
    private Long timeReceived;
    private String vout;

}
