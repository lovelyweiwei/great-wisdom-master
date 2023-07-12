package com.weiwei.greatwisdom.bizmq;

import com.rabbitmq.client.Channel;
import com.weiwei.greatwisdom.common.ErrorCode;
import com.weiwei.greatwisdom.constant.BIConstant;
import com.weiwei.greatwisdom.exception.BusinessException;
import com.weiwei.greatwisdom.manager.AiManager;
import com.weiwei.greatwisdom.model.entity.Chart;
import com.weiwei.greatwisdom.model.enums.ChartStatusEnum;
import com.weiwei.greatwisdom.service.ChartService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.BaseNumberUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.*;

/**
 * bi 消费者
 * @Author weiwei
 * @Date 2023/7/6 23:56
 * @Version 1.0
 */
@Component
@Slf4j
public class BiMqMessageConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AiManager aiManager;

    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    private void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        if (StringUtils.isBlank(message)) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }
        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图表为空");
        }
        //更改状态 running
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setChartStatus(ChartStatusEnum.RUNNING.getValue());
        boolean b = chartService.updateById(updateChart);
        if (!b) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "更新图表·执行中状态·失败");
            return;
        }

        // 设置超时时间限制  100 s
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 执行耗时的方法
            //调用AI
            String chatResult = aiManager.doChat(BIConstant.BI_MODEL_ID, buildUserInput(chart));
            return chatResult;
        });

        String chatResult = null;
        try {
            chatResult = future.get(100, TimeUnit.SECONDS);
            // 处理方法的返回结果
        } catch (InterruptedException e) {
            // 处理中断异常
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "AI 生成中断");
        } catch (ExecutionException e) {
            // 处理方法执行异常
        } catch (TimeoutException e) {
            // 超时处理
            future.cancel(true);
            // 抛出超时异常或执行其他错误处理
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "AI 生成超时，请调整后重新尝试");
        }

        //拆分结果
        String[] splits = chatResult.split("【【【【【");
        if (splits.length < 3) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "AI 生成错误");
            return;
        }
        String analyzeChart = splits[1].trim();
        String analyzeResult = splits[2].trim();
        //更改状态 succeed
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setGenChart(analyzeChart);
        updateChartResult.setGenResult(analyzeResult);
        updateChartResult.setChartStatus(ChartStatusEnum.SUCCEED.getValue());
        boolean b2 = chartService.updateById(updateChartResult);
        if (!b2) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "更新图表·成功状态·失败");
        }
        //消息确认
        channel.basicAck(deliveryTag, false);
    }

    /**
     * 构建用户的输入信息
     *
     * @param chart
     * @return
     */
    public String buildUserInput(Chart chart) {
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String chartData = chart.getChartData();

        // 无需Prompt，直接调用现有模型
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");
        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(chartData).append("\n");
        return userInput.toString();
    }

    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setChartStatus(ChartStatusEnum.FAILED.getValue());
        updateChartResult.setExecMessage(execMessage);
        boolean b = chartService.updateById(updateChartResult);
        if (!b) {
            log.error("更新图表失败信息失败，" + chartId + "," + execMessage);
        }
    }
}
