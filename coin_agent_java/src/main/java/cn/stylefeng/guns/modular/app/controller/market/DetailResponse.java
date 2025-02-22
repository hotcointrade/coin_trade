package cn.stylefeng.guns.modular.app.controller.market;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.AccessType;

import java.math.BigDecimal;
import java.util.List;

/**
 * 最近24小时行情
 */
@Data
public class DetailResponse<T>
{
    /**
     * status : ok
     * ch : market.ethusdt.detail.merged
     * ts : 1499225276950
     * tick : {"id":1499225271,"ts":1499225271000,"close":1885,"open":1960,"high":1985,"low":1856,"amount":81486.2926,"count":42122,"vol":1.57052744857082E8,"ask":[1885,21.8804],"bid":[1884,1.6702]}
     */

    private String status;
    private String ch;
    private long ts;
    public String errCode;
    public String errMsg;
    public T tick;

    public Object checkAndReturn() {
        if ("ok".equals(status)) {
            return tick;
        }
        throw new ApiException(errCode, errMsg);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getTick() {
        return tick;
    }

    public void setTick(T tick) {
        this.tick = tick;
    }



}
