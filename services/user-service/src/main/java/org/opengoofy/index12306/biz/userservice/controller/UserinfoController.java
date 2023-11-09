package org.opengoofy.index12306.biz.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.opengoofy.index12306.biz.userservice.dto.req.UserRegisterReqDTO;
import org.opengoofy.index12306.biz.userservice.dto.resp.UserLoginRespDTO;
import org.opengoofy.index12306.biz.userservice.service.UserLoginService;
import org.opengoofy.index12306.framework.starter.convention.result.Result;
import org.opengoofy.index12306.framework.starter.web.Results;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserinfoController {
    private final UserLoginService userLoginService;
    /**
     * 注册用户
     */
    public Result<UserLoginRespDTO> register(@RequestBody @Valid UserRegisterReqDTO requestParam){
        return Results.success(userLoginService.register(requestParam));
    }
}
