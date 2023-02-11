package com.is.controller;

import com.is.common.lang.Result;
import com.is.entity.User;
import com.is.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @RequiresAuthentication
    @GetMapping("/index")
    public Result index() {
        User user = userService.getById(1L);
        return Result.succ(user);
    }

    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user) {
        return Result.succ(user);
    }


}
