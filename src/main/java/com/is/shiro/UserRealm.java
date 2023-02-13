package com.is.shiro;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.is.entity.User;
import com.is.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;


public class UserRealm extends AuthorizingRealm {

    @Autowired
    IUserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {


        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //拿到当前登录对象
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal();
        User user = userService.getById(currentUser.getId());
        //设置当前用户角色
        Set<String> roles = new HashSet<>();
        roles.add(user.getRoles());
        info.setRoles(roles);
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userToken.getUsername());
        User user = userService.getOne(wrapper);
        if (user == null) {
            return null;
        }
        if (!userToken.getUsername().equals(user.getUsername())) {
            return null;//抛出异常 UnknownAccountException
        }
        //密码认证shiro做,加密了
        ByteSource salt = ByteSource.Util.bytes(user.getUsername());
        String realmName = this.getName();
        return new SimpleAuthenticationInfo(user, user.getPassword(),salt, realmName);
    }
}
