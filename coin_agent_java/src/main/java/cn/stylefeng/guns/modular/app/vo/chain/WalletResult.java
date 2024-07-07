package cn.stylefeng.guns.modular.app.vo.chain;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 钱包返回结果
 */
@Data
@Accessors(chain = true)
public class WalletResult
{

    /**
     *
     */
    private String status;

    private String message;

    private String innerException;

    private List<String> data;

    private WithdrawDto withdrawDto;


}
