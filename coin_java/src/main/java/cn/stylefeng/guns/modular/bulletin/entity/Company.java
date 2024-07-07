package cn.stylefeng.guns.modular.bulletin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 公司表
 */
@TableName("com_company")
@Data
@Accessors(chain =true)
public class Company extends BaseEntity {

    @TableId(value = "company_id",type = IdType.AUTO)
    private Long companyId;

    @TableField("company_code")
    private String companyCode;

    @TableField("company_name")
    private String companyName;

    @TableField("short_name")
    private String shortName;

    @TableField("en_name")
    private String enName;

    @TableField("en_short")
    private String enShort;

    @TableField("company_type")
    private String companyType;

    @TableField("ranges")
    private String ranges;

    @TableField("account")
    private String account;

    @TableField("account_name")
    private String accountName;

    @TableField("bank_name")
    private String bankName;

    @TableField("branch_name")
    private String branchName;

    @TableField("register_time")
    private Date registerTime;

    @TableField("legal")
    private String legal;

    @TableField("phone")
    private String phone;

    @TableField("address")
    private String address;

    @TableField("detail_address")
    private String detailAddress;

    @TableField("email")
    private String email;

    @TableField("license")
    private String license;

    @TableField("engraved")
    private String engraved;

    @TableField("logo")
    private String logo;

    @TableField("status")
    private String status;

    @TableField("del")
    private String del;

    @TableField("remark")
    private String remark;

}