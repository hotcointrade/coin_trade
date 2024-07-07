package cn.stylefeng.guns.modular.base.state;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
public class FailResponseData extends ResponseData {

    public FailResponseData() {
        super(true, DEFAULT_ERROR_CODE, "请求失败", (Object)null);
    }

    public FailResponseData(Object object) {
        super(true, DEFAULT_ERROR_CODE, "请求失败", object);
    }

    public FailResponseData(Integer code, String message, Object object) {
        super(true, code, message, object);
    }

}
