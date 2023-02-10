package com.is.controller;

import com.is.common.lang.Result;
import com.is.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author is
 * @since 2023-02-10
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/index")
    public Object index() {
        return Result.succ(userService.getById(1L));
    }
}
