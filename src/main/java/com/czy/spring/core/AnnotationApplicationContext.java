package com.atczy.annotation;

import com.czy.spring.core.ApplicationContext;
import com.czy.spring.core.annotation.Bean;
import com.czy.spring.core.annotation.Di;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Filter;

/**
 * ClassName: AnnotationApplicationContext
 * Package: com.atczy.annotation
 * Description:
 *  初始化该对象时，获取所有的具备Bean注释的类，将其封装到beanFactory的Map容器当中，初始化
 * @Author Ziyun Chen
 * @Create 2024/2/28 18:36
 * @Version 1.0  
 */
public class AnnotationApplicationContext implements ApplicationContext {
    private static Map<Class, Object> beanFactory = new HashMap<>();// 存储bean的容器
    private static String rootPath;

    @Override
    public Object getBean(Class clazz) {
        return beanFactory.get(clazz);
    }

    /**
     * 根据包扫描加载bean
     *  创建有参构造，传递包路径，设置包扫描规则
     *  当前包及其子包，那个类有@Bean注解，把这个类通过反射实例化
     */
    public AnnotationApplicationContext(String basePackage){
        // 1.获取该包的绝对路径
        String packagePath = basePackage.replaceAll("\\.", "\\\\");
        try {
            Enumeration<URL> urls =
                    Thread.currentThread().getContextClassLoader().getResources(packagePath);

            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                String filePath = URLDecoder.decode(url.getFile(), "utf-8");
                rootPath = filePath.substring(0, filePath.length()-packagePath.length());// 获取跟路径

                // 加载Bean,获取所有的具备Bean注释的类，将其封装到beanFactory的Map容器当中
                loadBean(new File(filePath));

                // 加载Di
                loadDi();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadBean(File file) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 1.查看当前文件夹是否是空文件夹？是-返回，否-查看当前文件夹下的所有文件
        if (!file.exists()){
            return;
        }
        if (file.isDirectory()){
            // 2.遍历当前文件夹下的所有文件
            File[] files = file.listFiles();
            if (files == null || files.length == 0){
                return;
            }
            for (File childFile :  files){
                // 3.当前文件是否是文件夹？是-递归，否-查看当前文件
                if (childFile.isDirectory()){
                    loadBean(childFile);
                }else {
                    // 4.查看当前class文件是否有Bean注解
                    // 4.1 通过文件名获取全类名，第一步把绝对路径去掉
                    String pathWithClass = childFile.getAbsolutePath().substring(rootPath.length()-1);
                    // 选中class文件
                    if (pathWithClass.contains(".class")){
                        // 将路径中的/转化为.,把.class去掉 -- 获取class的全类名
                        String fullName = pathWithClass.replaceAll("\\\\", "\\.").
                                replace(".class", "");
                        // 4.2 是否有Bean注解，
                        Class<?> clazz = Class.forName(fullName);

                        // 将非接口类的实例放入Map中
                        if (!clazz.isInterface()){
                            Bean annotation = clazz.getAnnotation(Bean.class);
                            if (annotation != null){
                                // 实例化该对象
                                Object instance = clazz.getDeclaredConstructor().newInstance();
                                if (clazz.getInterfaces().length > 0){
                                    // 判断是否有Implement接口 是-以该接口为clazz传入Map中
                                    beanFactory.put(clazz.getInterfaces()[0], instance);
                                }else {
                                    // 否-以该类为clazz传入Map
                                    beanFactory.put(clazz, instance);
                                }

                            }
                        }
                    }

                }


            }
        }

    }

    private void loadDi() throws IllegalAccessException {
        // 实例化对象在beanFactory的map集合中
        // 1.遍历beanFactory的map集合
        Set<Map.Entry<Class, Object>> entries = beanFactory.entrySet();
        for (Map.Entry<Class, Object> entry : entries){
            // 2.获取map的value值，每个属性获取到
            Object obj = entry.getValue();
            Class<?> clazz = obj.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();

            // 3.遍历得到每个对象属性数组，得到每个属性
            for (Field field : declaredFields){
                //4.判断属性上面是否有@Di注解
                Di annotation = field.getAnnotation(Di.class);
                if (annotation != null){
                    // 5.如果有@Di注解，把对象进行设置（注入）
                    field.setAccessible(true);
                    field.set(obj, beanFactory.get(field.getType()));
                }

            }
        }

    }
}
