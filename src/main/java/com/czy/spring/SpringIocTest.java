package com.czy.spring;

import com.czy.spring.core.ApplicationContext;
import com.czy.spring.test.service.UserService;
import org.junit.jupiter.api.Test;

/**
 * ClassName: SpringIocTest
 * Package: com.czy.spring
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
public class SpringIocTest {
    @Test
    public void testIoc(){
        ApplicationContext applicationContext = new com.atczy.annotation.AnnotationApplicationContext("com.czy.spring.test");

        UserService userService = (UserService) applicationContext.getBean(UserService.class);

        userService.out();
        System.out.println("run success");
    }
}
