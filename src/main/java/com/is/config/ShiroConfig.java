package com.is.config;

import com.is.shiro.UserRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * shiro启用注解拦截控制器
 */
@Configuration
public class ShiroConfig {

    //ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro的内置过滤器
        /*
            anno：无需认证就可以访问
            authc：必须认证才可以访问
            user：必须拥有 记住我 功能才可以访问
            perms：拥有对某个资源的权限才能访问
            role：拥有某个角色权限才能访问
         */
        //拦截
//        Map<String, String> filterMap = new LinkedHashMap<>();
        //授权，正常情况下没有授权会跳转到未授权页面
//        filterMap.put("/user/add","perms[user:add]");
//        filterMap.put("/user/update","perms[user:update]");
//        filterMap.put("/user/*", "authc");
//        filterMap.put("/blog/*", "authc");
//        bean.setFilterChainDefinitionMap(filterMap);

//        //设置登录请求
//        bean.setLoginUrl("/toLogin");
        //未授权页面
        bean.setUnauthorizedUrl("/noauth");
        return bean;
    }

    //DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    //创建Realm对象，需要自定义
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }
}

