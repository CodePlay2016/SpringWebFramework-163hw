package com.thomas.webhw.Utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Util {
    public static ApplicationContext getApplicationContext(String configLocation) {
        return new ClassPathXmlApplicationContext(configLocation);
    }
}
