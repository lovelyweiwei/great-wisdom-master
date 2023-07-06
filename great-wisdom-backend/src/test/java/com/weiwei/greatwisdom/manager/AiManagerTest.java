package com.weiwei.greatwisdom.manager;

import com.weiwei.greatwisdom.common.ErrorCode;
import com.weiwei.greatwisdom.exception.BusinessException;
import com.weiwei.greatwisdom.manager.AiManager;
import org.bouncycastle.asn1.dvcs.TargetEtcChain;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Author weiwei
 * @Date 2023/7/4 20:00
 * @Version 1.0
 */
@SpringBootTest
public class AiManagerTest {

    @Resource
    private AiManager aiManager;

    @Test
    void doChat(){
        String result = aiManager.doChat(1659171950288818178L, "分析需求：\n" +
                "分析网站用户的增长情况\n" +
                "原始数据：\n" +
                "日期，用户数\n" +
                "1号，10\n" +
                "2号，20\n" +
                "3号，30");
        System.out.println(result);

        ////拆分结果
        //String[] splits = result.split("【【【【【");
        //if (splits.length < 3) {
        //    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 生成错误");
        //}
        //String test1 = splits[0];
        //String analyzeResult = splits[1];
        //String test2 = splits[2];
        //System.out.println(test1);
        //System.out.println(analyzeResult);
        //System.out.println(test2);
    }
}
