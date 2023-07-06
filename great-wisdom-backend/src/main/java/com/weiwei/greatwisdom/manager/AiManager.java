package com.weiwei.greatwisdom.manager;

import com.weiwei.greatwisdom.common.ErrorCode;
import com.weiwei.greatwisdom.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author weiwei
 * @Date 2023/7/4 19:54
 * @Version 1.0
 */
@Component
public class AiManager {

    @Resource
    private YuCongMingClient yuCongMingClient;

    public String doChat(Long modelId, String message) {
        DevChatRequest devChatRequest = new DevChatRequest();
        // 鱼聪明平台模型ID
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }
        return response.getData().getContent();
    }

}
