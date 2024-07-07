package cn.stylefeng.guns.modular.meta_data.model;
import lombok.Data;

import java.io.Serializable;

/**
 * 参数数据传输bean
 */
@Data
public class ConfigDto  implements Serializable {

    private Long configId;

    private String code;

    private String value;

    private String name;

    private String status;

    private String remark;
}
