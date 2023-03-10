package com.is.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.is.common.lang.Result;
import com.is.entity.User;
import com.is.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author is
 * @since 2023-02-10
 */
@RestController
public class AccountController {
    @Autowired
    IUserService userService;


    @PostMapping("/login")
    public Result login(@RequestBody User loginUser) {
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        String name = loginUser.getUsername();
        String pwd = loginUser.getPassword();
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(name, pwd);
        //执行登录方法
        try {
            subject.login(token);
            LambdaQueryWrapper<User> nameWrapper = new LambdaQueryWrapper<>();
            nameWrapper.eq(User::getUsername, name);
            User currentUser = userService.getOne(nameWrapper);
            currentUser.setPassword(null);
//            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
//            updateWrapper.eq(User::getUsername, name).set(User::getUsername, name);
            userService.updateById(currentUser);
//            userService.update(updateWrapper);
            return Result.succ("登录成功", currentUser);
        } catch (UnknownAccountException e) {
            return Result.fail("用户名错误");
        } catch (IncorrectCredentialsException e) {
            return Result.fail("密码错误");
        }
    }

    @RequestMapping("/noAuth")
    public Result unauthorized() {
        return Result.fail("未经授权无法访问");
    }

    @RequestMapping("/toLogin")
    public Result toLogin() {
        return Result.fail("请先登录");
    }

    @RequiresAuthentication
    @RequestMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

    @PostMapping("/reg")
    public Result reg(@Validated @RequestBody User regUser) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, regUser.getUsername());
        if (userService.getOne(userLambdaQueryWrapper) != null) {
            return Result.fail("4000", "用户名重复");
        }
//        regUser.setCreated(LocalDateTime.now());
        regUser.setAvatar("https://i.328888.xyz/2023/02/11/RvKJF.th.jpeg");
        regUser.setRoles("user");
        ByteSource salt = ByteSource.Util.bytes(regUser.getUsername());
        String newPwd = new SimpleHash("MD5",regUser.getPassword(),salt,1024).toHex();
        regUser.setPassword(newPwd);
        userService.save(regUser);
        return Result.succ("注册成功", regUser);

    }


}
