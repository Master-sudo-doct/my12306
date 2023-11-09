package org.opengoofy.index12306.biz.userservice.service;

import org.opengoofy.index12306.biz.userservice.dto.req.UserRegisterReqDTO;
import org.opengoofy.index12306.biz.userservice.dto.resp.UserLoginRespDTO;

public interface UserLoginService {

    UserLoginRespDTO register(UserRegisterReqDTO requestParam);
}
