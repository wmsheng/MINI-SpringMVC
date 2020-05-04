package com.mooc.wms.starter;

import com.mooc.wms.beans.BeanFactory;
import com.mooc.wms.core.ClassScanner;
import com.mooc.wms.web.handler.HandlerManager;
import com.mooc.wms.web.server.TomcatServer;
import org.apache.catalina.LifecycleException;

import java.util.List;

/**
 * @author Bennett_Wang on 2020/5/2
 */
public class MiniApplication {
    public static void run(Class<?> cls, String[] args) {
        System.out.println("Hello mini-spring!");
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer();
            List<Class<?>> classList = ClassScanner.scanClasses(cls.getPackage().getName());

            //使用Bean工厂初始化Bean
            BeanFactory.initBean(classList);

            //在框架入口类中调用HandlerManager来初始化所有的MappingHandler
            HandlerManager.resolveMappingHandler(classList);

            classList.forEach(it -> System.out.println(it.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
