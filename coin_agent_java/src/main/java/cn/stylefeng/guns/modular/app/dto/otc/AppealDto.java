package cn.stylefeng.guns.modular.app.dto.otc;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 申诉dto
 *
 */
@Data
public class AppealDto
{
    @NotBlank
    private String orderNo;

    @NotBlank
    private String content;

    @NotBlank
    private String img;
}
