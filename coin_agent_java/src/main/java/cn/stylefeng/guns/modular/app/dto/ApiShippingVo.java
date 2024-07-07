package cn.stylefeng.guns.modular.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 收货地址
 */
@Data
public class ApiShippingVo {
    private Long shippingId;
    private String phone;
    private String name;
    private String address;
}
