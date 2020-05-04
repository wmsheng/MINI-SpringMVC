package com.mooc.wms.beans;

import com.mooc.wms.web.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Bennett_Wang on 2020/5/4
 * Bean工厂，用来初始化和保存Bean
 */
public class BeanFactory {
    //映射在后期可能需要并发处理，故用ConcurrentHashMap
    private static Map<Class<?>, Object> classToBean = new ConcurrentHashMap<>();

    //定义一个方法用来从映射里获取Bean
    public static Object getBean(Class<?> cls) {
        return classToBean.get(cls);
    }

    //完善Bean初始化的方法
    public static void initBean(List<Class<?>> classList) throws Exception {
        List<Class<?>> toCreate = new ArrayList<>(classList);
        //写一个循环来初始化Bean
        while(toCreate.size() != 0) { //当容器中还有类定义时，我们不断遍历，看类定义能否初始化为Bean
            //每初始化一个Bean,就把它从容器中删掉
            int remainSize = toCreate.size();
            for (int i = 0; i < toCreate.size(); i++) {
                //如果完成了创建，就把它从容器中删除
                if(finishCreate(toCreate.get(i))) {
                    toCreate.remove(i);
                }
            }
            //如果每次遍历之后容器大小没有变化，那么就是陷入了死循环，我们要抛出异常
            if(toCreate.size() == remainSize) {
                throw new Exception("cycle dependency!");
            }
        }
    }

    //创建完成则返回true
    private static boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        //首先判断其是否需要初始化为Bean。如果其不需要被初始化为Bean，
        //则直接返回true,然后把它从初始化列表中删除
        if(!cls.isAnnotationPresent(Bean.class) && !cls.isAnnotationPresent(Controller.class)) {
            return true;
        }

        //bean的初始化
        Object bean = cls.newInstance();
        //遍历属性，看它有没有需要解决的依赖
        for(Field field : cls.getDeclaredFields()) {
            //如果这个属性被Autowired注解了，表示它需要使用依赖注入来解决这个依赖
            if(field.isAnnotationPresent(Autowired.class)) {
                //此时需要从Bean工厂中获取被依赖的Bean
                //先拿到属性的类型
                Class<?> fieldType = field.getType();
                //通过类型从Bean工厂内获取Bean
                Object reliantBean = BeanFactory.getBean(fieldType);
                if(reliantBean == null) {
                    return false;
                }
                field.setAccessible(true);
                field.set(bean,reliantBean);
            }
        }
        classToBean.put(cls,bean);
        return true;
    }
}
