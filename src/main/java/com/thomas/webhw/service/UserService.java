package com.thomas.webhw.service;

import com.thomas.webhw.dao.UserDao;
import com.thomas.webhw.meta.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Component
public class UserService {

    @Resource(name = "UserDao")
    private UserDao dao;

    public UserDao getDao() {
        return this.dao;
    }

    public List<User> getUserList() {
        return this.dao.getUserList();
    }

}
