package com.weiwei.greatwisdom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weiwei.greatwisdom.model.entity.Chart;

import java.util.List;
import java.util.Map;

/**
* @author 86199
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2023-07-04 12:06:24
* @Entity com.weiwei.greatwisdom.model.entity.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {

    List<Map<String, Object>> queryChartData(String querySql);
}




