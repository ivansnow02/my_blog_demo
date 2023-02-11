package com.is.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.is.entity.User;
import com.is.mapper.UserMapper;
import com.is.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author is
 * @since 2023-02-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper userMapper;


    @Override
    public Boolean updateLastLogin(Integer id) {
        return userMapper.updateLastLogin(LocalDateTime.now(), id);
    }
}
