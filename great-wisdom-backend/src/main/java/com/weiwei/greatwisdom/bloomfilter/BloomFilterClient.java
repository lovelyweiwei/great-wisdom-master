package com.weiwei.greatwisdom.bloomfilter;

import com.google.common.hash.Funnels;
import com.google.common.hash.BloomFilter;
import com.weiwei.greatwisdom.model.entity.Chart;
import com.weiwei.greatwisdom.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author weiwei
 * @Date 2023/8/3 19:52
 * @Version 1.0
 */
@Component
public class BloomFilterClient {
    private BloomFilter<Long> bloomFilter;

    public BloomFilterClient() {
        int expectedElements = 10000; // 预期元素数量
        double falsePositiveRate = 0.01; // 误判率

        // 初始化布隆过滤器
        bloomFilter = BloomFilter.create(Funnels.longFunnel(), expectedElements, falsePositiveRate);

    }

    public void loadData(ChartService chartService){
        // 加载已存在的数据
        List<Chart> list = chartService.getAllCharts();
        for (Chart chart : list) {
            bloomFilter.put(chart.getId());
        }
    }

    public boolean checkIfExists(long element) {
        // 判断元素是否可能存在于布隆过滤器中
        return bloomFilter.mightContain(element);
    }

    public void addElement(long element) {
        // 将元素添加到布隆过滤器中
        bloomFilter.put(element);
    }
}
