package cn.stylefeng.guns.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDataException extends RuntimeException
{

    private int code;
    private String msg;
    private Object data;

    public UpdateDataException(int code)
    {
        this.code = code;
    }

}
