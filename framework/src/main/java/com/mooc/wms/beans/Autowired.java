package com.mooc.wms.beans;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
//使用在Bean的属性上
public @interface Autowired {
}
