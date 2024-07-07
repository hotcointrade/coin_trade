package cn.stylefeng.guns.modular.app.vo.chain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class CoinPublicBaseTransaction
{
    /**
     * (id,BIGINT(20),not null)
     */
    private Long id;

    /**
     * (coin,VARCHAR(10), null)
     */
    private String coin;

    private String fromMemberCode;

    private String toMemberCode;

    /**
     * (type,TINYINT(4), null)1转入2转出
     */
    private Byte type;

    private Byte success;

    /**
     * (token,VARCHAR(256), null)
     */
    private String token;

    /**
     * (from_address,VARCHAR(256), null)
     */
    private String fromAddress;

    /**
     * (to_address,VARCHAR(256), null)
     */
    private String toAddress;

    /**
     * (gases,BIGINT(50), null)矿工费用
     */
    private String gases;

    /**
     * (amount,BIGINT(50), null)
     */
    private String amount;

    private String gasPrice;

    private BigInteger gasLimit;

    private String nonce;

    /**
     * (STATUS,TINYINT(4), null)1未处理 2已完成 3未发起已完成
     */
    private Byte status;

    /**
     * (tx_hash,VARCHAR(256), null)
     */
    private String txHash;

    /**
     * (block_index,BIGINT(20), null)
     */
    private Long blockIndex;

    /**
     * (block_hash,VARCHAR(256), null)
     */
    private String blockHash;

    /**
     * (tran_index,BIGINT(20), null)
     */
    private Long tranIndex;

    /**
     * (time_start,DATETIME, null)
     */
    private Long timeStart;

    private String message;

    /**
     * (time_received,DATETIME, null)
     */
    private Long timeReceived;


}
