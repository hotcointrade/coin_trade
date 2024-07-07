package cn.stylefeng.guns.modular.app.controller.market;

/**
 * ApiException if api returns error.
 *
 * @Date 2018/1/14
 * @Time 16:02
 */

public class ApiException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 8196662756375909063L;

    final String errCode;

    public ApiException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
    }

    public ApiException(Exception e) {
        super(e);
        this.errCode = e.getClass().getName();
    }

    public String getErrCode() {
        return this.errCode;
    }

}
