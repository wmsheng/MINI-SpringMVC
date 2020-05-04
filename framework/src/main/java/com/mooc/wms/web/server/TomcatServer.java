package com.mooc.wms.web.server;

import com.mooc.wms.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

/**
 * @author Bennett_Wang on 2020/5/2
 */
public class TomcatServer {
    public Tomcat tomcat;
    public String[] args;

    public TomcatServer(String[] args) {
        this.args = args;
    }

    public void startServer() throws LifecycleException {
        tomcat = new Tomcat();
        tomcat.setPort(6699);
        tomcat.start();

        //初始化Context容器
        Context context = new StandardContext();
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());

        // 创建TestServlet实例
        DispatcherServlet servlet = new DispatcherServlet();
        //使用Tomcat的静态方法addServlet
        Tomcat.addServlet(context,"dispatcherServlet",servlet)
                .setAsyncSupported(true);

        //将Context容器和"testServlet"实例关联起来
        context.addServletMappingDecoded("/","dispatcherServlet");
        tomcat.getHost().addChild(context);

        Thread awaitThread = new Thread("tomcat_await_thread") {
            @Override
            public void run() {
                TomcatServer.this.tomcat.getServer().await();
            }
        };
        awaitThread.setDaemon(false);
        awaitThread.run();
    }
}
