package com.is.service.impl;

import com.is.blog_demo.entity.Blog;
import com.is.blog_demo.mapper.BlogMapper;
import com.is.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author is
 * @since 2023-02-10
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

}
