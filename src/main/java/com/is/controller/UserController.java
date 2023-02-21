package com.is.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.is.common.lang.Result;
import com.is.entity.User;
import com.is.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author is
 * @since 2023-02-10
 */
@RestController
@RequestMapping("/user")
@RequiresAuthentication
public class UserController {
    @Autowired
    private IUserService userService;


    @DeleteMapping("/{id}")
    @RequiresRoles("admin")
    public Result delete(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.succ("删除成功");
    }
    @PutMapping
    @RequiresRoles(value = {"admin","user"},logical = Logical.OR)
    public Result update(@RequestBody User user) {
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal();
        if(!currentUser.getId().equals(user.getId()) && !subject.hasRole("admin")) {
            return Result.fail("没有权限修改");
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        User user1 = userService.getOne(userLambdaQueryWrapper);
        if (user1 != null && !user1.getId().equals(user.getId())) {
            return Result.fail("4000", "用户名重复");
        }
        if (!subject.hasRole("admin")) {
            user.setRoles(null);
        }

        ByteSource salt = ByteSource.Util.bytes(user.getUsername());
        String newPwd = new SimpleHash("MD5",user.getPassword(),salt,1024).toHex();
        user.setPassword(newPwd);
        userService.updateById(user);
        return Result.succ("修改成功",user);
    }
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        User user = userService.getById(id);
        if(user == null) {
            return Result.fail("用户消失了！");
        }
        return Result.succ(user);
    }

    @GetMapping
    public Result getAll() {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<User> users = userService.list(userLambdaQueryWrapper);
        return Result.succ(users);

    }

    @GetMapping("/{currentPage}/{size}")
    public Result blogPages(@PathVariable Integer currentPage,
                            @PathVariable Integer size,
                            @RequestParam(required = false) String username,
                            @RequestParam(required = false) String email) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper
                .isNotNull(User::getId)
                .and(wrapper -> wrapper
                        .like(!username.isEmpty(), User::getUsername, username)
                        .eq(!email.isEmpty(), User::getEmail, email)
                );
        Page<User> page = userService.page(new Page<>(currentPage, size), userLambdaQueryWrapper);
        if (currentPage > page.getPages()) {
            page = userService.page(new Page<>(page.getPages(), size), userLambdaQueryWrapper);
        }


        return Result.succ(page);
    }

}
