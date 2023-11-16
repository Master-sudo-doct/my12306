///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
package org.opengoofy.index12306.biz.userservice;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
//import org.opengoofy.index12306.biz.userservice.serialize.PhoneDesensitizationSerializer;
import org.opengoofy.index12306.biz.userservice.dto.req.UserLoginReqDTO;
import org.opengoofy.index12306.biz.userservice.dto.req.UserRegisterReqDTO;
import org.opengoofy.index12306.biz.userservice.dto.resp.UserRegisterRespDTO;
import org.opengoofy.index12306.biz.userservice.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
//
@SpringBootTest
@Slf4j
class UserServiceApplicationTests {
//
//    @Test
//    void contextLoads() {
//    }
    @Autowired
    UserLoginService userLoginService;
    @Test
    void test() throws IOException {
        UserLoginReqDTO userLoginReqDTO = new UserLoginReqDTO();
        userLoginReqDTO.setUsernameOrMailOrPhone("czh1");
        userLoginReqDTO.setPassword("123456");
        UserRegisterReqDTO userRegisterReqDTO = new UserRegisterReqDTO();
        userRegisterReqDTO.setUsername("czh");
        userRegisterReqDTO.setPassword("123456");
        boolean username = userLoginService.hasUsername("czh1");

//        UserRegisterRespDTO register = userLoginService.register(userRegisterReqDTO);
//        log.info("注册结果：{}", register);
        log.info("存在用户名：{}", username);
    }
}
