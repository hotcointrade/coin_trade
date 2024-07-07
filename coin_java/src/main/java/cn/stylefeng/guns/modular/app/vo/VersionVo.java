package cn.stylefeng.guns.modular.app.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VersionVo {

    @NotBlank(message = "版本为空")
    private String version;

    @NotBlank(message = "版本类型为空")
    private String type;
}
