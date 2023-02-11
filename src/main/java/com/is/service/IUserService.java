package com.is.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.is.entity.User;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author is
 * @since 2023-02-10
 */
public interface IUserService extends IService<User> {

    public User queryUserByName(String username);

    public Boolean updateLastLogin(Integer id);
}
