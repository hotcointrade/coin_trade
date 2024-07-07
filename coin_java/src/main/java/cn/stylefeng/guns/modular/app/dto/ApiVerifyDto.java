package cn.stylefeng.guns.modular.app.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 实名认证DTO
 */
@Data
public class ApiVerifyDto
{
    /**
     * 真实姓名
     */
    private String name;

    /**
     * 身份证号
     */
    @NotBlank
    private String idCard;

    /**
     * 姓
     */
    //@NotBlank
    private String fistName;

    /**
     * 名
     */
    //@NotBlank
    private String lastName;



    /***
     * 证件类型
     */
    @NotBlank
    private String type;


    /**
     * 正面
     */
    @NotBlank
    private String frontImg;

    /**
     * 反面
     */
    @NotBlank
    private String backImg;
}
