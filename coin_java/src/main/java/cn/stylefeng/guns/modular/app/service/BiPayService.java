package cn.stylefeng.guns.modular.app.service;

import cn.stylefeng.guns.core.bipay.constant.CoinType;
import cn.stylefeng.guns.core.bipay.entity.Address;
import cn.stylefeng.guns.core.bipay.entity.SupportCoin;
import cn.stylefeng.guns.core.bipay.entity.Transaction;
import cn.stylefeng.guns.core.bipay.http.ResponseMessage;
import cn.stylefeng.guns.core.bipay.http.client.BiPayClient;
import cn.stylefeng.guns.modular.base.state.F;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BiPayService {

    @Resource
    private BiPayClient biPayClient;


    /**
     * 创建币种地址
     *
     * @param coinCode
     * @param alias
     * @param walletId
     * @return
     */
    public Address createCoinAddress(String coinCode, String alias, String walletId) {
//        String callbackUrl = F.me().getSysConfigValueByCode("CREATE_ADDRESS_CALLBACK_URL");
        try {
            ResponseMessage<Address> resp = biPayClient.createCoinAddress(coinCode,  alias, walletId);
            return resp.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提币
     * @param orderId
     * @param amount
     * @param coinCode
     * @param subCoinType
     * @param address
     * @param memo
     * @return
     */
    public ResponseMessage<String> transfer(String orderId, BigDecimal amount, String coinCode, String subCoinType, String address, String memo) {
//        String callbackUrl = F.me().getSysConfigValueByCode("CREATE_ADDRESS_CALLBACK_URL");
        try {
            ResponseMessage<String> resp = biPayClient.transfer(orderId, amount, coinCode, subCoinType, address, memo);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseMessage.error("提交转币失败");
    }

    /**
     * 代付
     * @param orderId
     * @param amount
     * @param coinType
     * @param subCoinType
     * @param address
     * @param memo
     * @return
     */
    public ResponseMessage<String> autoTransfer(String orderId, BigDecimal amount, CoinType coinType, String subCoinType, String address, String memo,Long memberId) {
//        String callbackUrl =F.me().getSysConfigValueByCode("CREATE_ADDRESS_CALLBACK_URL")+"/"+memberId;
        try {
            ResponseMessage<String> resp = biPayClient.autoTransfer(orderId, amount, coinType.getCode(), subCoinType, address, memo);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseMessage.error("提交转币失败");
    }

    public List<Transaction> queryTransaction() throws Exception {
        return biPayClient.queryTransaction("", "", "", null, "", "", "");
    }

    /**
     * 校验地址合法性
     * @param mainCoinType
     * @param address
     * @return
     * @throws Exception
     */
    public boolean checkAddress(String mainCoinType, String address) throws Exception {
        return biPayClient.checkAddress(mainCoinType, address);
    }

    /**
     * 查询支持币种
     * @param showBalance
     * @return
     * @throws Exception
     */
    public List<SupportCoin> getSupportCoin(Boolean showBalance) throws Exception {
        return biPayClient.getSupportCoin(showBalance);
    }
    
    /**
     * 检查前面
     */
}
