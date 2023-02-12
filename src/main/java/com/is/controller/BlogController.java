package com.is.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.is.common.lang.Result;
import com.is.entity.Blog;
import com.is.entity.User;
import com.is.service.IBlogService;
import com.is.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author is
 * @since 2023-02-10
 */
@RestController
@RequestMapping("/blogs")
@RequiresAuthentication
public class BlogController {
    @Autowired
    IBlogService blogService;
    @Autowired
    IUserService userService;

    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        Blog blog = blogService.getById(id);
        if(blog == null) {
            return Result.fail("该博客已被删除！");
        }
        return Result.succ(blog);
    }

    @PostMapping
    public Result add(@RequestBody Blog blog) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        blog.setUserId(user.getId());
        blog.setStatus(true);
        blog.setCreated(LocalDateTime.now());
        boolean flag = blogService.save(blog);
        return flag?Result.succ("发布成功",blog):Result.fail("发布失败");
    }
    @PutMapping
    public Result update(@RequestBody Blog blog) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if(!user.getId().equals( blog.getUserId())&& !user.getPerms().equals("admin") ){
            return Result.fail("没有修改权限");
        }
        boolean flag = blogService.updateById(blog);
        return flag?Result.succ("修改成功",blog):Result.fail("修改失败");
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if(!user.getId().equals(blogService.getById(id).getUserId())&& !user.getPerms().equals("admin") ){
            return Result.fail("没有删除权限");
        }
        boolean flag = blogService.removeById(id);
        return flag?Result.succ("删除成功"):Result.fail("删除失败");
    }
    @GetMapping
    public Result getAll() {
        List<Blog> blogs = blogService.list();
        return Result.succ(blogs);

    }
    @GetMapping("/{currentPage}/{size}")
    public Result blogPages(@PathVariable Integer currentPage, @PathVariable Integer size) {
        LambdaQueryWrapper<Blog> blogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogLambdaQueryWrapper.isNotNull(Blog::getId);
        Page<Blog> page = blogService.page(new Page<>(currentPage, size), blogLambdaQueryWrapper);
        if( currentPage > page.getPages()) {
            page = blogService.page(new Page<>(page.getPages(), size), blogLambdaQueryWrapper);
        }


        return Result.succ(page);
    }

}
