package cn.stylefeng.guns.modular.bulletin.model;

import lombok.Data;

/**
 * 开户行dto
 */
@Data
public class CompanyDto {

    //公司名
    private String companyName;
    //银行名
    private String bankName;

    //支行
    private String branchName;

    //账号
    private String account;

}
