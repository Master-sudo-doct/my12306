package org.opengoofy.index12306.biz.userservice.service;

import org.opengoofy.index12306.biz.userservice.dto.req.UserDeletionReqDTO;
import org.opengoofy.index12306.biz.userservice.dto.req.UserLoginReqDTO;
import org.opengoofy.index12306.biz.userservice.dto.req.UserRegisterReqDTO;
import org.opengoofy.index12306.biz.userservice.dto.resp.UserLoginRespDTO;
import org.opengoofy.index12306.biz.userservice.dto.resp.UserRegisterRespDTO;

public interface UserLoginService {

    UserRegisterRespDTO register(UserRegisterReqDTO requestParam);

    void deletion(UserDeletionReqDTO requestParam);

    boolean hasUsername(String username);

    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    UserLoginRespDTO checkLogin(String accessToken);

    void logout(String accessToken);
}
