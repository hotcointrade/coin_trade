package cn.stylefeng.guns.modular.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * 异步接口配置
 */
@Configuration
public class AsyncConfig
{
    //设置ThreadPoolExecutor的最大线程池大小。
    private static final int MAX_POOL_SIZE=4;
    //设置ThreadPoolExecutor的核心线程池大小
    private static final int CORE_POOL_SIZE=2;

    @Bean("asyncTaskExecutor")
    public AsyncTaskExecutor asyncTaskExecutor(){
        ThreadPoolTaskExecutor asyncExecutor=new ThreadPoolTaskExecutor();
        asyncExecutor.setMaxPoolSize(MAX_POOL_SIZE);//设置ThreadPoolExecutor的最大线程池大小
        asyncExecutor.setCorePoolSize(CORE_POOL_SIZE);//设置ThreadPoolExecutor的核心线程池大小
        asyncExecutor.setThreadNamePrefix("async-thread-pool-");//线程名称前缀
        asyncExecutor.initialize();//初始化
        return asyncExecutor;
    }


    @Bean("currencyTask")
    public AsyncTaskExecutor currencyTask(){
        ThreadPoolTaskExecutor asyncExecutor=new ThreadPoolTaskExecutor();
        asyncExecutor.setMaxPoolSize(MAX_POOL_SIZE);//设置ThreadPoolExecutor的最大线程池大小
        asyncExecutor.setCorePoolSize(CORE_POOL_SIZE);//设置ThreadPoolExecutor的核心线程池大小
        asyncExecutor.setThreadNamePrefix("currency-pool-");//线程名称前缀
        asyncExecutor.initialize();//初始化
        return asyncExecutor;
    }



    @Bean("boomTask")
    public AsyncTaskExecutor boomTask(){
        ThreadPoolTaskExecutor asyncExecutor=new ThreadPoolTaskExecutor();
        asyncExecutor.setMaxPoolSize(MAX_POOL_SIZE);//设置ThreadPoolExecutor的最大线程池大小
        asyncExecutor.setCorePoolSize(CORE_POOL_SIZE);//设置ThreadPoolExecutor的核心线程池大小
        asyncExecutor.setThreadNamePrefix("boomTask-pool-");//线程名称前缀
        asyncExecutor.initialize();//初始化
        return asyncExecutor;
    }

    @Bean("stopPlTask")
    public AsyncTaskExecutor stopPlTask(){
        ThreadPoolTaskExecutor asyncExecutor=new ThreadPoolTaskExecutor();
        asyncExecutor.setMaxPoolSize(MAX_POOL_SIZE);//设置ThreadPoolExecutor的最大线程池大小
        asyncExecutor.setCorePoolSize(CORE_POOL_SIZE);//设置ThreadPoolExecutor的核心线程池大小
        asyncExecutor.setThreadNamePrefix("stopPlTask-pool-");//线程名称前缀
        asyncExecutor.initialize();//初始化
        return asyncExecutor;
    }




}
