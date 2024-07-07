package cn.stylefeng.guns.modular.app.vo.chain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WithdrawBaseDto
{

    private String key;
    private String timeStamp;
    private String nonceStr;
    private String sign;
    private String memberCode;
    private String businessId;
    private String uid;
    private Long coinWithdrawId;
    private String fromAddress;
    private String toAddress;
    private String remark;
    private String amount;
    private String coin;
    private String gases;
    private String txHash;
    private Byte success;

}
