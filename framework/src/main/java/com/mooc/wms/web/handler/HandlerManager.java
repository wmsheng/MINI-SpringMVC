package com.mooc.wms.web.handler;

import com.mooc.wms.web.mvc.Controller;
import com.mooc.wms.web.mvc.RequestMapping;
import com.mooc.wms.web.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bennett_Wang on 2020/5/3
 */
public class HandlerManager {
    //静态属性，用于保存多个mappingHandler
    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    //一个方法，把controller中的类挑选出来，然后初始化mappingHandler方法初始化成mappingHandler
    public static void resolveMappingHandler(List<Class<?>> classList) {
        for(Class<?> cls : classList) {
            if(cls.isAnnotationPresent(Controller.class)) {
                //如果Controller注解存在，我们解析这个controller类
                parseHandlerFromController(cls);
            }
        }
    }

    //完善子方法
    private static void parseHandlerFromController(Class<?> cls) {
        //先用反射获取到这个类的所有方法
        Method[] methods = cls.getDeclaredMethods();
        //遍历所有方法，找到对应的requestMapping注解的方法
        for(Method method : methods) {
            if(!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            //找到这个方法后，从这个方法的属性中获取所有构成MappingHandler的属性
            //首先是uri，可以直接从注解的属性中获取到
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();
            //之后的所需要的参数，我们初始化一个容器来暂时存储一下
            List<String> paramNameList = new ArrayList<>();
            //遍历方法所有的参数，依次判断并找到被requestParam注解的参数
            for(Parameter parameter : method.getParameters()) {
                if(parameter.isAnnotationPresent(RequestParam.class)) {
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);
            //方法和类都是已知的，下面来构造MappingHandler
            MappingHandler mappingHandler = new MappingHandler(uri,method,cls,params);
            //最后把构造好的handler放到管理器的静态属性里
            HandlerManager.mappingHandlerList.add(mappingHandler);
        }
    }
}
