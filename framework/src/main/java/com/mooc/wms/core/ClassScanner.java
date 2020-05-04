package com.mooc.wms.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Bennett_Wang on 2020/5/3
 * 类扫描器
 */
public class ClassScanner {
    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        //存储扫描包的类
        List<Class<?>> classList = new ArrayList<>();
        //把包名转换为文件路径，即把包名中的"."换成"/"就可以了
        String path = packageName.replace(".","/");
        //使用类加载器，通过路径加载文件
        //获取默认类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //调用getResource()方法，其返回的是可遍历的URL资源
        Enumeration<URL> resources = classLoader.getResources(path);
        while(resources.hasMoreElements()) {
            //获取资源
            URL resource = resources.nextElement();
            //获取资源后，要判断资源的类型，可以用protocol属性获取到
            //因为这个项目最后会打包成jar包，所以我们设置资源类型为jar包，当资源类型为jar包的时候
            //进行处理
            if(resource.getProtocol().contains("jar")) {
                //如果是jar包，则尝试获取jar包路径，jar包路径可以通过jarURLConnection获取
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                //可以用这个jarURLConnection获取到jar包的路径名
                String jarFilePath = jarURLConnection.getJarFile().getName();
                classList.addAll(getClassesFromJar(jarFilePath,path));

            } else {
                // todo
            }
        }
        return classList;
    }
    /*
     * 此方法通过jar包的路径来获取到jar包所有的类
     * String jarFilePath为jar包的路径
     * String path——一个jar包可能有多个类文件，可以用path指定哪些类文件是我们需要的，path为类的相对路径
     */
    private static List<Class<?>> getClassesFromJar(String jarFilePath,String path) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while(jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName = jarEntry.getName();// com/mooc/wms/test/Test.class
            if(entryName.startsWith(path) && entryName.endsWith(".class")) {
                String classFullName = entryName.replace("/",".").substring(0,entryName.length() - 6);
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}
