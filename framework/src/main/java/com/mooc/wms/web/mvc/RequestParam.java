package com.mooc.wms.web.mvc;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
//这个注解目标是Controller注解的参数
@Target(ElementType.PARAMETER)
public @interface RequestParam {
    //需要添加一个参数用来表示他要接收的param String中的key
    String value();
}
