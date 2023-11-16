//package org.opengoofy.index12306.biz.userservice.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.opengoofy.index12306.biz.userservice.dao.entity.UserDO;
//import org.opengoofy.index12306.biz.userservice.dao.entity.UserDeletionDO;
//import org.opengoofy.index12306.biz.userservice.dao.mapper.UserDeletionMapper;
//import org.opengoofy.index12306.biz.userservice.dao.mapper.UserMapper;
//import org.opengoofy.index12306.biz.userservice.dto.resp.UserQueryRespDTO;
//import org.opengoofy.index12306.biz.userservice.service.UserService;
//import org.opengoofy.index12306.framework.starter.common.toolkit.BeanUtil;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@AllArgsConstructor
//@Slf4j
//@Service
//public class UserServiceImpl implements UserService {
//    private final UserMapper userMapper;
//    private final UserDeletionMapper userDeletionMapper;
//
//    @Override
//    public UserQueryRespDTO queryUserByUsername(String username) {
//        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username);
//        UserDO userDO = userMapper.selectOne(queryWrapper);
//        return BeanUtil.convert(userDO, UserQueryRespDTO.class);
//    }
//
//    @Override
//    public Integer queryUserDeletionNum(Integer idType, String idCard) {
//        LambdaQueryWrapper<UserDeletionDO> queryWrapper = Wrappers.lambdaQuery(UserDeletionDO.class)
//                .eq(UserDeletionDO::getIdType, idType)
//                .eq(UserDeletionDO::getIdCard, idCard);
//        //TODO 改为先查缓存，统计1024个redis set中存在多少个reuse name
//        Long count = userDeletionMapper.selectCount(queryWrapper);
//        return Optional.ofNullable(count).map(Long::intValue).orElse(0);
//    }
//}
