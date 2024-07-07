package cn.stylefeng.guns.core.common.exception;

/**
 * @Description 业务异常的封装
 */
@SuppressWarnings("serial")
public class BussinessException extends RuntimeException{

    //友好提示的code码
    private int friendlyCode;

    //友好提示
    private String friendlyMsg;

    //业务异常跳转的页面
    private String urlPath;

    public BussinessException(BizExceptionEnum bizExceptionEnum){
        this.friendlyCode = bizExceptionEnum.getCode();
        this.friendlyMsg = bizExceptionEnum.getMessage();
        this.urlPath = bizExceptionEnum.getUrlPath();
    }

    public int getCode() {
        return friendlyCode;
    }

    public void setCode(int code) {
        this.friendlyCode = code;
    }

    public String getMessage() {
        return friendlyMsg;
    }

    public void setMessage(String message) {
        this.friendlyMsg = message;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

}