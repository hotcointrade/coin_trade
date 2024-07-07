package cn.stylefeng.guns.modular.meta_data.job;

import cn.stylefeng.guns.modular.app.service.JobService;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.coin.entity.Swap;
import cn.stylefeng.guns.modular.mining.entity.MiningOrder;
import cn.stylefeng.guns.modular.mining.service.MiningOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//@Logger
public class JobBase {


    @Autowired
    private JobService jobService;

    @Autowired
    private MiningOrderService miningOrderService;

    @Autowired
    public JobBase(JobService jobService)
    {
        this.jobService=jobService;
    }

    //超时订单自动取消处理
    @Scheduled(cron = "0/3 * * * * ?")
    public void timeOutOrder(){
        jobService.timeOutOrder();
    }


    /**
     * 合约止盈止损监听
     */
    @Scheduled(fixedRate = 1000)
    public void stopPlUSDT() throws Exception{
        jobService.stopPl("USDT");


    }
    @Scheduled(fixedRate = 1000)
    public void Pl() throws Exception{
        jobService.pl();


    }

    /**
     * 爆仓监听 USDT
     */
    @Scheduled(fixedRate = 1000)
    public void boom1() throws Exception
    {
        List<Swap> s = F.me().getSwaps("Y");
        for (Swap swap : s) {
            jobService.boom("USDT");
        }
    }


    /**
     * 合约账户数据实时刷新
     */
    @Scheduled(fixedRate = 1000)
    public void contractUpdate1()
    {
        jobService.contractUpdateCoin("USDT");
    }


    /**
     * 设置5分钟一次大更新，防止更新中断问题
     */
    @Scheduled(fixedRate = 6000*5)
    public void normalUpdateContract(){
        jobService.upContract();
    }

    /**
     * 用户订单总盈亏计算 - 盈亏监听
     */
    @Scheduled(fixedRate = 1000)
    public void plTotal1(){
        jobService.plTotal("USDT");
    }


    /**
     * 合约 委托转持仓 监听
     */
    @Scheduled(fixedRate = 1000)
    public void entrust() throws Exception{
        jobService.entrust();
    }

    /**
     * 资产为负数信息刷新为01
     */
    @Scheduled(fixedRate = 10000)
    public void finZero(){
        jobService.finZero();
    }

    /**
     * 矿机
     */
    //@Scheduled(cron = "59 59 23 * * *" )
    @Scheduled(fixedRate = 6000)
    public void miningCheck(){
        //System.out.println("nihoa");
        MiningOrder qu = new MiningOrder();
        qu.setMiningStatus("1");
        qu.setDel("N");
        List<MiningOrder> list = miningOrderService.list(new QueryWrapper<>(qu));
        if(list!=null && list.size()>0){
            for (MiningOrder miningOrder : list) {

                jobService.miningCheck(miningOrder);
            }
        }

    }



}
