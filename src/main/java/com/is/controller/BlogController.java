package com.is.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.is.common.lang.Result;
import com.is.entity.Blog;
import com.is.service.IBlogService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
@RequestMapping("/blogs")
@RequiresAuthentication
public class BlogController {
    @Autowired
    IBlogService blogService;

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
        boolean flag = blogService.save(blog);
        return flag?Result.succ("发布成功"):Result.fail("发布失败");
    }
    @PutMapping
    public Result update(@RequestBody Blog blog) {
        boolean flag = blogService.updateById(blog);
        return flag?Result.succ("修改成功"):Result.fail("修改失败");
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        boolean flag = blogService.removeById(id);
        return flag?Result.succ("删除成功"):Result.fail("删除失败");
    }
    @GetMapping
    public Result getAll() {
        List<Blog> blogs = blogService.list();
        return Result.succ(blogs);

    }
    @GetMapping("/{currentPage}/{size}")
    public Result blogs(@PathVariable Integer currentPage) {
        if(currentPage == null || currentPage < 1) currentPage = 1;
        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        return Result.succ(pageData);
    }

}
