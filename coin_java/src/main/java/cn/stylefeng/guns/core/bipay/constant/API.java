package cn.stylefeng.guns.core.bipay.constant;

public class API {
    
    /**
     * 生成地址
     * @link <a href="https://www.uduncloud.com/geteway-interface?index=1">...</a>
     */
    public static String CREATE_ADDRESS = "/mch/address/create";
    
    /**
     * 提幣申請
     * @link <a href="https://www.uduncloud.com/geteway-interface?index=2">...</a>
     */
    public static String WITHDRAW = "/mch/withdraw";
    
    /**
     * 貌似弃用
     */
    public static String TRANSACTION = "/mch/transaction";
    

    public static String AUTO_WITHDRAW = "/mch/withdraw/proxypay";
    
    /**
     * 獲取商戶支持的幣種信息
     * @link  <a href="https://www.uduncloud.com/geteway-interface?index=5">...</a>
     */
    public static String SUPPORT_COIN = "/mch/support-coins";
    public static String CHECK_PROXY = "/mch/check-proxy";
    
    /**
     * 校驗地址是否存在
     * @link <a href="https://www.uduncloud.com/geteway-interface?index=6">...</a>
     */
    public static String CHECK_ADDRESS = "/mch/check/address";
    public static String CREATE_BATCH_ADDRESS = "/mch/address/create/batch";
}
