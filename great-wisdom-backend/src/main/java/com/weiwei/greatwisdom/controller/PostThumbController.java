package com.weiwei.greatwisdom.controller;

import com.weiwei.greatwisdom.common.BaseResponse;
import com.weiwei.greatwisdom.common.ErrorCode;
import com.weiwei.greatwisdom.common.ResultUtils;
import com.weiwei.greatwisdom.exception.BusinessException;
import com.weiwei.greatwisdom.model.dto.postthumb.PostThumbAddRequest;
import com.weiwei.greatwisdom.model.entity.User;
import com.weiwei.greatwisdom.service.PostThumbService;
import com.weiwei.greatwisdom.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 帖子点赞接口
 *
 *
 */
@RestController
@RequestMapping("/post_thumb")
@Slf4j
@CrossOrigin
public class PostThumbController {

    @Resource
    private PostThumbService postThumbService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param postThumbAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody PostThumbAddRequest postThumbAddRequest,
            HttpServletRequest request) {
        if (postThumbAddRequest == null || postThumbAddRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long postId = postThumbAddRequest.getPostId();
        int result = postThumbService.doPostThumb(postId, loginUser);
        return ResultUtils.success(result);
    }

}
