package cn.stylefeng.guns.modular.bulletin.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**Data银行dto
 */
@Data
@Accessors(chain = true)
public class BankDto {

    private String code;
    private String name;
}
