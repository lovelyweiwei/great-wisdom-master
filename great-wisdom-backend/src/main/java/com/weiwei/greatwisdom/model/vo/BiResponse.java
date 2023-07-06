package com.weiwei.greatwisdom.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Bi 的返回结果
 * @Author weiwei
 * @Date 2023/7/4 20:51
 * @Version 1.0
 */
@Data
public class BiResponse implements Serializable {

    private String genChart;

    private String genResult;

    private Long chartId;

}
