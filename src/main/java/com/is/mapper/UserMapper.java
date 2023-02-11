package com.is.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.is.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author is
 * @since 2023-02-10
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from m_user where username=#{username}")
    public User queryUserByName(String username);

    @Update("update m_user set last_login=#{time} where id=#{id}")
    public Boolean updateLastLogin(LocalDateTime time, Integer id);
}
