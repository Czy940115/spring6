package com.czy.spring.test.service.impl;

import com.czy.spring.core.annotation.Bean;
import com.czy.spring.core.annotation.Di;
import com.czy.spring.test.dao.UserDao;
import com.czy.spring.test.service.UserService;

/**
 * ClassName: UserServiceImpl
 * Package: com.czy.spring6.test.service.impl
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Bean
public class UserServiceImpl implements UserService {

    @Di
    private UserDao userDao;

    @Override
    public void out() {
        // userDao.print();
        System.out.println("Service层执行结束");
    }
}
