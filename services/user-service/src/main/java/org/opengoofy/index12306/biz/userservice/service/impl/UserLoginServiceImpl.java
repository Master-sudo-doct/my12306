package org.opengoofy.index12306.biz.userservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.opengoofy.index12306.biz.userservice.dao.entity.*;
import org.opengoofy.index12306.biz.userservice.dao.mapper.*;
import org.opengoofy.index12306.biz.userservice.dto.req.UserDeletionReqDTO;
import org.opengoofy.index12306.biz.userservice.dto.req.UserLoginReqDTO;
import org.opengoofy.index12306.biz.userservice.dto.req.UserRegisterReqDTO;
import org.opengoofy.index12306.biz.userservice.dto.resp.UserLoginRespDTO;
import org.opengoofy.index12306.biz.userservice.dto.resp.UserRegisterRespDTO;
import org.opengoofy.index12306.biz.userservice.service.UserLoginService;
//import org.opengoofy.index12306.biz.userservice.service.UserService;
import org.opengoofy.index12306.framework.starter.cache.DistributedCache;
import org.opengoofy.index12306.framework.starter.common.toolkit.BeanUtil;
import org.opengoofy.index12306.framework.starter.convention.exception.ClientException;
import org.opengoofy.index12306.framework.starter.convention.exception.ServiceException;
import org.opengoofy.index12306.framework.starter.designpattern.chain.AbstractChainContext;
import org.opengoofy.index12306.frameworks.starter.user.core.UserContext;
import org.opengoofy.index12306.frameworks.starter.user.core.UserInfoDTO;
import org.opengoofy.index12306.frameworks.starter.user.toolkit.JWTUtil;
import org.redisson.api.RLock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.concurrent.TimeUnit;

import static org.opengoofy.index12306.biz.userservice.common.constant.RedisKeyConstant.*;
import static org.opengoofy.index12306.biz.userservice.common.enums.UserRegisterErrorCodeEnum.*;
import static org.opengoofy.index12306.biz.userservice.toolkit.UserReuseUtil.hashShardingIdx;
import static org.opengoofy.index12306.framework.starter.convention.errorcode.BaseErrorCode.USER_NAME_VERIFY_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {
//    private final UserService userService;
    private final UserMapper userMapper;
    private final UserReuseMapper userReuseMapper;
    private final UserDeletionMapper userDeletionMapper;
    private final UserPhoneMapper userPhoneMapper;
    private final UserMailMapper userMailMapper;
    private final AbstractChainContext<UserRegisterReqDTO> abstractChainContext;
    private final DistributedCache distributedCache;
    //    private final RedissonClient redissonClient;
//    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        //TODO 责任链模式校验用户名、身份证名、手机号格式等
        if (hasUsername(requestParam.getUsername())) {
            throw new ClientException(HAS_USERNAME_NOTNULL);
        }
        int insert = userMapper.insert(BeanUtil.convert(requestParam, UserDO.class));
        if (insert < 1) {
            throw new ServiceException(USER_REGISTER_FAIL);
        }
        return BeanUtil.convert(requestParam, UserRegisterRespDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletion(UserDeletionReqDTO requestParam) {
//        String username = UserContext.getUserName();
//        if (!UserContext.getUserName().equals(username)) {
//            log.error("注销用户与当前登录用户不一致，需上报风控检查");
//            throw new ClientException("注销用户与当前登录用户不一致");
//        }
//        RLock lock = redissonClient.getLock(USER_DELETION + username);
//        boolean tryLock = lock.tryLock();
//        if (!tryLock) {
//            throw new ServiceException(USER_DELETION_FAIL);
//        }
//        try {
//            UserQueryRespDTO userQueryRespDTO = userService.queryUserByUsername(username);
//            UserDeletionDO userDeletionDO = BeanUtil.convert(userQueryRespDTO, UserDeletionDO.class);
//            userDeletionMapper.insert(userDeletionDO);
//            UserDO userDO = new UserDO();
//            userDO.setUsername(username);
//            userDO.setDeletionTime(System.currentTimeMillis());
//            userMapper.deletionUser(userDO);
//            UserPhoneDO userPhoneDO = UserPhoneDO.builder()
//                    .phone(userQueryRespDTO.getPhone())
//                    .deletionTime(System.currentTimeMillis())
//                    .build();
//            userPhoneMapper.deletionUser(userPhoneDO);
//            if (StrUtil.isNotBlank(userQueryRespDTO.getMail())) {
//                UserMailDO userMailDO = UserMailDO.builder()
//                        .mail(userQueryRespDTO.getMail())
//                        .deletionTime(System.currentTimeMillis())
//                        .build();
//                userMailMapper.deletionUser(userMailDO);
//            }
//            distributedCache.delete(UserContext.getUserToken());
//            userReuseMapper.insert(new UserReuseDO(username));
//            StringRedisTemplate instance = (StringRedisTemplate) distributedCache.getInstance();
//            instance.opsForSet().add(USER_REGISTER_REUSE_SHARDING + hashShardingIdx(username), username);
//        } finally {
//            lock.unlock();
//        }
    }

    @Override
    public boolean hasUsername(String username) {
        //TODO 使用布隆过滤器防止缓存穿透
//        boolean hasUsername = userRegisterCachePenetrationBloomFilter.contains(username);
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username);
        return userMapper.selectOne(queryWrapper) != null;
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        //0代表用户名登录，1代表邮箱登录
        int loginFlag = 0;
        if (requestParam.getUsernameOrMailOrPhone().contains("@")) {
            loginFlag = 1;
        }
        LambdaQueryWrapper<UserDO> queryWrapper = null;
        UserDO userDO = null;
        if (loginFlag == 0) {
            queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                    .eq(UserDO::getUsername, requestParam.getUsernameOrMailOrPhone())
                    .eq(UserDO::getPassword, requestParam.getPassword());
            userDO = userMapper.selectOne(queryWrapper);
        } else {
            queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                    .eq(UserDO::getMail, requestParam.getUsernameOrMailOrPhone())
                    .eq(UserDO::getPassword, requestParam.getPassword());
            userDO = userMapper.selectOne(queryWrapper);
        }
        if (userDO != null) {
            UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                    .userName(userDO.getUsername())
                    .userId(String.valueOf(userDO.getId()))
                    .realName(userDO.getRealName())
                    .build();
            String accessToken = JWTUtil.generateAccessToken(userInfoDTO);
            UserLoginRespDTO actual = new UserLoginRespDTO(String.valueOf(userDO.getId()),
                    requestParam.getUsernameOrMailOrPhone(), userDO.getRealName(), accessToken);
            distributedCache.put(accessToken, JSON.toJSONString(actual), 30, TimeUnit.MINUTES);
            return actual;
        }
        throw new ServiceException(USER_NAME_VERIFY_ERROR);
    }

    @Override
    public UserLoginRespDTO checkLogin(String accessToken) {
        return distributedCache.get(accessToken, UserLoginRespDTO.class);
    }

    @Override
    public void logout(String accessToken) {
        if (StrUtil.isNotBlank(accessToken)) {
            distributedCache.delete(accessToken);
        }
    }
}
