package com.is.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
@RequestMapping("blogs")
@RequiresAuthentication
public class BlogController {

}
