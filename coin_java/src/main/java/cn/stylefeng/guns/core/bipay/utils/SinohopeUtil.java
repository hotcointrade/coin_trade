package cn.stylefeng.guns.core.bipay.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.stylefeng.guns.core.bipay.http.client.WaasApiClient;
import cn.stylefeng.guns.modular.app.controller.market.Kline;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import com.sinohope.request.WaasNotifyReqParam;
import com.sinohope.response.ApiWaasVaultInfo;
import com.sinohope.response.ApiWaasWalletInfoVO;
import com.sinohope.response.WaasChainVO;
import com.sinohope.response.common.ResultData;
import org.bouncycastle.util.encoders.Hex;
import com.sinohope.sign.ECDSA;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.KeyPair;
import java.util.List;

import static com.sinohope.sign.ECDSA.SECP256R1;

public class SinohopeUtil {


    public static void main(String[] args) throws Exception {
        WaasApiClient waasApiClient = new WaasApiClient();
//        ResultData<List<WaasChainVO>> supportedChains = waasApiClient.getSupportedChains();
//        System.out.println(supportedChains);
//        System.out.println(waasApiClient.getSupportedCoins("TRON"));
//        ResultData<List<ApiWaasVaultInfo>> listResultData = waasApiClient.queryVaults();
//        System.out.println(listResultData);
//        ResultData<List<ApiWaasWalletInfoVO>> batchWallet = waasApiClient.getBatchWallet("test");
//        System.out.println(batchWallet);
//        System.out.println(waasApiClient.listWallets());
//        System.out.println(waasApiClient.generateChainAddresses("548046374931973","ETH"));
//        System.out.println(waasApiClient.listAddedChains("548032638963845"));
//        System.out.println(WaasApiClient.getAddressBalance("ETH_SEPOLIA","0xadeb8bc5e8d7f0e2fcc3ee84f7f9913375a600a3"));
//        System.out.println(WaasApiClient.getAddressBalance("USDT_SEPOLIA","0xadeb8bc5e8d7f0e2fcc3ee84f7f9913375a600a3"));
//        getPublicKeyAndPrivateKey();

//        WaasNotifyReqParam waasNotifyReqParam = new WaasNotifyReqParam();
//        waasNotifyReqParam.setRequestType(1);
//        waasNotifyReqParam.setRequestId("545487770584901");
//        waasNotifyReqParam.setRequestDetail("{\"blockHash\":\"0x38d0ecf1d0669a86757a48ead0e26bd2eb6aeda05efda12cb3621ea3c9a68f41\",\"walletId\":\"544837174218885\",\"note\":\"\",\"amount\":\"10000000000000000\",\"chainSymbol\":\"SEPOLIA\",\"feeAssetDecimal\":18,\"fee\":\"0\",\"toTag\":\"\",\"sinoId\":\"0\",\"nonce\":0,\"gasLimit\":\"0\",\"feeDecimal\":18,\"assetId\":\"ETH_SEPOLIA\",\"from\":\"0x9e65145d8dd51fc8be899df7a4d2c2a16ac56498\",\"feeAsset\":\"ETH_SEPOLIA\",\"txDirection\":1,\"to\":\"0xadeb8bc5e8d7f0e2fcc3ee84f7f9913375a600a3\",\"state\":10,\"apiRequestId\":\"\",\"decimal\":18,\"txHash\":\"0x4453259d1a90e7637bdd3d7a1df47ccebdb4aa37cfbeeccb4e86728fc7ddb422\",\"usedFee\":\"31509627681000\",\"confirmNumber\":0,\"gasPrice\":\"0\"}");
//        String requestDetail = waasNotifyReqParam.getRequestDetail();
//        JSONObject jsonObject = JSONUtil.parseObj(requestDetail);
//        System.out.println(jsonObject.getStr("walletId"));
    }

    // 创建api密钥
    public static void  getPublicKeyAndPrivateKey() throws Exception {
        ECDSA ecdsa = new ECDSA(SECP256R1);
        KeyPair keyPair = ecdsa.generateKeyPair();
        String publicKey = Hex.toHexString(keyPair.getPublic().getEncoded());
        String privateKey = Hex.toHexString(keyPair.getPrivate().getEncoded());
        System.out.println("publicKey = " + publicKey);
        System.out.println("privateKey = " + privateKey);
    }

}
