package com.mooc.wms.web.handler;

import com.mooc.wms.beans.BeanFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Bennett_Wang on 2020/5/3
 */
public class MappingHandler {
    //要处理的URI
    private String uri;
    //对应的controller方法
    private Method method;
    //对应的controller类
    private Class<?> controller;
    //调用方法需要的参数
    private String[] args;

    public boolean handle(ServletRequest req, ServletResponse res) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        String requestUri = ((HttpServletRequest) req).getRequestURI();
        //判断handler中的uri和请求的uri是否相等
        if(!uri.equals(requestUri)) {
            return false;
        }
        //如果uri相等，那么我们需要调用handler里的method方法
        //首先我们准备一下参数
        Object[] parameters = new Object[args.length];
        //通过参数名依次从ServletRequest里面获取到这个参数
        for (int i = 0; i < args.length; i++) {
            parameters[i] = req.getParameter(args[i]);
        }
        //使用Bean工厂来初始化Bean
        Object ctl = BeanFactory.getBean(controller);
        //由于controller可能会返回多种类型，我们用object来存储结果
        Object response = method.invoke(ctl,parameters);
        //把方法返回的结果放入到ServletResponse里面去
        res.getWriter().println(response.toString());
        //处理成功后返回true
        return true;

    }

    MappingHandler(String uri, Method method,Class<?> cls,String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = cls;
        this.args = args;
    }
}
