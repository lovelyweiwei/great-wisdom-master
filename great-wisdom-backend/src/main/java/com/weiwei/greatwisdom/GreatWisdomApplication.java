package com.weiwei.greatwisdom;

import com.weiwei.greatwisdom.bloomfilter.BloomFilterClient;
import com.weiwei.greatwisdom.service.ChartService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

/**
 * 主类（项目启动入口）
 */
// todo 如需开启 Redis，须移除 exclude 中的内容
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("com.weiwei.greatwisdom.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class GreatWisdomApplication implements ApplicationRunner{

    @Resource
    private ChartService chartService;

    @Resource
    private BloomFilterClient bloomFilterClient;

    public static void main(String[] args) {
        SpringApplication.run(GreatWisdomApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        bloomFilterClient.loadData(chartService);
    }
}
