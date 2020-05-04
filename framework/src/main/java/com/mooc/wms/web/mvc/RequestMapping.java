package com.mooc.wms.web.mvc;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
//目标：Controller中的方法
@Target(ElementType.METHOD)
public @interface RequestMapping {
    //需要添加属性用来保存映射的URI
    String value();
}
