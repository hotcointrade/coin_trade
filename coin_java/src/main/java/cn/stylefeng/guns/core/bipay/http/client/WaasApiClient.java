package cn.stylefeng.guns.core.bipay.http.client;

import cn.hutool.core.util.IdUtil;
import com.sinohope.request.*;
import com.sinohope.response.*;
import com.sinohope.response.common.PageData;
import com.sinohope.response.common.ResultData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sinohope.client.ClientFactory;
import com.sinohope.client.MpcNodeClient;
import com.sinohope.client.SeniorClient;
import com.sinohope.client.TransactionClient;
import com.sinohope.client.WalletAndAddressClient;
import com.sinohope.sign.ECDSA;

import java.util.ArrayList;
import java.util.List;

import static com.sinohope.sign.ECDSA.SECP256R1;

@Slf4j
@Data
@Component
public class WaasApiClient {

    public static ECDSA ecdsa;

    @Value("${waas.publicKey}")
    private String publicKey;

    @Value("${waas.privateKey}")
    private String privateKey;

    private static String baseUrl = null;

    // 金库id
    @Value("${waas.vaultId}")
    private String vaultId;

    // 钱包前缀
    @Value("${waas.website}")
    private String website;

    public WaasApiClient() throws Exception {

    }

    static {
        try {
            baseUrl = "https://api.sinohope.com";
            ecdsa = new ECDSA(SECP256R1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构造钱包和地址相关客户端
     *
     * @param publicKey
     * @param privateKey
     * @return
     */
    public static WalletAndAddressClient getWalletAndAddressClient(String publicKey, String privateKey) {
        return ClientFactory.newInstance(ecdsa, baseUrl, publicKey, privateKey).newWalletAndAddressClient();
    }

    /**
     * 构造交易客户端
     *
     * @param publicKey
     * @param privateKey
     * @return
     */

    public static TransactionClient getTransactionClient(String publicKey, String privateKey) {
        return ClientFactory.newInstance(ecdsa, baseUrl, publicKey, privateKey).newTransactionClient();
    }

    /**
     * 构造高级功能客户端
     *
     * @param publicKey
     * @param privateKey
     * @return
     */
    public static SeniorClient getSeniorClient(String publicKey, String privateKey) {
        return ClientFactory.newInstance(ecdsa, baseUrl, publicKey, privateKey).newSeniorClient();
    }

    /**
     * 构造mpcNode客户端
     *
     * @param publicKey
     * @param privateKey
     * @return
     */
    public static MpcNodeClient getMpcNodeClient(String publicKey, String privateKey) {
        return ClientFactory.newInstance(ecdsa, baseUrl, publicKey, privateKey).newMpcNodeClient();
    }

    /**
     * 查询系统支持链
     */
    public  ResultData<List<WaasChainVO>> getSupportedChains() {
        WalletAndAddressClient walletAndAddressClient = WaasApiClient.getWalletAndAddressClient(publicKey, privateKey);
        return walletAndAddressClient.getSupportedChains();
    }

    /**
     * 查询链支持的币种列表
     */
    public  ResultData<List<WassCoinDTO>> getSupportedCoins(String chainSymbol) {
        WalletAndAddressClient walletAndAddressClient = WaasApiClient.getWalletAndAddressClient(publicKey, privateKey);
        WaasChainParam waasChainParam = new WaasChainParam();
        waasChainParam.setVaultId(vaultId);
        waasChainParam.setChainSymbol(chainSymbol);
        return walletAndAddressClient.getSupportedCoins(waasChainParam);
    }

    /**
     * 查询金库列表
     */
    public  ResultData<List<ApiWaasVaultInfo>> queryVaults() {
        WalletAndAddressClient walletAndAddressClient = WaasApiClient.getWalletAndAddressClient(publicKey, privateKey);
        return walletAndAddressClient.queryVaults();
    }

    /**
     * 生成钱包
     *
     * @return
     */
    public  ResultData<List<ApiWaasWalletInfoVO>> getBatchWallet(String memberId) {
        WalletAndAddressClient walletAndAddressClient = WaasApiClient.getWalletAndAddressClient(publicKey, privateKey);
        WaasCreateBatchWalletParam waasCreateBatchWalletParam = new WaasCreateBatchWalletParam();
        waasCreateBatchWalletParam.setVaultId(vaultId);
        waasCreateBatchWalletParam.setRequestId(IdUtil.simpleUUID());
        List<WaasCreateWalletInfo> walletInfos = new ArrayList<>();
        WaasCreateWalletInfo walletInfo = new WaasCreateWalletInfo();
        walletInfo.setWalletName("wallet_" + website + ":" + memberId + "_" + IdUtil.simpleUUID());
        walletInfos.add(walletInfo);
        waasCreateBatchWalletParam.setWalletInfos(walletInfos);
        return walletAndAddressClient.createBatchWallet(waasCreateBatchWalletParam);
    }


    /**
     * 查询钱包列表
     *
     * @return
     */
    public  ResultData<PageData<ApiWaasWalletInfoVO>> listWallets() {
        WalletAndAddressClient walletAndAddressClient = WaasApiClient.getWalletAndAddressClient(publicKey, privateKey);
        WaasListWalletsParam waasListWalletsParam = new WaasListWalletsParam();
        waasListWalletsParam.setVaultId(vaultId);
        return walletAndAddressClient.listWallets(waasListWalletsParam);
    }

    /**
     * 查询链地址
     *
     * @return
     */
    public  ResultData<PageData<WaasAddressInfo>> listAddresses(String walletId, String chainSymbol) {
        WalletAndAddressClient walletAndAddressClient = WaasApiClient.getWalletAndAddressClient(publicKey, privateKey);
        WaasListAddressesParam waasListAddressesParam = new WaasListAddressesParam();
        waasListAddressesParam.setVaultId(vaultId);
        waasListAddressesParam.setWalletId(walletId);
        waasListAddressesParam.setChainSymbol(chainSymbol);
        return walletAndAddressClient.listAddresses(waasListAddressesParam);
    }

    /**
     * 生成链地址
     *
     * @return
     */
    public  ResultData<List<WaasAddressVo>> generateChainAddresses(String walletId, String chainSymbol) {
        WalletAndAddressClient walletAndAddressClient = WaasApiClient.getWalletAndAddressClient(publicKey, privateKey);
        WaasGenerateChainAddressParam waasGenerateChainAddressParam = new WaasGenerateChainAddressParam();
        waasGenerateChainAddressParam.setRequestId(IdUtil.simpleUUID());
        waasGenerateChainAddressParam.setVaultId(vaultId);
        waasGenerateChainAddressParam.setWalletId(walletId);
        waasGenerateChainAddressParam.setChainSymbol(chainSymbol);
        return walletAndAddressClient.generateChainAddresses(waasGenerateChainAddressParam);
    }


    /**
     * 查询指定钱包下已添加地址的链及其首个地址信息
     *
     * @param walletId
     * @return
     */
    public  ResultData<List<WaasListAddedChainsDTO>> listAddedChains(String walletId) {
        WalletAndAddressClient walletAndAddressClient = WaasApiClient.getWalletAndAddressClient(publicKey, privateKey);
        WaasListAddedChainsParam waasListAddedChainsParam = new WaasListAddedChainsParam();
        waasListAddedChainsParam.setVaultId(vaultId);
        waasListAddedChainsParam.setWalletId(walletId);
        return walletAndAddressClient.listAddedChains(waasListAddedChainsParam);
    }

    /**
     * 查询地址余额信息
     *
     * @param assetId
     * @param address
     * @return
     */
    public  ResultData<WaasGetWalletBalanceDTO> getAddressBalance(String assetId, String address) {
        WalletAndAddressClient walletAndAddressClient = WaasApiClient.getWalletAndAddressClient(publicKey, privateKey);
        WaasGetAddressBalanceParam waasGetAddressBalanceParam = new WaasGetAddressBalanceParam();
        waasGetAddressBalanceParam.setAssetId(assetId);
        waasGetAddressBalanceParam.setAddress(address);
        return walletAndAddressClient.getAddressBalance(waasGetAddressBalanceParam);
    }
}
