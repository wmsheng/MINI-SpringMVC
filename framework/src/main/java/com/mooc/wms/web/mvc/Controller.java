package com.mooc.wms.web.mvc;

import java.lang.annotation.*;

@Documented
//生命周期为运行期
@Retention(RetentionPolicy.RUNTIME)
//作用目标是：类
@Target(ElementType.TYPE)
public @interface Controller {
}
