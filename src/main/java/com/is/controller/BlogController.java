package com.is.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.is.common.lang.Result;
import com.is.entity.Blog;
import com.is.entity.User;
import com.is.service.IBlogService;
import com.is.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author is
 * @since 2023-02-10
 */
@RestController
@RequestMapping("/blogs")
public class BlogController {
    @Autowired
    IBlogService blogService;
    @Autowired
    IUserService userService;


    @RequiresAuthentication
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        Blog blog = blogService.getById(id);
        if(blog == null) {
            return Result.fail("该博客已被删除！");
        }
        return Result.succ(blog);
    }

    @RequiresAuthentication
    @RequiresRoles(value = {"admin","user"},logical = Logical.OR)
    @PostMapping
    public Result add(@RequestBody Blog blog) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        blog.setUserId(user.getId());
        blog.setStatus(true);
//        blog.setCreated(LocalDateTime.now());
        boolean flag = blogService.save(blog);
        return flag?Result.succ("发布成功",blog):Result.fail("发布失败");
    }

    @RequiresAuthentication
    @PutMapping
    @RequiresRoles(value = {"admin","user"},logical = Logical.OR)
    public Result update(@RequestBody Blog blog) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if(!user.getId().equals( blog.getUserId())&& !subject.hasRole("admin") ){
            return Result.fail("没有修改权限");
        }
        blog.setUserId(null);
        boolean flag = blogService.updateById(blog);
        return flag?Result.succ("修改成功",blog):Result.fail("修改失败");
    }
    @RequiresAuthentication
    @RequiresRoles(value = {"admin","user"},logical = Logical.OR)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if(!user.getId().equals(blogService.getById(id).getUserId())&& !subject.hasRole("admin")){
            return Result.fail("没有删除权限");
        }
        boolean flag = blogService.removeById(id);
        return flag?Result.succ("删除成功"):Result.fail("删除失败");
    }

    @GetMapping
    public Result getAll() {
        LambdaQueryWrapper<Blog> blogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogLambdaQueryWrapper.eq(Blog::getStatus, true);
        List<Blog> blogs = blogService.list(blogLambdaQueryWrapper);
        return Result.succ(blogs);

    }

    @GetMapping("/{currentPage}/{size}")
    public Result blogPages(@PathVariable Integer currentPage,
                            @PathVariable Integer size,
                            @RequestParam(required = false, defaultValue = "") String title,
                            @RequestParam(required = false, defaultValue = "") String description,
                            @RequestParam(required = false, defaultValue = "") String content,
                            @RequestParam(required = false, defaultValue = "") Integer userId) {
        LambdaQueryWrapper<Blog> blogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogLambdaQueryWrapper
                .isNotNull(Blog::getId)
                .eq(Blog::getStatus, true)
                .and(wrapper -> wrapper
                        .like(Blog::getTitle, title)
                        .like(Blog::getDescription, description)
                        .like(Blog::getContent, content)
                        .eq(userId != null, Blog::getUserId, userId));
        Page<Blog> page = blogService.page(new Page<>(currentPage, size), blogLambdaQueryWrapper);
        if (currentPage > page.getPages()) {
            page = blogService.page(new Page<>(page.getPages(), size), blogLambdaQueryWrapper);
        }


        return Result.succ(page);
    }

}
