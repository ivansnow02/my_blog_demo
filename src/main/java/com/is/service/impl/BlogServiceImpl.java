package com.is.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.is.entity.Blog;
import com.is.mapper.BlogMapper;
import com.is.service.IBlogService;
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
