package com.thomas.webhw.web.controller;

import com.thomas.webhw.Utils.Util;
import com.thomas.webhw.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
public class MyController {
//    @Resource
//    public UserService service = Util.getApplicationContext(
//            "application-context-dao.xml").getBean(UserService.class);

    @Resource
    private UserService service;

    @RequestMapping("/getResource")
    @ResponseBody
    public ModelAndView getResource() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("users");//这个名字要和ftl文件的名字对应
        mav.addObject("userList", service.getUserList());//这里的attributeName要和freemarker里对应
        return mav;
    }
}
