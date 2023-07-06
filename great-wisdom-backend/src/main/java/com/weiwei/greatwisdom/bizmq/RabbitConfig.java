package com.weiwei.greatwisdom.bizmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq 配置文件
 *
 * @Author weiwei
 * @Date 2023/7/6 23:33
 * @Version 1.0
 */
@Configuration
public class RabbitConfig {

    @Bean
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange(BiMqConstant.BI_EXCHANGE_NAME).build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(BiMqConstant.BI_QUEUE_NAME).build();
    }

    @Bean
    public Binding binding(DirectExchange directExchange, Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with(BiMqConstant.BI_ROUTING_KEY);
    }

}
