package com.thomas.webhw.dao;

import com.thomas.webhw.meta.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("UserDao")
public interface UserDao {
    @Select("SELECT * FROM UserBalance")
    public List<User> getUserList();
}
