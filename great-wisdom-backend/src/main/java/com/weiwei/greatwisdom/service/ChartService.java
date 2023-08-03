package com.weiwei.greatwisdom.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.weiwei.greatwisdom.model.dto.chart.ChartQueryRequest;
import com.weiwei.greatwisdom.model.dto.post.PostQueryRequest;
import com.weiwei.greatwisdom.model.entity.Chart;
import com.weiwei.greatwisdom.model.entity.Post;

import java.util.List;

/**
* @author 86199
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2023-07-04 12:06:24
*/
public interface ChartService extends IService<Chart> {

    QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);

    List<Chart> getAllCharts();

}
