package com.weiwei.greatwisdom.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * 生成图表请求
 *
 */
@Data
public class GenChartByAiRequest implements Serializable {

    /**
     * 名称
     */
    private String chartName;

    /**
     * 目标
     */
    private String goal;

    /**
     * 图表类型
     */
    private String chartType;

    private static final long serialVersionUID = 1L;
}
