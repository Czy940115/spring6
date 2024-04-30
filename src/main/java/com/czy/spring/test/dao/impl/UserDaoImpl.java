package com.czy.spring.test.dao.impl;

import com.czy.spring.core.annotation.Bean;
import com.czy.spring.test.dao.UserDao;

/**
 * ClassName: UserDaoImpl
 * Package: com.czy.spring6.test.dao.impl
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Bean
public class UserDaoImpl implements UserDao {
    @Override
    public void print() {
        System.out.println("Dao层执行结束");
    }
}
